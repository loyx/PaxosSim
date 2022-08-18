package cn.loyx.paxos.impl.naive;

import cn.loyx.paxos.PaxosValue;
import cn.loyx.paxos.ProposalNo;
import cn.loyx.paxos.conf.Configuration;
import cn.loyx.paxos.protocol.*;
import cn.loyx.paxos.protocol.load.*;
import lombok.extern.log4j.Log4j;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

@Log4j
public class Proposer {

    enum ProposerState {
        IDLE, // start status of this Paxos process
        WAIT_PREPARE_RESP, // wait for prepare response
        WAIT_ACCEPT_RESP, // wait for accept response
        FINISH, // finish this Paxos process
    }
    private final Configuration conf;
    private final BlockingQueue<PaxosPacket> sendQueue;
    private PaxosValue proposeValue;
    private PaxosValue confirmedValue;
    private ProposerState state;

    private final Set<Integer> prepareOk;
    private final Set<Integer> acceptOk;
    private ProposalNo currentNo;
    private ProposalNo acceptorAcceptedNo;
    private PaxosValue acceptorAcceptedValue;

    public Proposer(Configuration conf, BlockingQueue<PaxosPacket> sendQueue) {
        this.conf = conf;
        this.sendQueue = sendQueue;
        this.proposeValue = null;
        this.prepareOk = new HashSet<>();
        this.acceptOk = new HashSet<>();
        this.state = ProposerState.IDLE;
        this.acceptorAcceptedValue = null;
        this.acceptorAcceptedNo = ProposalNo.empty();
    }

    public void handlePacket(PaxosPacket packet) throws InterruptedException {
        PaxosPacket handledResult;
        switch (packet.getType()){
            case PROPOSE_PACKET:
                handledResult = propose(packet);
                break;
            case PREPARE_RESPONSE_PACKET:
                handledResult = onPrepareResponse(packet);
                break;
            case ACCEPT_RESPONSE_PACKET:
                handledResult = onAcceptResponse(packet);
                break;
            default:
                handledResult = null;
        }
        if (handledResult != null) {
            sendQueue.put(handledResult);
        }
    }

    private PaxosPacket propose(PaxosPacket packet){
        this.proposeValue = ((ProposeLoad)packet.getLoad()).getValue();
        return prepare();
    }

    private PaxosPacket prepare(){
        this.state = ProposerState.WAIT_PREPARE_RESP;
        initialStatus();
        PrepareLoad newNo = PrepareLoad.newNo();
        currentNo = newNo;
        PaxosPacket preparePacket = new PaxosPacket(
                PacketTarget.ACCEPTOR,
                conf.getIdList(),
                conf.getId(),
                PacketType.PREPARE_PACKET,
                newNo
        );
        log.info(String.format("Proposer(%d) send a prepare packet: %s", conf.getId(), preparePacket));
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                /*
                After a long time and the proposer still does not get a response (prepare response or accept response),
                then the proposer will try to prepare again. In this implementation, acceptors send reject response,
                so the proposer can quickly know when to send the preparePacket again (not yet implemented).
                Only network problems can prevent the proposer from receiving a response. We use ProposerState to
                indicate the state that the Paxos process executes to. Only proposers in the WAIT_PREPARE_RESP state
                and WAIT_ACCEPT_RESP state can attempt to resend the preparePacket. Note that after the proposer
                resends a preparePacket, it will switch to the WAIT_PREPARE_RESP state. At this point, it may receive
                two types of response packets that have been delayed due to network problems. For accept response packet,
                the proponent will simply ignore it. For the prepare response packet, the proposer will treat this
                packet as a response packet for this request. This is not a problem because we assume that the
                PaxosValue cannot be changed during a Paxos process. The only drawback is that the response does not
                reflect the status of the acceptor in a timely manner. So the proposer may send useless preparation
                packets several times, but it does not break the algorithmic flow.
                 */
                try {
//                    log.debug("try to prepare again");
                    if (state == ProposerState.WAIT_PREPARE_RESP || state == ProposerState.WAIT_ACCEPT_RESP) {
                        sendQueue.put(prepare());
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, conf.getTimeout());
        return preparePacket;
    }

    private void initialStatus() {
        this.prepareOk.clear();
        this.acceptOk.clear();
        this.acceptorAcceptedNo = ProposalNo.empty();
        this.acceptorAcceptedValue = null;
    }

    private PaxosPacket onPrepareResponse(PaxosPacket packet){
        if (this.state != ProposerState.WAIT_PREPARE_RESP) {
            log.info(String.format("In %s state, not handle prepare response: %s", state, packet));
            return null;
        }
        log.info(String.format("Proposer(%d) handle a prepare response: %s", conf.getId(), packet));
        PrepareResponseLoad resp = (PrepareResponseLoad) packet.getLoad();
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
        this.state = ProposerState.WAIT_ACCEPT_RESP;
        confirmedValue = acceptValue;
        PaxosPacket acceptPacket = new PaxosPacket(
                PacketTarget.ACCEPTOR,
                conf.getIdList(),
                conf.getId(),
                PacketType.ACCEPT_PACKET,
                AcceptLoad.of(currentNo, acceptValue)
        );
        log.info(String.format("Proposer(%d) send a accept packet: %s",conf.getId(), acceptPacket));
        return acceptPacket;
    }

    private PaxosPacket onAcceptResponse(PaxosPacket packet){
        if (this.state != ProposerState.WAIT_ACCEPT_RESP){
            log.info(String.format("In %s state, not handle accept response: %s", state, packet));
            return null;
        }
        AcceptResponseLoad load = (AcceptResponseLoad) packet.getLoad();
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
        this.state = ProposerState.FINISH;
        String result = String.format("propose(%d) value %s, %s all machine accept %s",
                conf.getId(),
                proposeValue,
                proposeValue.equals(confirmedValue) ? "and" : "but",
                confirmedValue);
        log.info(result);
        state = ProposerState.IDLE;
    }

}
