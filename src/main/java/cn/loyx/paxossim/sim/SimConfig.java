package cn.loyx.paxossim.sim;

import cn.loyx.paxos.conf.NodeInfo;
import com.google.gson.Gson;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Data
public class SimConfig {
    private final List<NodeInfo> nodeList;
    public static SimConfig fromFile(String filePath){
        try {
            return new Gson().fromJson(Files.newBufferedReader(Path.of(filePath)), SimConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public int getIdFromNetAddr(String ip, int port){
        for (NodeInfo nodeInfo : nodeList) {
            if (nodeInfo.getIp().equals(ip) && nodeInfo.getPort() == port){
                return nodeInfo.getId();
            }
        }
        throw new RuntimeException("unknown address: " + ip + ":" + port);
    }
}
