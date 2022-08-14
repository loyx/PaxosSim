package cn.loyx.paxos.impl.naive;

import cn.loyx.paxos.PaxosValue;
import cn.loyx.paxos.ProposalNo;
import cn.loyx.paxos.comm.protocol.*;
import cn.loyx.paxos.conf.Configuration;
import lombok.extern.log4j.Log4j;

import java.util.Optional;

@Log4j
public class Proposer {
    private final Configuration conf;
    private PaxosValue unconfirmedValue;

    public Proposer(Configuration conf) {
        this.conf = conf;
        this.unconfirmedValue = null;
    }

    public Optional<PaxosPacket> handlePacket(PaxosPacket packet){
        log.debug("Proposer get a packet: " + packet);
        switch (packet.getType()){
            case PROPOSE_PACKET:
                return Optional.of(propose(packet));
            case PREPARE_RESPONSE_PACKET:
                break;
            case ACCEPT_RESPONSE_PACKET:
                break;
            default:
                return Optional.empty();
        }
        return Optional.empty();
    }

    private PaxosPacket propose(PaxosPacket packet){
        this.unconfirmedValue = ((ProposeValue)packet.getLoad()).getValue();
        return prepare();
    }

    private PaxosPacket prepare(){
        PaxosPacket preparePacket = new PaxosPacket(
                PacketTarget.ACCEPTOR,
                conf.getIdList(),
                conf.getId(),
                PacketType.PREPARE_PACKET,
                PrepareNo.newNo()
        );
        log.info("Proposer prepare a packet: " + preparePacket);
        return preparePacket;
    }

    private void onPrepareResponse(){

    }

    private void accept(){

    }

    private void onAcceptResponse(){

    }

    private void done(){

    }

}
