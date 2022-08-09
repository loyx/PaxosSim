package cn.loyx.paxos.comm.protocol;

import lombok.Data;

@Data
public class Packet {
    public enum PacketTarget{
        ACCEPTOR,
        PROPOSER,
        LEARNER,
    }

    PacketTarget target;

}
