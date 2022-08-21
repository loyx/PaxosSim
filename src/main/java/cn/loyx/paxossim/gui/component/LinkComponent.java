package cn.loyx.paxossim.gui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class LinkComponent extends JComponent {
    SiteComponent start;
    SiteComponent end;

    public LinkComponent(SiteComponent start, SiteComponent end) {
        this.start = start;
        this.end = end;
        this.start.registerLink(this);
        this.end.registerLink(this);
        updateLink();
        setBackground(Color.red);
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
}
