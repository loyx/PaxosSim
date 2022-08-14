package cn.loyx.paxos.conf;

import java.util.List;
import java.util.stream.Collectors;

public class Configuration {
    private final List<NodeInfo> nodeList;
    private final int id;
    private final int timeout;
    private final String dataPath;

    private List<Integer> idList = null;

    public Configuration(List<NodeInfo> nodeList, int id, int timeout, String dataPath) {
        this.nodeList = nodeList;
        this.id = id;
        this.timeout = timeout;
        this.dataPath = dataPath;
    }

    public NodeInfo getSelfInfo(){
        return nodeList.get(id);
    }

    public List<Integer> getIdList() {
        if (idList == null){
            idList = nodeList.stream().map(NodeInfo::getId).collect(Collectors.toList());
        }
        return idList;
    }


    public List<NodeInfo> getNodeList() {
        return nodeList;
    }

    public int getId() {
        return id;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getDataPath() {
        return dataPath;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "nodeList=" + nodeList +
                ", id=" + id +
                ", timeout=" + timeout +
                ", dataPath='" + dataPath + '\'' +
                '}';
    }
}
