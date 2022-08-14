package cn.loyx.paxos.impl.naive;

import cn.loyx.paxos.PaxosValue;
import cn.loyx.paxos.ProposalNo;
import cn.loyx.paxos.comm.protocol.*;
import cn.loyx.paxos.conf.Configuration;
import lombok.extern.log4j.Log4j;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Log4j
public class Proposer {

    enum ProposerState {
        IDLE, PREPARE, ACCEPT
    }
    private final Configuration conf;
    private PaxosValue proposeValue;
    private PaxosValue confirmedValue;
    private ProposerState state;

    private final Set<Integer> prepareOk;
    private final Set<Integer> acceptOk;
    private ProposalNo currentNo;
    private ProposalNo acceptorAcceptedNo;
    private PaxosValue acceptorAcceptedValue;

    public Proposer(Configuration conf) {
        this.conf = conf;
        this.proposeValue = null;
        this.prepareOk = new HashSet<>();
        this.acceptOk = new HashSet<>();
        this.state = ProposerState.IDLE;
        this.acceptorAcceptedValue = null;
        this.acceptorAcceptedNo = ProposalNo.empty();
    }

    public Optional<PaxosPacket> handlePacket(PaxosPacket packet){
//        log.info("Proposer get a packet: " + packet);
        switch (packet.getType()){
            case PROPOSE_PACKET:
                return Optional.of(propose(packet));
            case PREPARE_RESPONSE_PACKET:
                PaxosPacket acceptPacket = onPrepareResponse(packet);
                return acceptPacket == null ? Optional.empty() : Optional.of(acceptPacket);
            case ACCEPT_RESPONSE_PACKET:
                PaxosPacket rePreparePacket = onAcceptResponse(packet);
                return rePreparePacket == null ? Optional.empty(): Optional.of(rePreparePacket);
            default:
                return Optional.empty();
        }
    }

    private PaxosPacket propose(PaxosPacket packet){
        this.proposeValue = ((ProposeValue)packet.getLoad()).getValue();
        return prepare();
    }

    private PaxosPacket prepare(){
        this.state = ProposerState.PREPARE;
        PrepareNo newNo = PrepareNo.newNo();
        currentNo = newNo;
        PaxosPacket preparePacket = new PaxosPacket(
                PacketTarget.ACCEPTOR,
                conf.getIdList(),
                conf.getId(),
                PacketType.PREPARE_PACKET,
                newNo
        );
        log.info("Proposer send a prepare packet: " + preparePacket);
        // todo timeout
        return preparePacket;
    }

    private PaxosPacket onPrepareResponse(PaxosPacket packet){
        if (this.state != ProposerState.PREPARE) {
            log.info(String.format("In %s state, not handle prepare response: %s", state, packet));
            return null;
        }
        log.info("Proposer handle a prepare response: " + packet);
        PrepareResponse resp = (PrepareResponse) packet.getLoad();
        if (resp.isPrepareOk()){
            this.prepareOk.add(packet.getSrcId());
            if (resp.getAcceptedNo().gt(acceptorAcceptedNo)){
                acceptorAcceptedNo = resp.getAcceptedNo();
                acceptorAcceptedValue = resp.getAcceptedValue();
            }
            if (prepareOk.size() >= conf.getAcceptorNum() / 2 + 1){
                PaxosValue acceptValue;
                if (acceptorAcceptedValue == null){
                    acceptValue = proposeValue;
                } else {
                    acceptValue = acceptorAcceptedValue;
                }
                return accept(currentNo, acceptValue);
            }
        }
        // todo early retry
        return null;
    }

    private PaxosPacket accept(ProposalNo currentNo, PaxosValue acceptValue){
        this.state = ProposerState.ACCEPT;
        confirmedValue = acceptValue;
        PaxosPacket acceptPacket = new PaxosPacket(
                PacketTarget.ACCEPTOR,
                conf.getIdList(),
                conf.getId(),
                PacketType.ACCEPT_PACKET,
                AcceptLoad.of(currentNo, acceptValue)
        );
        log.info("Proposer send a accept packet: " + acceptPacket);
        return acceptPacket;
    }

    private PaxosPacket onAcceptResponse(PaxosPacket packet){
        if (this.state != ProposerState.ACCEPT){
            log.info(String.format("In %s state, not handle accept response: %s", state, packet));
            return null;
        }
        AcceptResponse load = (AcceptResponse) packet.getLoad();
        if (load.isAcceptOk()){
            acceptOk.add(packet.getSrcId());
            if (acceptOk.size() >= conf.getAcceptorNum() / 2 + 1){
                done();
            }
        }
        // todo early retry
        return null;
    }

    private void done(){
        String result = String.format("propose value %s, %s all machine accept %s",
                proposeValue,
                proposeValue.equals(confirmedValue) ? "and" : "but",
                confirmedValue);
        log.info(result);
        state = ProposerState.IDLE;
    }

}
