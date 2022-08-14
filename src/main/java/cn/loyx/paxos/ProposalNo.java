package cn.loyx.paxos;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ProposalNo implements Serializable {
    private static final long serialVersionUID = -5049415909460704153L;
    private final long no;

    public ProposalNo() {
        this.no = new Date().getTime();
    }

    private ProposalNo(int no){
        this.no = no;
    }

    public static ProposalNo newNo(){
        return new ProposalNo();
    }

    public static ProposalNo empty(){
        return new ProposalNo(0);
    }

    public long getNo() {
        return no;
    }

    public boolean greeter(ProposalNo proposalNo){
        return no > proposalNo.no;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProposalNo that = (ProposalNo) o;
        return no == that.no;
    }

    @Override
    public int hashCode() {
        return Objects.hash(no);
    }

    @Override
    public String toString() {
        return "ProposalNo{" +
                "no=" + no +
                '}';
    }
}
