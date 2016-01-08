package com.queuingSystem.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by 风之凌殇 on 2016/1/8.
 */
public class TicketMachineCreator {
    private JButton createNewTicketMachine;
    private JPanel panel;
    private JTextField ipAddress;
    private JTextField port;
    private JFrame frame;

    public TicketMachineCreator() {
        frame = new JFrame("TicketMachineCreator");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        createNewTicketMachine.setMnemonic('n');

        createNewTicketMachine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TicketMachine(ipAddress.getText(), Integer.parseInt(port.getText()));
            }
        });
    }

    public static void main(String[] args) {
        new TicketMachineCreator();
    }
}
