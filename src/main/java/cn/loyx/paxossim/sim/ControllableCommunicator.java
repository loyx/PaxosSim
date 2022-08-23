package cn.loyx.paxossim.sim;

import cn.loyx.paxos.comm.Communicator;
import cn.loyx.paxos.comm.SocketCommunicator;
import cn.loyx.paxos.protocol.PaxosPacket;
import cn.loyx.paxossim.sim.util.ChangeableDelayQueue;
import lombok.extern.log4j.Log4j;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Log4j
public class ControllableCommunicator implements Communicator {


    private final SocketCommunicator realCommunicator;
    private final ChangeableDelayQueue<DelayedPacket> CDQueue = new ChangeableDelayQueue<>();
    private final Map<Long, DelayedPacket> packetsMap = new HashMap<>();
    private final List<PacketListener> listeners = new LinkedList<>();

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

    public void addPacketListener(PacketListener listener){
        listeners.add(listener);
    }

    public void removePacketListener(PacketListener listener){
        listeners.remove(listener);
    }

    @Override
    public void send(String ip, int port, PaxosPacket paxosPacket) {
        DelayedPacket packet = new DelayedPacket(10, ip, port, paxosPacket);
        CDQueue.put(packet);
        packetsMap.put(packet.getId(), packet);
        listeners.forEach(listener -> listener.onSendPacket(ip, port, paxosPacket));
    }

    @Override
    public PaxosPacket receive() {
        PaxosPacket receive = realCommunicator.receive();
        listeners.forEach(listener -> listener.onReceivePacket(receive));
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
