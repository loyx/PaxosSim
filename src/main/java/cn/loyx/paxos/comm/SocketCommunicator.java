package cn.loyx.paxos.comm;

import cn.loyx.paxos.comm.protocol.PaxosPacket;
import lombok.extern.log4j.Log4j;

@Log4j
public class SocketCommunicator implements Communicator {
    @Override
    public void send(PaxosPacket paxosPacket) {
        log.info("send a packet");
    }

    @Override
    public PaxosPacket receive() {
        log.info("receive a packet");
        return null;
    }
}
