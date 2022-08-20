package cn.loyx.paxossim.gui;

import javax.swing.*;
import java.awt.*;

public class SiteInfoPanel extends JPanel {

    SiteInfoPanel(Dimension dimension){
        setPreferredSize(dimension);
        setLayout(new FlowLayout());
        setBackground(Color.green);
        add(new JLabel("dynamic info"));
        add(new JLabel("brief info"));
    }
}
