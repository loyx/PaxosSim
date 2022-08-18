package cn.loyx.paxossim.sim;

import cn.loyx.paxos.comm.Communicator;
import cn.loyx.paxos.comm.SocketCommunicator;
import cn.loyx.paxos.protocol.PaxosPacket;
import cn.loyx.paxossim.sim.util.ChangeableDelayQueue;
import lombok.extern.log4j.Log4j;

import java.util.HashMap;
import java.util.Map;

@Log4j
public class ControllableCommunicator implements Communicator {


    private final SocketCommunicator realCommunicator;
    private final ChangeableDelayQueue<DelayedPacket> CDQueue = new ChangeableDelayQueue<>();
    private final Map<Long, DelayedPacket> packetsMap = new HashMap<>();

    public ControllableCommunicator(SocketCommunicator communicator){
        this.realCommunicator = communicator;

        new Thread(()->{
            try {
                while (true){
                    DelayedPacket take = CDQueue.take();
//                    log.info("CComm send a packet: " + take.getPacket());
                    communicator.send(take.getIp(), take.getPort(), take.getPacket());
                    packetsMap.remove(take.getId());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "real-send-thread").start();
    }

    @Override
    public void send(String ip, int port, PaxosPacket paxosPacket) {
//        log.info("CComm get a packet: " + paxosPacket);
        DelayedPacket packet = new DelayedPacket(1, ip, port, paxosPacket);
        CDQueue.put(packet);
        packetsMap.put(packet.getId(), packet);
    }

    @Override
    public PaxosPacket receive() {
        PaxosPacket receive = realCommunicator.receive();
//        log.info("CComm receive a packet: " + receive);
        return receive;
    }


    public void delayPacket(long packetId, long offset){
        DelayedPacket delayedPacket = packetsMap.get(packetId);
        CDQueue.changeDelay(delayedPacket, offset);
    }

    public void lossPacket(long packetId){
        DelayedPacket delayedPacket = packetsMap.get(packetId);
        CDQueue.cancelTask(delayedPacket);
    }


}
