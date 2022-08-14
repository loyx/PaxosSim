package cn.loyx.paxos.comm.protocol;

import cn.loyx.paxos.ProposalNo;

public class PrepareLoad extends ProposalNo implements PaxosPacketLoad{
    public PrepareLoad(int proposerId) {
        super(proposerId);
    }

    public static PrepareLoad of(int proposerId){
        return new PrepareLoad(proposerId);
    }
}
