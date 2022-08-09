package cn.loyx.paxos;

public interface Paxos {
    public void submit(PaxosValue value, StateMachineContext context);
    public void run();
}
