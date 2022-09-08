package cn.loyx.paxossim.gui.panel;

import cn.loyx.paxossim.gui.comm.EventListener;
import cn.loyx.paxossim.gui.comm.SimBus;
import cn.loyx.paxossim.gui.component.SiteComponent;
import cn.loyx.paxossim.sim.Simulator;

import javax.swing.*;
import java.awt.*;

public class SiteInfoPanel extends JPanel {

    Simulator simulator;
    SimBus bus = SimBus.getInstance();

    SiteInfoPanel(Dimension dimension){
        setPreferredSize(dimension);
        setLayout(new FlowLayout());
        setBackground(Color.green);
        add(new JLabel("dynamic info"));
        add(new JLabel("brief info"));

        bus.addEventListener(new EventListener() {
            @Override
            public void siteSelected(SiteComponent siteComponent) {
                System.out.println("SiteInfoPanel get site select event.");
            }
        });
    }

    public void setSimulator(Simulator simulator) {
        this.simulator = simulator;
    }
}
