package cn.loyx.paxos.comm.protocol;

public class AcceptResponse implements PaxosPacketLoad{

    private final boolean acceptOk;

    public AcceptResponse(boolean acceptOk) {
        this.acceptOk = acceptOk;
    }

    public static AcceptResponse ok(){
        return new AcceptResponse(true);
    }

    public static AcceptResponse reject(){
        return new AcceptResponse(false);
    }

    public boolean isAcceptOk() {
        return acceptOk;
    }

    @Override
    public String toString() {
        return "AcceptResponse{" +
                "acceptOk=" + acceptOk +
                '}';
    }
}
