package cn.loyx.paxossim.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
//        FlatLightLaf.setup();
        SwingUtilities.invokeLater(PaxosSimFrame::new);
    }
}
