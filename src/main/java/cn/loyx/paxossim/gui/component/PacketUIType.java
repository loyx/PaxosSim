package cn.loyx.paxossim.gui.component;

import cn.loyx.paxos.protocol.PaxosPacket;
import cn.loyx.paxos.protocol.load.AcceptResponseLoad;
import cn.loyx.paxos.protocol.load.PrepareResponseLoad;

public enum PacketUIType {
    ACCEPT_OK,
    ACCEPT_PACKET,
    ACCEPT_REJECT,
    PREPARE_OK,
    PREPARE_PACKET,
    PREPARE_REJECT,
    PROPOSE_PACKET;

    public static PacketUIType fromPacket(PaxosPacket packet) {
        switch (packet.getType()) {
            case PROPOSE_PACKET:
                return PROPOSE_PACKET;
            case PREPARE_RESPONSE_PACKET:
                PrepareResponseLoad prepareLoad = (PrepareResponseLoad) packet.getLoad();
                return prepareLoad.isPrepareOk() ? PREPARE_OK : PREPARE_REJECT;
            case ACCEPT_RESPONSE_PACKET:
                AcceptResponseLoad acceptLoad = (AcceptResponseLoad) packet.getLoad();
                return acceptLoad.isAcceptOk() ? ACCEPT_OK : ACCEPT_REJECT;
            case PREPARE_PACKET:
                return PREPARE_PACKET;
            case ACCEPT_PACKET:
                return ACCEPT_PACKET;
        }
        throw new RuntimeException("unknown packet: " + packet);
    }
}
