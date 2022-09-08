package cn.loyx.paxossim.gui.panel;

import cn.loyx.paxos.Paxos;
import cn.loyx.paxos.PaxosValue;
import cn.loyx.paxos.conf.NodeInfo;
import cn.loyx.paxos.protocol.PaxosPacket;
import cn.loyx.paxossim.gui.comm.SimBus;
import cn.loyx.paxossim.gui.component.LinkComponent;
import cn.loyx.paxossim.gui.component.PacketComponent;
import cn.loyx.paxossim.gui.component.PacketUIType;
import cn.loyx.paxossim.gui.component.SiteComponent;
import cn.loyx.paxossim.sim.ControllableCommunicator;
import cn.loyx.paxossim.sim.PacketListener;
import cn.loyx.paxossim.sim.SimConfig;
import cn.loyx.paxossim.sim.Simulator;
import org.pushingpixels.radiance.animation.api.swing.SwingComponentTimeline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel {

    SiteComponent select;
    List<SiteComponent> sites = new ArrayList<>();
    SimBus bus = SimBus.getInstance();
    DrawPanel(Simulator simulator){
        // DrawPanel settings
        setLayout(null);
        DrawPanelMouseListener drawPanelMouseListener = new DrawPanelMouseListener();
        addMouseListener(drawPanelMouseListener);

        // simulator
        SimConfig config = simulator.getConfig();
        System.out.println(config);
        JComponent drawInstance = this;
        for (NodeInfo nodeInfo : config.getNodeList()) {
            SiteComponent site = new SiteComponent();
            site.setState(SiteComponent.SiteState.RUN);
            for (SiteComponent preSite : sites) {
                LinkComponent link = new LinkComponent(preSite, site);
                add(link);
            }
            sites.add(site);
            addSite(site); // add UI component
            int id = nodeInfo.getId();
            site.setName("Paxos Site " + id);
            ControllableCommunicator comm = simulator.getComm(id);
            comm.addPacketListener(new PacketListener() {
                final SiteComponent src = site;
                @Override
                public void onSendPacket(String ip, int port, PaxosPacket paxosPacket) {
                    PacketUIType packetUIType = PacketUIType.fromPacket(paxosPacket);
                    SiteComponent dst = sites.get(config.getIdFromNetAddr(ip, port));
                    PacketComponent packetComponent = new PacketComponent(packetUIType, src, dst, 10_000, drawInstance);
                    packetComponent.setName(String.format("%s -> %s: %s", src.getName(), dst.getName(), paxosPacket.getType()));
                    addPacket(packetComponent);
                }
            });
        }

        // start simulator
        simulator.start();

        JButton propose = new JButton("propose");
        propose.setLocation(10, 500);
        propose.setSize(100, 60);
        propose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Paxos paxos = simulator.getPaxos(0);
                paxos.propose(new PaxosValue("site1"), null);
            }
        });
        add(propose);

    }

    private void addPacket(PacketComponent packet) {
        SwingComponentTimeline timeline = packet.createNewPacketTimeline();
        MouseAdapter packetMouseListener = new MouseAdapter() {
            Point previousPoint;
            SwingComponentTimeline currentTimeline = timeline;
            JComponent comp;
            @Override
            public void mousePressed(MouseEvent e) {
                // todo select
                System.out.println("select packet");
                bus.selectPacket(packet);
                currentTimeline.cancel();
                previousPoint = e.getPoint();
                comp = (JComponent) e.getSource();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentTimeline = packet.createNewPacketTimeline();
                currentTimeline.play();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point currentPoint = e.getPoint();
                SiteComponent src = packet.getSrc();
                SiteComponent dst = packet.getDst();
                if (src == dst){
                    return;
                }
                double x1 = comp.getX() + currentPoint.x - src.getBounds().getCenterX();
                double y1 = comp.getY() + currentPoint.y - src.getBounds().getCenterY();
                double x2 = dst.getBounds().getCenterX() - src.getBounds().getCenterX();
                double y2 = dst.getBounds().getCenterY() - src.getBounds().getCenterY();
                double length = Math.sqrt(x2 * x2 + y2 * y2);
                double progress = (x1*x2+y1*y2)/ (length*length);
                if (progress < 0) progress = 0;
                if (progress > 1) progress = 1;
                packet.setProgress((float) progress);
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
                bus.selectSite(component);
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
