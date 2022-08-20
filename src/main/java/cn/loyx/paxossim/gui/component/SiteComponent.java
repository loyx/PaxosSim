package cn.loyx.paxossim.gui.component;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SiteComponent extends JComponent {

    public enum SiteState {
        INITIAL,
        BOOT,
        RUN,
        DOWN,
    }

    static Map<SiteState, Image> imageResources;
    static {
        String[] imagesPath = {
                "/icon/site_initial.png",
                "/icon/site_boot.png",
                "/icon/site_run.png",
                "/icon/site_down.png",
        };
        SiteState[] states = SiteState.values();
        imageResources = new HashMap<>();
        for (int i = 0; i < imagesPath.length; i++) {
            URL resource = SiteState.class.getResource(imagesPath[i]);
            imageResources.put(states[i], new ImageIcon(Objects.requireNonNull(resource)).getImage());
        }
    }

    Image siteImage;
    SiteState state;

    // constructors
    public SiteComponent(){
        setState(SiteState.INITIAL);
        setSize(siteImage.getWidth(null), siteImage.getHeight(null));
    }

    // status
    public void setState(SiteState state){
        this.state = state;
        siteImage = imageResources.get(this.state);
    }


    // UI
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(siteImage, 0, 0, null);
    }
}
