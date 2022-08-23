package cn.loyx.paxossim.gui;

import cn.loyx.paxossim.gui.component.LinkComponent;
import cn.loyx.paxossim.gui.component.PacketComponent;
import cn.loyx.paxossim.gui.component.SiteComponent;
import org.pushingpixels.radiance.animation.api.swing.SwingComponentTimeline;

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
        addSite(site1);

        SiteComponent site2 = new SiteComponent();
        site2.setState(SiteComponent.SiteState.DOWN);
        site2.setLocation(200, 200);
        addSite(site2);

        PacketComponent packet = new PacketComponent(PacketComponent.PacketUIType.ACCEPT_OK, site1, site2, 10_000, this);
        addPacket(packet);

        LinkComponent link1 = new LinkComponent(site1, site2);
        add(link1);



    }

    private void addPacket(PacketComponent packet) {
        SwingComponentTimeline timeline = packet.createNewPacketTimeline();
        MouseAdapter packetMouseListener = new MouseAdapter() {
            Point previousPoint;
            SwingComponentTimeline currentTimeline = timeline;
            @Override
            public void mousePressed(MouseEvent e) {
                // todo select
                System.out.println("select packet");
                currentTimeline.cancel();
                previousPoint = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentTimeline = packet.createNewPacketTimeline();
                currentTimeline.play();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point currentPoint = e.getPoint();

            }
        };

        packet.addMouseListener(packetMouseListener);
        packet.addMouseMotionListener(packetMouseListener);
        add(packet);
        SwingUtilities.invokeLater(timeline::play);
    }

    private void addSite(SiteComponent component) {

        MouseAdapter siteMouseListener = new MouseAdapter() {
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
        component.addMouseListener(siteMouseListener);
        component.addMouseMotionListener(siteMouseListener);
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
