package cn.loyx.paxos.comm;

import cn.loyx.paxos.protocol.PaxosPacket;

public interface Communicator {
    void send(String ip, int port, PaxosPacket paxosPacket);
    PaxosPacket receive();
}
