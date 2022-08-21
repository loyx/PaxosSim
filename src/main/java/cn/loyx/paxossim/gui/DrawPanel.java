package cn.loyx.paxossim.gui;

import cn.loyx.paxossim.gui.component.LinkComponent;
import cn.loyx.paxossim.gui.component.PacketComponent;
import cn.loyx.paxossim.gui.component.SiteComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DrawPanel extends JPanel {

    SiteComponent select;
    DrawPanel(){
        // settings
        setLayout(null);

        DrawPanelMouseListener drawPanelMouseListener = new DrawPanelMouseListener();
        addMouseListener(drawPanelMouseListener);

        // sites
        SiteComponent site1 = new SiteComponent();
        site1.setState(SiteComponent.SiteState.RUN);
        site1.setLocation(100, 100);
        addPaxosComponent(site1);

        SiteComponent site2 = new SiteComponent();
        site2.setState(SiteComponent.SiteState.DOWN);
        site2.setLocation(200, 200);
        addPaxosComponent(site2);

        LinkComponent link1 = new LinkComponent(site1, site2);
        link1.setBackground(Color.cyan);
        add(link1);

        link1.addPacket(new PacketComponent(PacketComponent.PacketUIType.ACCEPT_PACKET));


    }

    private void addPaxosComponent(SiteComponent component) {

        MouseAdapter componentMouseListener = new MouseAdapter() {
            Point previousPoint = null;
            @Override
            public void mousePressed(MouseEvent e) {
                select = component;
                previousPoint = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                previousPoint = null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point currentPoint = e.getPoint();
                int dx = (int) (currentPoint.getX() - previousPoint.getX());
                int dy = (int) (currentPoint.getY() - previousPoint.getY());
                Point location = component.getLocation();
                location.translate(dx, dy);
                component.setLocation(location);
            }
        };
        component.addMouseListener(componentMouseListener);
        component.addMouseMotionListener(componentMouseListener);
        add(component);
    }

    private class DrawPanelMouseListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            select = null;
            System.out.println("unselect");
        }
    }

}
