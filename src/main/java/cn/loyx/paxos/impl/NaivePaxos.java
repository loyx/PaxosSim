package cn.loyx.paxos.impl;

import cn.loyx.paxos.Paxos;
import cn.loyx.paxos.PaxosValue;
import cn.loyx.paxos.StateMachineContext;

public class NaivePaxos implements Paxos {

    @Override
    public PaxosNodeStatus submit(PaxosValue value, StateMachineContext context) {
        return null;
    }

    @Override
    public PaxosNodeStatus run() {
        return null;
    }
}
