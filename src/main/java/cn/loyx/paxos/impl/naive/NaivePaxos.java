package cn.loyx.paxos.impl.naive;

import cn.loyx.paxos.Paxos;
import cn.loyx.paxos.PaxosValue;
import cn.loyx.paxos.StateMachineContext;
import cn.loyx.paxos.comm.Communicator;
import cn.loyx.paxos.comm.SocketCommunicator;
import cn.loyx.paxos.conf.Configuration;
import cn.loyx.paxos.conf.NodeInfo;
import cn.loyx.paxos.protocol.PacketTarget;
import cn.loyx.paxos.protocol.PacketType;
import cn.loyx.paxos.protocol.PaxosPacket;
import cn.loyx.paxos.protocol.load.ProposeLoad;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Log4j
public class NaivePaxos implements Paxos {
    private final Configuration conf;
    private final NodeInfo selfInfo;

    private final Acceptor acceptor;
    private final Proposer proposer;
    private final Communicator commServer;

    private final BlockingQueue<PaxosPacket> processQueue;
    private final BlockingQueue<PaxosPacket> sendQueue;
    {
        processQueue = new LinkedBlockingQueue<>();
        sendQueue = new LinkedBlockingQueue<>();
    }

    public NaivePaxos(String configFilePath){
        Gson gson = new Gson();
        Configuration configuration;
        try {
            configuration = gson.fromJson(Files.newBufferedReader(Path.of(configFilePath)), Configuration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.conf = configuration;
        this.selfInfo = configuration.getSelfInfo();
        this.commServer = new SocketCommunicator(selfInfo.getPort());
        this.acceptor = new Acceptor(conf, sendQueue);
        this.proposer = new Proposer(conf, sendQueue);
    }

    public NaivePaxos(Configuration configuration){
        this.conf = configuration;
        this.selfInfo = configuration.getSelfInfo();
        this.commServer = new SocketCommunicator(selfInfo.getPort());
        this.acceptor = new Acceptor(conf, sendQueue);
        this.proposer = new Proposer(conf, sendQueue);
    }

    @Override
    public void propose(PaxosValue value, StateMachineContext context) {
        // todo blocking method
        PaxosPacket packet = new PaxosPacket(
                PacketTarget.PROPOSER,
                null, null,
                PacketType.PROPOSE_PACKET,
                ProposeLoad.of(value)
        );
        log.debug("submit packet " + packet);
        try {
            processQueue.put(packet);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        new Thread(() -> {
            while(true){
                PaxosPacket receive = commServer.receive();
                try {
                    processQueue.put(receive);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "receive-thread").start();

        new Thread(()->{
            while (true){
                PaxosPacket packet;
                try {
                    packet = sendQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (Integer dstId : packet.getDstIds()) {
                    NodeInfo nodeInfo = conf.getNodeList().get(dstId);
                    commServer.send(nodeInfo.getIp(), nodeInfo.getPort(), packet);
                }
            }
        }, "send-thread").start();

        new Thread(()->{
            while (true){
                try {
                    PaxosPacket packet = processQueue.take();
                    switch (packet.getTarget()) {
                        case ACCEPTOR:
                            acceptor.handlePacket(packet);
                            break;
                        case PROPOSER:
                            proposer.handlePacket(packet);
                            break;
                        case LEARNER:
                            System.out.println("no implement yet");
                        default:
                            log.info("Unknown packet: " + packet);
                    }
                } catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
            }
        }, "process-thread").start();

        log.info(String.format("%s Site run on [ip: %s, port: %s]",
                this.getClass().getSimpleName(),
                selfInfo.getIp(),
                selfInfo.getPort()
                ));
    }
}
