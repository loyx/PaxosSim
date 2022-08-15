package cn.loyx.paxos.protocol.load;

import cn.loyx.paxos.ProposalNo;
import cn.loyx.paxos.protocol.PaxosPacketLoad;

public class PrepareLoad extends ProposalNo implements PaxosPacketLoad {
    public static PrepareLoad newNo(){
        return new PrepareLoad();
    }
}
