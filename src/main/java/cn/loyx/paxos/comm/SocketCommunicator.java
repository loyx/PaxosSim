package cn.loyx.paxos.comm;

import cn.loyx.paxos.comm.protocol.PaxosPacket;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

@Log4j
public class SocketCommunicator implements Communicator {

    @AllArgsConstructor
    static class SocketPacket{
        private final String ip;
        private final int port;
        private final PaxosPacket data;
    }
    private final BlockingQueue<PaxosPacket> receivedPackets;
    private final BlockingQueue<SocketPacket> needSendPackets;
    private final int port;
    private final ExecutorService executors;
    public SocketCommunicator(int port){
        this.port = port;
        this.receivedPackets = new LinkedBlockingQueue<>();
        this.needSendPackets = new LinkedBlockingQueue<>();
        this.executors = Executors.newFixedThreadPool(10);

        // receive thread
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (true) {
                    Socket accept = serverSocket.accept();
                    executors.submit(() -> {
                        try {
                            ObjectInputStream ois = new ObjectInputStream(accept.getInputStream());
                            receivedPackets.put((PaxosPacket) ois.readObject());
                        } catch (IOException | ClassNotFoundException | InterruptedException e1) {
                            throw new RuntimeException(e1);
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }, "comm-receive-thread").start();

        // send thread
        new Thread(() -> {
            while (true){
                try {
                    SocketPacket packet = needSendPackets.take();
                    try (Socket socket = new Socket(packet.ip, packet.port)) {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(packet.data);
                        oos.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }, "comm-send-thread").start();

    }

    @Override
    public void send(String ip, int port, PaxosPacket paxosPacket) {
        log.info(String.format("send [%s, %s] a packet %s", ip, port, paxosPacket));
        try {
            needSendPackets.put(new SocketPacket(ip, port, paxosPacket));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaxosPacket receive() {
        PaxosPacket take;
        try {
            take = receivedPackets.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("receive a packet: " + take);
        return take;
    }
}
