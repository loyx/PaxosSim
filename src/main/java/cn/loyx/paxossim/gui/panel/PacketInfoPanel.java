package cn.loyx.paxossim.gui.panel;

import cn.loyx.paxossim.gui.comm.EventListener;
import cn.loyx.paxossim.gui.comm.SimBus;
import cn.loyx.paxossim.gui.component.PacketComponent;

import javax.swing.*;
import java.awt.*;

public class PacketInfoPanel extends JPanel {

    SimBus bus = SimBus.getInstance();
    PacketInfoPanel(Dimension dimension){
        setPreferredSize(dimension);
        setLayout(new FlowLayout());
        setBackground(Color.cyan);
        add(new JLabel("dynamic info"));
        add(new JLabel("brief info"));
        bus.addEventListener(new EventListener() {
            @Override
            public void packetSelected(PacketComponent packetComponent) {
                System.out.println("PacketInfoPanel get packet select event.");
            }
        });
    }
}
