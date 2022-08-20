package cn.loyx.paxossim;

import javax.swing.*;
import java.awt.*;

public class Packet extends JComponent {
    Image packetIcon;

    Packet(){
        packetIcon = new ImageIcon().getImage();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
//        g2D.drawImage(packetIcon);
    }
}
