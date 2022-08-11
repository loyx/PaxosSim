package cn.loyx.paxos.comm;

import cn.loyx.paxos.comm.protocol.PaxosPacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
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
    private final ExecutorService service;
    public SocketCommunicator(int port){
        this.port = port;
        this.receivedPackets = new LinkedBlockingQueue<>();
        this.needSendPackets = new LinkedBlockingQueue<>();
        this.service = Executors.newFixedThreadPool(50);

        // receive thread
        new Thread(()->{
            try(ServerSocket serverSocket = new ServerSocket(port)){
                while (true){
                    Socket accept = serverSocket.accept();
                    // todo decode packet
                    service.submit(this::decode);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }, "receive thread").start();

        // send thread
        new Thread(()->{
            try {
                SocketPacket packet = needSendPackets.take();
                try(Socket socket = new Socket(packet.ip, packet.port)){
                    // todo send packet
                }catch (IOException e){
                    throw new RuntimeException(e);
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, "seed thread").start();

    }

    private PaxosPacket decode(){
        return null;
    }
    @Override
    public void send(String ip, int port, PaxosPacket paxosPacket) {
        log.info("send a packet: " + paxosPacket);
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
