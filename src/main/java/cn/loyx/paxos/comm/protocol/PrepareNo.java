package cn.loyx.paxos.comm.protocol;

import cn.loyx.paxos.ProposalNo;

public class PrepareNo extends ProposalNo implements PaxosPacketLoad{
    public static PrepareNo newNo(){
        return new PrepareNo();
    }
}
