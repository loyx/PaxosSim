package cn.loyx.paxos.protocol.load;

import cn.loyx.paxos.PaxosValue;
import cn.loyx.paxos.ProposalNo;
import cn.loyx.paxos.protocol.PaxosPacketLoad;
import lombok.Data;

@Data
public class PrepareResponseLoad implements PaxosPacketLoad {
    private final boolean prepareOk;
    private final ProposalNo acceptedNo;
    private final PaxosValue acceptedValue;

    public static PrepareResponseLoad ok(ProposalNo no, PaxosValue value){
        return new PrepareResponseLoad(true, no, value);
    }

    public static PrepareResponseLoad reject(){
        return new PrepareResponseLoad(false, null, null);
    }
}
