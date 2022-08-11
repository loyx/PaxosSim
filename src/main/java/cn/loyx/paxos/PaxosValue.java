package cn.loyx.paxos;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaxosValue implements Serializable {
    private static final long serialVersionUID = 417774183334258404L;
    private final String value;
}
