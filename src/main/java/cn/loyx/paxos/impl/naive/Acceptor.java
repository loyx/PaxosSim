package cn.loyx.paxos.impl.naive;

import cn.loyx.paxos.PaxosValue;
import cn.loyx.paxos.ProposalNo;
import cn.loyx.paxos.comm.protocol.*;
import cn.loyx.paxos.conf.Configuration;
import lombok.extern.log4j.Log4j;

import java.util.*;
import java.util.concurrent.BlockingQueue;

@Log4j
public class Acceptor {

    private final Configuration conf;
    private final BlockingQueue<PaxosPacket> sendQueue;

    private ProposalNo responseNo;
    private PaxosValue acceptedValue;
    private ProposalNo acceptedNo;
    {
        responseNo = ProposalNo.empty();
        acceptedNo = ProposalNo.empty();
        acceptedValue = null;
    }

    public Acceptor(Configuration conf, BlockingQueue<PaxosPacket> sendQueue) {
        this.conf = conf;
        this.sendQueue = sendQueue;
    }

    public void handlePacket(PaxosPacket packet) throws InterruptedException {
        PaxosPacket handleResult;
        switch (packet.getType()){
            case PREPARE_PACKET:
                handleResult = onPrepare(packet);
                break;
            case ACCEPT_PACKET:
                handleResult = onAccept(packet);
                break;
            default:
                handleResult = null;
        }
        if (handleResult != null){
            sendQueue.put(handleResult);
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
