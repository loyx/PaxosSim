package cn.loyx.paxos.protocol.load;

import cn.loyx.paxos.protocol.PaxosPacketLoad;

public class AcceptResponseLoad implements PaxosPacketLoad {

    private final boolean acceptOk;

    public AcceptResponseLoad(boolean acceptOk) {
        this.acceptOk = acceptOk;
    }

    public static AcceptResponseLoad ok(){
        return new AcceptResponseLoad(true);
    }

    public static AcceptResponseLoad reject(){
        return new AcceptResponseLoad(false);
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
