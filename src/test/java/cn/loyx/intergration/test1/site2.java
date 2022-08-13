package cn.loyx.intergration.test1;

import cn.loyx.paxos.PaxosValue;
import cn.loyx.paxos.impl.naive.NaivePaxos;
import org.apache.log4j.BasicConfigurator;

public class site2 {
    public static void main(String[] args) throws InterruptedException {
        BasicConfigurator.configure();
        NaivePaxos naivePaxos = new NaivePaxos("src/test/resources/config2.json");
        naivePaxos.run();
//        Thread.sleep(4000);
//        naivePaxos.propose(new PaxosValue("test value"), null);
    }
}
