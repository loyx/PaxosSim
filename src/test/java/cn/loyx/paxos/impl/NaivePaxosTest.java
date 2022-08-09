package cn.loyx.paxos.impl;

import cn.loyx.paxos.PaxosValue;
import cn.loyx.paxos.impl.naive.NaivePaxos;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NaivePaxosTest {


    @Test
    void submit() {
        BasicConfigurator.configure();
        NaivePaxos naivePaxos = new NaivePaxos("src/test/resources/config.json");
        naivePaxos.run();

        for (int i = 0; i < 10; i++) {
            naivePaxos.submit(new PaxosValue(String.format("Packet(%d)",i)), null);
        }
    }
}