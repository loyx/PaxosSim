package cn.loyx.paxossim.gui;

import cn.loyx.paxossim.gui.component.PacketComponent;
import cn.loyx.paxossim.gui.component.SiteComponent;

import javax.swing.*;

public class DrawPanel extends JPanel {
    DrawPanel(){
        // settings
        setLayout(null);

        // sites
        SiteComponent site1 = new SiteComponent();
        site1.setState(SiteComponent.SiteState.RUN);
        site1.setLocation(100, 100);
        add(site1);

        SiteComponent site2 = new SiteComponent();
        site2.setState(SiteComponent.SiteState.DOWN);
        site2.setLocation(0,0);
        add(site2);

        SiteComponent site3 = new SiteComponent();
        site3.setState(SiteComponent.SiteState.INITIAL);
        site3.setLocation(250,250);
        add(site3);

        PacketComponent p1 = new PacketComponent(PacketComponent.PacketUIType.PREPARE_PACKET);
        p1.setLocation(100 ,20);
        add(p1);

        PacketComponent p2 = new PacketComponent(PacketComponent.PacketUIType.PREPARE_OK);
        p2.setLocation(200 ,20);
        add(p2);

        PacketComponent p3 = new PacketComponent(PacketComponent.PacketUIType.PREPARE_REJECT);
        p3.setLocation(300 ,20);
        add(p3);
    }

}
