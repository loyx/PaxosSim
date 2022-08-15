package cn.loyx.paxos.comm;

@FunctionalInterface
public interface CommunicatorFactory {
    Communicator getCommunicator(int port);
}
