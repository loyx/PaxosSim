package cn.loyx.paxossim.gui.panel;

import cn.loyx.paxossim.gui.comm.EventListener;
import cn.loyx.paxossim.gui.comm.SimBus;
import cn.loyx.paxossim.gui.component.PacketComponent;
import cn.loyx.paxossim.gui.component.PacketUIType;

import javax.swing.*;
import java.awt.*;

public class PacketInfoPanel extends JPanel {

    private final JTextArea dynamicInfo;
    SimBus bus = SimBus.getInstance();
    PacketInfoPanel(Dimension dimension){
        setPreferredSize(dimension);
        setLayout(new FlowLayout());
//        setBackground(Color.cyan);
        dynamicInfo = new JTextArea("dynamic info");
        dynamicInfo.setLineWrap(true);
        dynamicInfo.setWrapStyleWord(true);
        dynamicInfo.setEditable(false);
        add(dynamicInfo);
//        add(new JLabel("brief info"));
        bus.addEventListener(new EventListener() {
            @Override
            public void packetSelected(PacketComponent packetComponent) {
                System.out.println("PacketInfoPanel get packet select event.");
                String name = packetComponent.getName();
                PacketUIType type = packetComponent.getType();
                dynamicInfo.setText(String.format("%s - %s", name, type));
            }
        });
    }
}
