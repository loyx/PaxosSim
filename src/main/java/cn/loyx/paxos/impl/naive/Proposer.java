package cn.loyx.paxos.impl.naive;

import cn.loyx.paxos.comm.protocol.PacketTarget;
import cn.loyx.paxos.comm.protocol.PacketType;
import cn.loyx.paxos.comm.protocol.PaxosPacket;
import cn.loyx.paxos.comm.protocol.PrepareLoad;
import lombok.extern.log4j.Log4j;

import java.util.Optional;

@Log4j
public class Proposer {
    public Optional<PaxosPacket> handlePacket(PaxosPacket packet){
        log.debug("Proposer get a packet: " + packet);
        switch (packet.getType()){
            case PROPOSE_PACKET:
                return Optional.of(prepare(packet));

            case PREPARE_RESPONSE_PACKET:
                break;
            case ACCEPT_RESPONSE_PACKET:
                break;
            default:
                return Optional.empty();
        }
        return Optional.empty();
    }

    private PaxosPacket prepare(PaxosPacket packet){
        PaxosPacket preparePacket = new PaxosPacket(
                PacketTarget.ACCEPTOR,
                packet.getDstIds(),
                packet.getSrcId(),
                PacketType.PROPOSE_PACKET,
                new PrepareLoad()
        );
        log.info("prepare packet: " + preparePacket);
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
