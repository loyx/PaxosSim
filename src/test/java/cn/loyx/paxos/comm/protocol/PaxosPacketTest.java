package cn.loyx.paxos.comm.protocol;

import cn.loyx.paxos.PaxosValue;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PaxosPacketTest {
    @Test
    void TestSerializable() throws IOException, ClassNotFoundException {
        PaxosPacketLoad[] loads = {
                new PrepareNo(),
                PrepareResponse.reject()
        };
        for (PaxosPacketLoad load : loads) {
            PaxosPacket paxosPacket = new PaxosPacket(PacketTarget.PROPOSER, Arrays.asList(1,2,3),1, PacketType.PREPARE_PACKET, load);
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

}