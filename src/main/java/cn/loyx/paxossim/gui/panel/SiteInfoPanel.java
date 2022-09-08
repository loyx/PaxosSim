package cn.loyx.paxossim.gui.panel;

import cn.loyx.paxossim.gui.comm.EventListener;
import cn.loyx.paxossim.gui.comm.SimBus;
import cn.loyx.paxossim.gui.component.SiteComponent;
import cn.loyx.paxossim.sim.Simulator;

import javax.swing.*;
import java.awt.*;

public class SiteInfoPanel extends JPanel {

    private final JLabel dynamicInfo;
    Simulator simulator;
    SimBus bus = SimBus.getInstance();

    SiteInfoPanel(Dimension dimension){
        setPreferredSize(dimension);
//        setLayout(new FlowLayout());
        dynamicInfo = new JLabel("dynamic info");
        dynamicInfo.setSize(getWidth(), 10);
        add(dynamicInfo);
//        add(new JLabel("brief info"));

        bus.addEventListener(new EventListener() {
            @Override
            public void siteSelected(SiteComponent siteComponent) {
                System.out.println("SiteInfoPanel get site select event.");
                String name = siteComponent.getName();
                SiteComponent.SiteState state = siteComponent.getState();
                dynamicInfo.setText(String.format("%s - %s", name, state));
            }
        });
    }

    public void setSimulator(Simulator simulator) {
        this.simulator = simulator;
    }
}
