package cn.loyx.paxos.protocol;

public enum PacketType {
    // proposer handled packet type
    PROPOSE_PACKET,
    PREPARE_RESPONSE_PACKET,
    ACCEPT_RESPONSE_PACKET,

    // acceptor handled packet type
    PREPARE_PACKET,
    ACCEPT_PACKET,

}
