package cn.loyx.paxos.protocol.load;

import cn.loyx.paxos.PaxosValue;
import cn.loyx.paxos.protocol.PaxosPacketLoad;
import lombok.Data;

@Data
public class ProposeLoad implements PaxosPacketLoad {
    private final PaxosValue value;

    public static ProposeLoad of(PaxosValue value){
        return new ProposeLoad(value);
    }
}
