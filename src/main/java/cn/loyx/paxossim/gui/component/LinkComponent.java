package cn.loyx.paxossim.gui.component;

import org.pushingpixels.radiance.animation.api.Timeline;
import org.pushingpixels.radiance.animation.api.TimelinePropertyBuilder;
import org.pushingpixels.radiance.animation.api.swing.SwingComponentTimeline;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.LinkedList;
import java.util.List;

public class LinkComponent extends JComponent {
    SiteComponent start;
    SiteComponent end;

    List<PacketComponent> packets = new LinkedList<>();

    public LinkComponent(SiteComponent start, SiteComponent end) {
        this.start = start;
        this.end = end;
        this.start.registerLink(this);
        this.end.registerLink(this);
        updateLink();
        setBackground(Color.red);
    }

    public void addPacket(PacketComponent packet){
        packets.add(packet);
        add(packet);
        TimelinePropertyBuilder.PropertyAccessor<Float> propertyAccessor = new TimelinePropertyBuilder.PropertyAccessor<>() {

            float progress = 0;
            @Override
            public void set(Object o, String s, Float value) {
                progress = value;
                System.out.println("progress: " + progress);

                int newX = (int) ((getWidth() - packet.getWidth()) * progress);
                int newY = (int) ((getHeight() - packet.getHeight()) * progress);
                packet.setLocation(newX, newY);
            }

            @Override
            public Float get(Object o, String s) {
                return progress;
            }
        };
        SwingComponentTimeline progress = SwingComponentTimeline.componentBuilder(packet)
                .addPropertyToInterpolate(Timeline.<Float>property("progress")
                        .from(0.0f)
                        .to(1.0f)
                        .accessWith(propertyAccessor)
                )
                .setDuration(5_000)
                .build();
        progress.playLoop(Timeline.RepeatBehavior.REVERSE);
//        packet.setLocation(0, 0);
    }

    public void updateLink(){
        int x1 = (int) start.getBounds().getCenterX();
        int y1 = (int) start.getBounds().getCenterY();
        int x2 = (int) end.getBounds().getCenterX();
        int y2 = (int) end.getBounds().getCenterY();
        if (x1 > x2){
            SiteComponent temp = end;
            end = start;
            start = temp;
        }
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        setLocation(minX, minY);
        setSize(maxX - minX, maxY - minY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        if (start.getLocation().y < end.getLocation().y) {
            g2D.draw(new Line2D.Double(0, 0, getWidth(), getHeight()));
        } else {
            g2D.draw(new Line2D.Double(0, getHeight(), getWidth(), 0));
        }

    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
    }
}
