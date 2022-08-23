package cn.loyx.paxossim.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.apache.log4j.BasicConfigurator;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        BasicConfigurator.configure();
//        FlatLightLaf.setup();
        SwingUtilities.invokeLater(PaxosSimFrame::new);
    }
}
