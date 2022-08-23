package cn.loyx.paxossim.sim;

import cn.loyx.paxos.protocol.PaxosPacket;

public interface PacketListener {
    default void onSendPacket(String ip, int port, PaxosPacket paxosPacket){}
    default void onReceivePacket(PaxosPacket receive){}
}
