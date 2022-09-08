package cn.loyx.paxossim.gui.panel;

import cn.loyx.paxossim.sim.Simulator;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    final int PANEL_WIDTH = 1200;
    final int PANEL_HEIGHT = 700;

    public MainPanel(){
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new BorderLayout());

        // create a simulator
        Simulator simulator = new Simulator("src/test/resources/simConfig.json");

        // north
        add(new JLabel("top"), BorderLayout.NORTH);

        // south
        add(new JLabel("bottom"), BorderLayout.SOUTH);

        // west
        SiteInfoPanel siteInfoPanel = new SiteInfoPanel(new Dimension(PANEL_WIDTH / 5, PANEL_HEIGHT));
        siteInfoPanel.setSimulator(simulator);
        add(siteInfoPanel, BorderLayout.WEST);

        // east
        add(new PacketInfoPanel(new Dimension(PANEL_WIDTH/5, PANEL_HEIGHT)), BorderLayout.EAST);

        // center
        add(new DrawPanel(simulator), BorderLayout.CENTER);
    }

}
