package cn.loyx.paxossim.simluator;

import cn.loyx.paxos.comm.Communicator;
import cn.loyx.paxos.comm.SocketCommunicator;
import cn.loyx.paxos.protocol.PaxosPacket;
import cn.loyx.paxossim.simluator.util.ChangeableDelayQueue;

import java.util.HashMap;

public class ControllableNetwork implements Communicator {


    private final SocketCommunicator realCommunicator;
    private final ChangeableDelayQueue<DelayedPacket> CDQueue = new ChangeableDelayQueue<>();
    private final HashMap<Long, DelayedPacket> packetsMap = new HashMap<>();

    public ControllableNetwork(SocketCommunicator communicator){
        this.realCommunicator = communicator;

        new Thread(()->{
            try {
                DelayedPacket take = CDQueue.take();
                communicator.send(take.getIp(), take.getPort(), take.getPacket());
                packetsMap.remove(take.getId());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "real-send-thread").start();
    }

    @Override
    public void send(String ip, int port, PaxosPacket paxosPacket) {
        DelayedPacket packet = new DelayedPacket(1, ip, port, paxosPacket);
        CDQueue.put(packet);
        packetsMap.put(packet.getId(), packet);
    }

    @Override
    public PaxosPacket receive() {
        return realCommunicator.receive();
    }



}
