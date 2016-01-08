package com.queuingSystem.GUI;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

/**
 * Created by 风之凌殇 on 2016/1/8.
 */
public class Counter {
    private static int cnt = -1;
    private JButton serverTheClient;
    private JButton shutdown;
    private JPanel panel;
    private JButton finishService;
    private JLabel name;
    private JLabel numbers;
    private JLabel state;
    private JFrame frame;
    //client side socket
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Counter(String ip, int port) {
        frame = new JFrame("餐桌");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        name.setText("餐桌 " + (++cnt));
        frame.setSize(300, 200);
        frame.setVisible(true);

        serverTheClient.setMnemonic('n');
        finishService.setMnemonic('f');
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

            //
            try {
                //create the new counter
                String cmd = "new counter";
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

        serverTheClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    out.println("start server the client");
                    String reply = in.readLine();
                    if (reply.equals("后面已无人等待，无人可以入座。") || reply.equals("当前座位有人，新客户无法入座。"))
                        JOptionPane.showMessageDialog(null, reply, "Message from the server", JOptionPane.WARNING_MESSAGE);
                    else {
                        int customerId = Integer.parseInt(reply);
                        String customerInQueue = "队列人数: " + in.readLine();
                        numbers.setText(customerInQueue);
                        state.setText("使用中");
                        JOptionPane.showMessageDialog(null, "客户 " + customerId + " 入座", "Message from the server", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        shutdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    out.println("stop counter");
                    String reply = in.readLine();
                    if (reply.equals("success")) {
                        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    } else {
                        JOptionPane.showMessageDialog(null, reply, "Message from the server", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
        finishService.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    out.println("finish serve client");
                    String reply = in.readLine();
                    if (!reply.equals("当前座位无人。")) {
                        state.setText("空闲");
                        JOptionPane.showMessageDialog(null, "客户 " + reply + " 完成就餐。", "Message from the server", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, reply, "Message from the server", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
        numbers.addMouseMotionListener(new MouseMotionAdapter() {
        });
    }

    public static void main(String[] args) {
        new Counter("localhost", 2333);
    }
}
