package cn.loyx.paxos.conf;

import lombok.Data;

import java.util.List;

@Data
public class Configuration {
    private List<NodeInfo> nodeList;
    private int id;
    private int timeout;
    private String dataPath;
}
