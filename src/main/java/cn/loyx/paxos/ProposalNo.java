package cn.loyx.paxos;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ProposalNo implements Serializable {
    private static final long serialVersionUID = -7400927432205312398L;
    private final int proposerId;
    private final long no;

    public ProposalNo(int proposerId) {
        this.proposerId = proposerId;
        this.no = new Date().getTime();
    }

    public int getProposerId() {
        return proposerId;
    }

    public long getNo() {
        return no;
    }

    public boolean greeter(ProposalNo proposalNo){
        return no > proposalNo.no;
    }

    @Override
    public String toString() {
        return "ProposalNo{" +
                "proposerId=" + proposerId +
                ", no=" + no +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProposalNo that = (ProposalNo) o;
        return proposerId == that.proposerId && no == that.no;
    }

    @Override
    public int hashCode() {
        return Objects.hash(proposerId, no);
    }
}
