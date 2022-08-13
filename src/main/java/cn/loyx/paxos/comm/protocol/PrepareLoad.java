package cn.loyx.paxos.comm.protocol;

import java.util.UUID;

public class PrepareLoad implements PaxosPacketLoad{
    private final UUID billNo;
    public PrepareLoad(){
        billNo = UUID.randomUUID();
    }

    public UUID getBillNo() {
        return billNo;
    }

    @Override
    public String toString() {
        return "PrepareLoad{" +
                "billNo=" + billNo +
                '}';
    }
}
