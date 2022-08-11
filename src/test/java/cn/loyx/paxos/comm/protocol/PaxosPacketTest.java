package cn.loyx.paxos.comm.protocol;

import cn.loyx.paxos.PaxosValue;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class PaxosPacketTest {

    @Data
    static class ValueLoad implements PaxosPacketLoad{
        private final PaxosValue value;
    }
    @Test
    void TestSerializable() throws IOException, ClassNotFoundException {
        ValueLoad valueLoad = new ValueLoad(new PaxosValue("123"));
        PaxosPacket paxosPacket = new PaxosPacket(PacketTarget.PROPOSER, 1, PacketType.PROPOSE_PACKET, valueLoad);
        System.out.println("before: " + paxosPacket);
        File file = File.createTempFile("testSerial", ".tmp");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(paxosPacket);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        PaxosPacket packet = (PaxosPacket) ois.readObject();
        System.out.println("after: " + packet);
        assertEquals(packet, paxosPacket);
    }

}