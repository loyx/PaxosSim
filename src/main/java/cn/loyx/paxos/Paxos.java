package cn.loyx.paxos;

public interface Paxos {
    public void propose(PaxosValue value, StateMachineContext context);
    public void run();
}
