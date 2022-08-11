package cn.loyx.paxos.comm.protocol;

import cn.loyx.paxos.PaxosValue;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class PaxosPacketTest {

    @Test
    void TestSerializable() throws IOException, ClassNotFoundException {
        PaxosPacket paxosPacket = new PaxosPacket(PacketTarget.PROPOSER, 1, PacketType.PROPOSE_PACKET, new PaxosValue("123"));
        System.out.println("before: " + paxosPacket);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/test/resources/testSerial"));
        oos.writeObject(paxosPacket);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/test/resources/testSerial"));
        PaxosPacket packet = (PaxosPacket) ois.readObject();
        System.out.println("after: " + packet);
        assertEquals(packet, paxosPacket);
    }

}