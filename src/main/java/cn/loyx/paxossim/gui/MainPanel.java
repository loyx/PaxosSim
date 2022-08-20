package cn.loyx.paxossim.gui;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    final int PANEL_WIDTH = 1200;
    final int PANEL_HEIGHT = 700;

    MainPanel(){
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new BorderLayout());

        // north
        add(new JLabel("top"), BorderLayout.NORTH);

        // south
        add(new JLabel("bottom"), BorderLayout.SOUTH);

        // west
        add(new SiteInfoPanel(new Dimension(PANEL_WIDTH/5, PANEL_HEIGHT)), BorderLayout.WEST);

        // east
        add(new PacketInfoPanel(new Dimension(PANEL_WIDTH/5, PANEL_HEIGHT)), BorderLayout.EAST);

        // center
        add(new DrawPanel(), BorderLayout.CENTER);
    }

}
