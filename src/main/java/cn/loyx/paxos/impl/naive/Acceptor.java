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
                return Optional.of(onAccept(packet));
            default:
                return Optional.empty();
        }
    }

    PaxosPacket onPrepare(PaxosPacket packet){
        log.info("Acceptor get a prepare packet: " + packet);
        PrepareNo prepareNo = (PrepareNo) packet.getLoad();
        PrepareResponse response;
        if (prepareNo.gt(responseNo)){
            responseNo = prepareNo;
            response = PrepareResponse.ok(acceptedNo, acceptedValue);
        } else {
            response = PrepareResponse.reject();
        }

        PaxosPacket responsePacket = new PaxosPacket(
                PacketTarget.PROPOSER,
                List.of(packet.getSrcId()),
                conf.getId(),
                PacketType.PREPARE_RESPONSE_PACKET,
                response
        );
        log.info("Acceptor response preparePacket: " + responsePacket);
        return responsePacket;
    }

    PaxosPacket onAccept(PaxosPacket packet){
        log.info("Acceptor get a accept packet: " + packet);
        AcceptLoad load = (AcceptLoad) packet.getLoad();
        AcceptResponse response;
        if (load.getProposalNo().ge(responseNo)){
            acceptedNo = load.getProposalNo();
            acceptedValue = load.getAcceptValue();
            response = AcceptResponse.ok();
        }else {
            response = AcceptResponse.reject();
        }
        PaxosPacket responsePacket = new PaxosPacket(
                PacketTarget.PROPOSER,
                List.of(packet.getSrcId()),
                conf.getId(),
                PacketType.ACCEPT_RESPONSE_PACKET,
                response
        );
        log.info("Acceptor response acceptPacket: " + responsePacket);
        return responsePacket;
    }

}
