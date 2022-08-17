package cn.loyx.paxossim.simluator;

import cn.loyx.paxos.protocol.PaxosPacket;
import cn.loyx.paxossim.simluator.util.ChangeableDelayed;

import java.util.Objects;

class DelayedPacket implements ChangeableDelayed {
    public static long packetsCnt = 0;
    private final long id;
    private long delayTime;
    private final String ip;
    private final int port;
    private final PaxosPacket packet;

    public DelayedPacket(long delayTime, String ip, int port, PaxosPacket packet) {
        this.delayTime = delayTime;
        this.ip = ip;
        this.port = port;
        this.packet = packet;
        id = packetsCnt;
        DelayedPacket.packetsCnt++;
    }

    @Override
    public long getDelay() {
        return delayTime;
    }

    @Override
    public void changeDelay(long offset) {
        delayTime += offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DelayedPacket that = (DelayedPacket) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public long getId() {
        return id;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public PaxosPacket getPacket() {
        return packet;
    }
}
