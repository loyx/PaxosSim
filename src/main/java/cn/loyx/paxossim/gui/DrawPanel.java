package cn.loyx.paxossim.gui;

import cn.loyx.paxossim.gui.component.LinkComponent;
import cn.loyx.paxossim.gui.component.PacketComponent;
import cn.loyx.paxossim.gui.component.SiteComponent;
import org.pushingpixels.radiance.animation.api.Timeline;
import org.pushingpixels.radiance.animation.api.TimelinePropertyBuilder;
import org.pushingpixels.radiance.animation.api.callback.TimelineCallbackAdapter;
import org.pushingpixels.radiance.animation.api.swing.SwingComponentTimeline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

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

        PacketComponent packet = new PacketComponent(PacketComponent.PacketUIType.ACCEPT_OK);
        addPacket(packet, site1, site2);

        LinkComponent link1 = new LinkComponent(site1, site2);
        add(link1);



    }

    private void addPacket(PacketComponent packet, SiteComponent src, SiteComponent dst) {
//        MouseAdapter DragListener = new MouseAdapter() {
//            public float progress = 0;
//
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                super.mouseDragged(e);
//            }
//        };
        class Progress{
            public float value = 0;
        }
        Progress progress = new Progress();

        // packet animation setting
        SwingComponentTimeline timeline = SwingComponentTimeline.componentBuilder(packet)
                .addPropertyToInterpolate(Timeline.<Float>property("progress")
                        .from(0.0f)
                        .to(1.0f)
                        .accessWith(new TimelinePropertyBuilder.PropertyAccessor<>() {
//                            float progress = 0;
                            @Override
                            public void set(Object o, String s, Float value) {
                                progress.value = value;
//                                System.out.println("progress: " + progress.value);
                                Rectangle sb = src.getBounds();
                                Rectangle eb = dst.getBounds();
                                int newX = (int) (sb.getCenterX() + (eb.getCenterX() - sb.getCenterX()) * progress.value);
                                int newY = (int) (sb.getCenterY() + (eb.getCenterY() - sb.getCenterY()) * progress.value);
                                packet.setCenterLocation(newX, newY);
                            }

                            @Override
                            public Float get(Object o, String s) {
                                System.out.println("getValue: " + progress.value);
                                return progress.value;
                            }
                        })
                )
                .setDuration(5_000)
                .addCallback(new TimelineCallbackAdapter(){
                    @Override
                    public void onTimelineStateChanged(Timeline.TimelineState oldState,
                                                       Timeline.TimelineState newState,
                                                       float durationFraction,
                                                       float timelinePosition) {
                        if (newState == Timeline.TimelineState.DONE){
                            remove(packet);
                            repaint();
                        }
                    }
                })
                .build();

        // packet mouse event setting
        Rectangle srcB = src.getBounds();
        Rectangle dstB = dst.getBounds();
        double distance = Point.distance(srcB.x, srcB.y, dstB.x, dstB.y);
        MouseAdapter packetMouseListener = new MouseAdapter() {
            Point previousPoint;
            @Override
            public void mousePressed(MouseEvent e) {
                // todo select
                System.out.println("select packet");
                timeline.suspend();
                previousPoint = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                timeline.resume();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point currentPoint = e.getPoint();
                progress.value = 0;
            }
        };

        timeline.play();
//        progress.playLoop(Timeline.RepeatBehavior.REVERSE);
        packet.addMouseListener(packetMouseListener);
        packet.addMouseMotionListener(packetMouseListener);
        add(packet);
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
