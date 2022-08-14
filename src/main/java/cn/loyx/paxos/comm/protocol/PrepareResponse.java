package cn.loyx.paxos.comm.protocol;

import cn.loyx.paxos.PaxosValue;
import cn.loyx.paxos.ProposalNo;
import lombok.Data;

@Data
public class PrepareResponse implements PaxosPacketLoad{
    private final boolean prepareOk;
    private final ProposalNo acceptedNo;
    private final PaxosValue acceptedValue;

    public static PrepareResponse ok(ProposalNo no, PaxosValue value){
        return new PrepareResponse(true, no, value);
    }

    public static PrepareResponse reject(){
        return new PrepareResponse(false, null, null);
    }
}
