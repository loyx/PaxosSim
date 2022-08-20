package cn.loyx.paxossim.gui.component;


import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PacketComponent extends JComponent {

    public static enum PacketUIType {
        ACCEPT_OK,
        ACCEPT_PACKET,
        ACCEPT_REJECT,
        PREPARE_OK,
        PREPARE_PACKET,
        PREPARE_REJECT,
        PROPOSE_PACKET,
    }
    static Map<PacketUIType, Image> imageResources;

    static {
        String[] imagesPath = {
                "/icon/accept_ok.png",
                "/icon/accept_packet.png",
                "/icon/accept_reject.png",
                "/icon/prepare_ok.png",
                "/icon/prepare_packet.png",
                "/icon/prepare_reject.png",
                "/icon/propose_packet.png",
        };
        imageResources = new HashMap<>();
        PacketUIType[] types = PacketUIType.values();
        for (int i = 0; i < imagesPath.length; i++) {
            URL resource = PacketComponent.class.getResource(imagesPath[i]);
            imageResources.put(types[i], new ImageIcon(Objects.requireNonNull(resource)).getImage());
        }
    }

    Image packetIcon;
    PacketUIType type;

    public PacketComponent(PacketUIType type){
        this.type = type;
        packetIcon = imageResources.get(type);
        setSize(packetIcon.getWidth(null), packetIcon.getHeight(null));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(packetIcon, 0, 0, null);
    }
}
