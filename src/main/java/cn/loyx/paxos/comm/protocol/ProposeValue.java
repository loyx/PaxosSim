package cn.loyx.paxos.comm.protocol;

import cn.loyx.paxos.PaxosValue;
import lombok.Data;

@Data
public class ProposeValue implements PaxosPacketLoad{
    private final PaxosValue value;

    public static ProposeValue of(PaxosValue value){
        return new ProposeValue(value);
    }
}
