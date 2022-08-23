package cn.loyx.paxossim.sim;

import cn.loyx.paxos.Paxos;
import cn.loyx.paxos.comm.SocketCommunicator;
import cn.loyx.paxos.conf.Configuration;
import cn.loyx.paxos.conf.NodeInfo;
import cn.loyx.paxos.impl.naive.NaivePaxos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulator {
    private final Map<Integer, Paxos> paxosSites = new HashMap<>();
    private final Map<Integer, ControllableCommunicator> comm = new HashMap<>();

    private final SimConfig config;

    public Simulator(SimConfig config){
        List<NodeInfo> nodeList = config.getNodeList();
        this.config = config;
        for (int id = 0, nodeListSize = nodeList.size(); id < nodeListSize; id++) {
            NodeInfo nodeInfo = nodeList.get(id);
            Configuration nodeConfig = new Configuration(nodeList, id, nodeInfo.getTimeout(), "");
            int finalId = id;
            NaivePaxos paxos = new NaivePaxos(nodeConfig, port -> {
                ControllableCommunicator controllableCommunicator = new ControllableCommunicator(new SocketCommunicator(port));
                comm.put(finalId, controllableCommunicator);
                return controllableCommunicator;
            });
            paxosSites.put(id, paxos);
        }
    }
    public Simulator(String configFile){
        this(SimConfig.fromFile(configFile));
    }

    public SimConfig getConfig() {
        return config;
    }
    public void start(){
        for (Paxos paxos : paxosSites.values()) {
            paxos.run();
        }
    }

    public Paxos getPaxos(int id){
        return paxosSites.get(id);
    }
}
