package com.queuingSystem.GUI;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by 风之凌殇 on 2016/1/8.
 */
public class Updater extends Thread {
    private Socket updater;
    private BufferedReader in;
    private PrintWriter out;
    private String initCmd;
    private JLabel numbers;

    //constructor
    public Updater(InetAddress addr, int portNo, String cmd, JLabel num) {
        initCmd = cmd;
        numbers = num;
        try {
            updater = new Socket(addr, portNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //initialize the IO object
        try {
            in = new BufferedReader(new InputStreamReader(updater.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(updater.getOutputStream())), true);

            //start the thread
            start();
        } catch (IOException e) {
            // if there is any exception, close the updater
            try {
                updater.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void run() {
        try {
            //create the new counter
            String cmd = initCmd;
            out.println(cmd);
            //wait for update signal
            for (; ; ) {
                String reply = in.readLine();
                if (!reply.equals("stop")) {
                    String customerInQueue = "队列人数: " + reply;
                    numbers.setText(customerInQueue);
                } else {
                    out.println("stop updater");
                    updater.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                updater.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}