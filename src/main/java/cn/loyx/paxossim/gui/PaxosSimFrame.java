package cn.loyx.paxossim.gui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class PaxosSimFrame extends JFrame {

    MainPanel panel;
    PaxosSimFrame(){
        super("PaxosSim");

        FlatLightLaf.setup();

        panel = new MainPanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
