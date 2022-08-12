package cn.loyx.paxos.conf;

import lombok.Data;

@Data
public class NodeInfo {
    private int id;
    private String ip;
    private int port;
}
