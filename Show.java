package com.crud.applaundry;

import javax.swing.*;

public class Show {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Show::createGUI);
    }

    public static void createGUI() {
        AplikasiLaundry UI = new AplikasiLaundry();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(UI.getContentPane());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
