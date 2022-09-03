package cn.loyx.paxossim.gui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class LinkComponent extends JComponent {
    SiteComponent start;
    SiteComponent end;
    private int lineWidth;


    public LinkComponent(SiteComponent start, SiteComponent end) {
        this(start, end, 3);
    }

    public LinkComponent(SiteComponent start, SiteComponent end, int lineWidth){
        this.start = start;
        this.end = end;
        this.lineWidth = lineWidth;
        this.start.registerLink(this);
        this.end.registerLink(this);
        updateLink();
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
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
        setLocation((int) (minX + lineWidth/2.0), (int) (minY + lineWidth/2.0));
        setSize(maxX - minX + lineWidth , maxY - minY + lineWidth);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g.create();

        // enable antialias
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // dash line
        g2D.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1, new float[]{5}, 0));

        // diag and back-diag
        double[] pos;
        double halfWidth = lineWidth / 2.0;
        if (start.getLocation().y < end.getLocation().y) {
            pos = new double[]{0 + halfWidth, 0 + halfWidth, getWidth()- halfWidth, getHeight() - halfWidth};
        } else {
            pos = new double[]{0 + halfWidth, getHeight() - halfWidth, getWidth() - halfWidth, 0 + halfWidth};
        }

        // end margin
        double yLen = Math.abs(end.getBounds().getCenterY() - start.getBounds().getCenterY());
        double xLen = Math.abs(end.getBounds().getCenterX() - start.getBounds().getCenterX());
        double diagLength = Math.sqrt(xLen * xLen + yLen * yLen);
        double marginRatio;
        if (xLen > yLen){
            marginRatio = (start.getWidth()/2.0/ (xLen / diagLength)) / diagLength;
        } else {
            marginRatio = (start.getHeight()/2.0/ (yLen / diagLength)) / diagLength;
        }
        pos = new double[]{
                pos[0] + marginRatio * (pos[2] - pos[0]),
                pos[1] + marginRatio * (pos[3] - pos[1]),
                pos[0] + (1 - marginRatio) * (pos[2] - pos[0]),
                pos[1] + (1 - marginRatio) * (pos[3] - pos[1]),
        };

        // draw line
        g2D.draw(new Line2D.Double(pos[0], pos[1], pos[2], pos[3]));
        g2D.dispose();
    }
}
