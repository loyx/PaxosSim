package cn.loyx.paxos.impl.naive;

import cn.loyx.paxos.PaxosValue;
import cn.loyx.paxos.ProposalNo;
import cn.loyx.paxos.comm.protocol.*;
import cn.loyx.paxos.conf.Configuration;
import lombok.extern.log4j.Log4j;

import java.util.*;

@Log4j
public class Acceptor {

    Configuration conf;

    private ProposalNo responseNo;
    private PaxosValue acceptedValue;
    private ProposalNo acceptedNo;
    {
        responseNo = ProposalNo.empty();
        acceptedNo = ProposalNo.empty();
        acceptedValue = null;
    }

    public Acceptor(Configuration conf) {
        this.conf = conf;
    }

    public Optional<PaxosPacket> handlePacket(PaxosPacket packet){
        switch (packet.getType()){
            case PREPARE_PACKET:
                return Optional.of(onPrepare(packet));
            case ACCEPT_PACKET:
                break;
            default:
                return Optional.empty();
        }
        return Optional.empty();
    }

    PaxosPacket onPrepare(PaxosPacket packet){
        log.info("Acceptor get a prepare packet: " + packet);
        PrepareNo prepareNo = (PrepareNo) packet.getLoad();
        PrepareResponse response = null;
        if (prepareNo.greeter(responseNo)){
            responseNo = prepareNo;
            response = PrepareResponse.ok(acceptedNo, acceptedValue);
        } else {
            response = PrepareResponse.reject();
        }

        return new PaxosPacket(
                PacketTarget.PROPOSER,
                List.of(packet.getSrcId()),
                conf.getId(),
                PacketType.PREPARE_RESPONSE_PACKET,
                response
        );
    }

    void onAccept(){

    }

}
