package com.queuingSystem.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by 风之凌殇 on 2016/1/8.
 */
public class CounterCreator {
    private JButton createNewCounter;
    private JPanel panel;
    private JTextField ipAddress;
    private JTextField port;
    private JFrame frame;

    public CounterCreator() {
        frame = new JFrame("家具市场");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        createNewCounter.setMnemonic('n');

        createNewCounter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Counter(ipAddress.getText(), Integer.parseInt(port.getText()));
            }
        });
    }

    public static void main(String[] args) {
        new CounterCreator();
    }
}
