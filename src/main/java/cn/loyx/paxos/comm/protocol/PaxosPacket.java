package cn.loyx.paxos.comm.protocol;

import lombok.Data;

import cn.loyx.paxos.PaxosValue;

import java.io.Serializable;

@Data
public class PaxosPacket implements Serializable {
    private final PacketTarget target;
    private final Integer srcId;
    private final PacketType type;
    private final PaxosValue value;
}
