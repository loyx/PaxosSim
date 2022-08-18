package cn.loyx.paxossim.sim;

import cn.loyx.paxos.Paxos;
import cn.loyx.paxos.PaxosValue;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulatorTest {

    public static void main(String[] args) throws InterruptedException {
        BasicConfigurator.configure();
        Simulator simulator = new Simulator("src/test/resources/simConfig.json");
        simulator.start();
        Paxos paxos = simulator.getPaxos(0);
        paxos.propose(new PaxosValue("simulate"), null);
        Thread.sleep(100_000);
    }
}