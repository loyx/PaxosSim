package cn.loyx.paxos.impl.naive;

import cn.loyx.paxos.PaxosValue;
import cn.loyx.paxos.ProposalNo;
import cn.loyx.paxos.comm.protocol.PaxosPacket;
import lombok.extern.log4j.Log4j;

import java.util.Optional;

@Log4j
public class Acceptor {

    private ProposalNo proposalNo;
    private PaxosValue value;
    private ProposalNo acceptedProposalNo;

    public Optional<PaxosPacket> handlePacket(PaxosPacket packet){
        log.debug("Acceptor: " + packet);
        switch (packet.getType()){
            case PREPARE_PACKET:
                break;
            case ACCEPT_PACKET:
                break;
            default:
                return Optional.empty();
        }
        return Optional.empty();
    }

    void onPrepare(){

    }

    void onAccept(){

    }

}
