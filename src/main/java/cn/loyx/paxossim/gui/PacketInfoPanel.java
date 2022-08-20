package cn.loyx.paxossim.gui;

import javax.swing.*;
import java.awt.*;

public class PacketInfoPanel extends JPanel {

    PacketInfoPanel(Dimension dimension){
        setPreferredSize(dimension);
        setLayout(new FlowLayout());
        setBackground(Color.cyan);
        add(new JLabel("dynamic info"));
        add(new JLabel("brief info"));
    }
}
