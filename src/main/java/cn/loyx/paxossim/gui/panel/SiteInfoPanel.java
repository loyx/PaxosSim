package cn.loyx.paxossim.gui.panel;

import cn.loyx.paxossim.gui.comm.EventListener;
import cn.loyx.paxossim.gui.comm.SimBus;
import cn.loyx.paxossim.gui.component.SiteComponent;
import cn.loyx.paxossim.sim.Simulator;
import org.pushingpixels.radiance.animation.api.interpolator.KeyFrames;
import org.pushingpixels.radiance.animation.api.interpolator.KeyTimes;
import org.pushingpixels.radiance.animation.api.interpolator.KeyValues;
import org.pushingpixels.radiance.animation.api.swing.SwingComponentTimeline;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SiteInfoPanel extends JPanel {

    public static class ArrowComponent extends JComponent{

        List<Color> colors = Arrays.asList(
                Color.gray,
                Color.cyan,
                Color.green,
                Color.red
        );
        Color paintColor;
        int width = 10;
        int delta;
        int initial;
        int status = 0;
        double pos = 0;


        double verticalExtend = 0;
        public ArrowComponent(Dimension dimension, int delta, int initial){
            this.delta = delta;
            this.initial = initial;
            paintColor = colors.get(status);
            setPreferredSize(new Dimension(width, dimension.height));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(paintColor);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.fill(new Ellipse2D.Double(0, initial + pos * delta - width/2.0, width, width*(1+verticalExtend)));
        }

        public void setStatus(int status) {
            KeyTimes times = new KeyTimes(0.0f, 0.7f, 1.0f);
            KeyValues<Double> values = KeyValues.create(0.0, 0.9, 0.0);

            SwingComponentTimeline timeline = SwingComponentTimeline.componentBuilder(this)
                    .setForceUiUpdate(true)
                    .addPropertyToInterpolate("paintColor", paintColor, colors.get(status))
                    .addPropertyToInterpolate("verticalExtend", new KeyFrames<>(values, times))
                    .addPropertyToInterpolate("pos", (double)this.status, (double)status)
                    .setDuration(200)
                    .build();
            timeline.play();
            this.status = status;
        }

        public void setPaintColor(Color paintColor) {
            this.paintColor = paintColor;
        }

        public Color getPaintColor() {
            return paintColor;
        }

        public void setPos(double pos) {
//            System.out.println("set pos: " + pos);
            this.pos = pos;
            repaint();
        }

        public double getPos() {
            return pos;
        }

        public double getVerticalExtend() {
            return verticalExtend;
        }

        public void setVerticalExtend(double verticalExtend) {
            this.verticalExtend = verticalExtend;
            System.out.println("verticalExtend: " + verticalExtend);
        }
    }

    private final JLabel dynamicInfo;
    private final JPanel controlPanel;
    Simulator simulator;
    SimBus bus = SimBus.getInstance();

    SiteInfoPanel(Dimension dimension){
        setPreferredSize(dimension);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        dynamicInfo = new JLabel("dynamic info");
        dynamicInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
//        dynamicInfo.setBorder(BorderFactory.createLineBorder(Color.red));
        add(dynamicInfo);

        bus.addEventListener(new EventListener() {
            @Override
            public void siteSelected(SiteComponent siteComponent) {
                System.out.println("SiteInfoPanel get site select event.");
                String name = siteComponent.getName();
                SiteComponent.SiteState state = siteComponent.getState();
                dynamicInfo.setText(String.format("%s - %s", name, state));
            }
        });

        // control panel
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 1;
        int top = 10;
        c.insets = new Insets(top, 10, 0, 0);
        int delta = 0;
        List<JButton> controlButtons = new ArrayList<>();
        SiteComponent.SiteState[] siteStates = SiteComponent.SiteState.values();
        for (int i = 0; i < siteStates.length; i++) {
            SiteComponent.SiteState siteState = siteStates[i];
            c.gridy = i;
            JButton button = new JButton(siteState.name());
            delta = (int) button.getPreferredSize().getHeight();
            controlButtons.add(button);
            controlPanel.add(button, c);
        }
        ArrowComponent arrow = new ArrowComponent(controlPanel.getPreferredSize(), delta + top, delta / 2 + top);
        for (int i = 0; i < controlButtons.size(); i++) {
            int finalI = i;
            controlButtons.get(i).addActionListener(e -> {
                arrow.setStatus(finalI);
            });
        }
        c.insets = new Insets(0,0,0,0);
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = siteStates.length;
        controlPanel.add(arrow, c);

//        controlPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        controlPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.setMaximumSize(new Dimension(dimension.width, controlPanel.getPreferredSize().height));
        add(controlPanel);
    }
    public void setSimulator(Simulator simulator) {
        this.simulator = simulator;
    }
}
