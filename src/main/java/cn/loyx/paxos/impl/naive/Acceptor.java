package cn.loyx.paxos.impl.naive;

import cn.loyx.paxos.comm.protocol.PaxosPacket;
import lombok.extern.log4j.Log4j;

import java.util.Optional;

@Log4j
public class Acceptor {
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

    void prepareResponse(){

    }

    void onAccept(){

    }

    void acceptResponse(){

    }

}
