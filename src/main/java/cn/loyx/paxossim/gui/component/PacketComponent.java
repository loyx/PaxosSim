package cn.loyx.paxossim.gui.component;

import cn.loyx.paxos.protocol.PaxosPacket;
import cn.loyx.paxos.protocol.load.AcceptResponseLoad;
import cn.loyx.paxos.protocol.load.PrepareResponseLoad;
import org.pushingpixels.radiance.animation.api.Timeline;
import org.pushingpixels.radiance.animation.api.callback.TimelineCallbackAdapter;
import org.pushingpixels.radiance.animation.api.swing.SwingComponentTimeline;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PacketComponent extends JComponent {

    static Map<PacketUIType, Image> imageResources;

    static {
        String[] imagesPath = {
                "/icon/accept_ok.png",
                "/icon/accept_packet.png",
                "/icon/accept_reject.png",
                "/icon/prepare_ok.png",
                "/icon/prepare_packet.png",
                "/icon/prepare_reject.png",
                "/icon/propose_packet.png",
        };
        imageResources = new HashMap<>();
        PacketUIType[] types = PacketUIType.values();
        for (int i = 0; i < imagesPath.length; i++) {
            URL resource = PacketComponent.class.getResource(imagesPath[i]);
            imageResources.put(types[i], new ImageIcon(Objects.requireNonNull(resource)).getImage());
        }
    }

    Image packetIcon;
    PacketUIType type;
    final SiteComponent src;
    final SiteComponent dst;
    final long delayTime;
    final JComponent fatherComponent;
    private float progress;


    public PacketComponent(PacketUIType type, SiteComponent src, SiteComponent dst, long delayTime, JComponent fatherComponent){
        this.type = type;
        packetIcon = imageResources.get(type);
        this.src = src;
        this.dst = dst;
        this.delayTime = delayTime;
        this.fatherComponent = fatherComponent;
        setSize(packetIcon.getWidth(null), packetIcon.getHeight(null));
        if (src == dst){
            setCenterLocation((int) (src.getBounds().getCenterX() + 0.5*src.getWidth() + .5*getWidth()),
                    (int) src.getBounds().getCenterY());
        } else {
            setCenterLocation((int) src.getBounds().getCenterX(), (int) src.getBounds().getCenterY());
        }
    }

    public SwingComponentTimeline createNewPacketTimeline(){
        PacketComponent thisInstance = this;
        return SwingComponentTimeline.componentBuilder(this)
                .addPropertyToInterpolate(Timeline.<Float>property("progress")
                        .fromCurrent()
                        .to(1.0f)
                )
                .setDuration((long) (delayTime * (1 - progress)))
                .addCallback(new TimelineCallbackAdapter() {
                    @Override
                    public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                        if (newState == Timeline.TimelineState.DONE){
                            fatherComponent.remove(thisInstance);
                            fatherComponent.repaint();
                        }
                    }
                })
                .build();
    }

    public void setCenterLocation(int x, int y){
        setLocation(x - getWidth()/2, y - getHeight()/2);
        repaint();
    }
    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        if (src == dst){
            setCenterLocation((int) (src.getBounds().getCenterX() + 0.5*src.getWidth() + .5*getWidth()),
                    (int) src.getBounds().getCenterY());
            return;
        }
        Rectangle sb = src.getBounds();
        Rectangle eb = dst.getBounds();
        int newX = (int) (sb.getCenterX() + (eb.getCenterX() - sb.getCenterX()) * progress);
        int newY = (int) (sb.getCenterY() + (eb.getCenterY() - sb.getCenterY()) * progress);
        setCenterLocation(newX, newY);
    }

    public SiteComponent getSrc() {
        return src;
    }

    public SiteComponent getDst() {
        return dst;
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(packetIcon, 0, 0, null);
    }
}
