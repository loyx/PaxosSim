package cn.loyx.paxos.comm;

import cn.loyx.paxos.comm.protocol.PaxosPacket;

public interface Communicator {
    void send(PaxosPacket paxosPacket);
    PaxosPacket receive();
}
