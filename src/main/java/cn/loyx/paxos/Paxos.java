package cn.loyx.paxos;

public interface Paxos {
    enum PaxosNodeStatus {
        SUBMIT_SUCCESS,
        SUBMIT_FAIL,

        RUN_SUCCESS,
        RUN_FAIL
    }
    public PaxosNodeStatus submit(PaxosValue value, StateMachineContext context);
    public PaxosNodeStatus run();
}
