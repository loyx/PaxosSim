package cn.loyx.paxos.comm;

import cn.loyx.paxos.comm.protocol.Packet;

public interface Communicator {
    void send(String ip, int port, Packet packet);
    Packet receive();
}
