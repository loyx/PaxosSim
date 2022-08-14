package cn.loyx.paxos.comm.protocol;

import cn.loyx.paxos.PaxosValue;
import cn.loyx.paxos.ProposalNo;
import lombok.Data;

@Data
public class AcceptLoad implements PaxosPacketLoad{
    private final ProposalNo proposalNo;
    private final PaxosValue acceptValue;

    public static AcceptLoad of(ProposalNo proposalNo, PaxosValue value){
        return new AcceptLoad(proposalNo, value);
    }
}
