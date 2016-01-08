package com.queuingSystem.GUI;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by 风之凌殇 on 2016/1/8.
 */
public class TicketMachine {
    private static int cnt = -1;
    private JButton newCustomer;
    private JButton shutdown;
    private JPanel panel;
    private JLabel name;
    private JLabel numbers;
    private JFrame frame;
    //client side socket
    private Socket socket;
    //socket for updating queue size in ui
    private Updater updater;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader sin;

    public TicketMachine(String ip, int port) {
        frame = new JFrame("TicketMachine");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        name.setText("取号机 " + (++cnt));
        frame.setSize(300, 200);
        frame.setVisible(true);

        newCustomer.setMnemonic('n');
        shutdown.setMnemonic('s');

        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //initialize the IO object
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            sin = new BufferedReader(new InputStreamReader(System.in));

            //
            try {
                //create the new counter
                String cmd = "new ticket machine";
                out.println(cmd);
                String reply = in.readLine();
                String customerInQueue = "队列人数: " + in.readLine();
                numbers.setText(customerInQueue);
                JOptionPane.showMessageDialog(null, reply, "Message from the server", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        } catch (IOException e) {
            // if there is any exception, close the socket
            try {
                socket.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }

        try {
            updater = new Updater(InetAddress.getByName(ip), port, "new ticketMachine updater", numbers);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        newCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    out.println("new customer");
                    String reply = in.readLine();
                    JOptionPane.showMessageDialog(null, reply, "Message from the server", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
        shutdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("stop ticket machine");
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        numbers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    String cmd = "current queue size";
                    out.println(cmd);
                    String customerInQueue = "队列人数: " + in.readLine();
                    numbers.setText(customerInQueue);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        new TicketMachine("localhost", 2333);
    }
}
