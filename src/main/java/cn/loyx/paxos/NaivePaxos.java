package cn.loyx.paxos;

import cn.loyx.paxos.comm.Communicator;
import cn.loyx.paxos.comm.protocol.Packet;
import cn.loyx.paxos.conf.Configuration;
import cn.loyx.paxos.conf.NodeInfo;
import com.google.gson.Gson;
import lombok.extern.java.Log;

import javax.management.MBeanAttributeInfo;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

@Log
public class NaivePaxos implements Paxos {
    private final Configuration conf;
    private final NodeInfo selfInfo;

    private Acceptor acceptor;
    private Proposer proposer;
    private Communicator commServer;

    private BlockingQueue<Packet> receiveQueue;
    private BlockingQueue<Packet> sendQueue;

    public NaivePaxos(String configFilePath){
        Gson gson = new Gson();
        Configuration configuration;
        try {
            configuration = gson.fromJson(Files.newBufferedReader(Path.of(configFilePath)), Configuration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.conf = configuration;
        this.selfInfo = configuration.getNodeList().get(configuration.getId());
    }

    public NaivePaxos(Configuration configuration){
        this.conf = configuration;
        this.selfInfo = configuration.getNodeList().get(configuration.getId());
    }

    @Override
    public PaxosNodeStatus submit(PaxosValue value, StateMachineContext context) {
        return null;
    }

    @Override
    public PaxosNodeStatus run() {
        new Thread(() -> {
            Packet receive = commServer.receive();
            try {
                receiveQueue.put(receive);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(()->{
            Packet take = null;
            try {
                take = sendQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            commServer.send("", 0, take);
        });

        while (true){
            Packet receive = this.commServer.receive();
            switch (receive.getTarget()) {
                case ACCEPTOR -> acceptor.handlePacket(receive);
                case PROPOSER -> proposer.handlePacket(receive);
                case LEARNER -> System.out.println("no implement yet");
            }
        }
    }
}
