package cn.loyx.paxos.comm.protocol;

import lombok.Data;

import cn.loyx.paxos.PaxosValue;

@Data
public class PaxosPacket {


    private final PacketTarget target;

    private final PaxosValue value;

}
