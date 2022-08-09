package cn.loyx.paxos.impl.naive;

import cn.loyx.paxos.comm.protocol.PaxosPacket;
import lombok.extern.log4j.Log4j;

import java.util.Optional;

@Log4j
public class Proposer {
    public Optional<PaxosPacket> handlePacket(PaxosPacket paxosPacket){
        log.debug("Proposer packet: " + paxosPacket);
        return Optional.empty();
    };
}
