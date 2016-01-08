package com.queuingSystem.console;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by 风之凌殇 on 2016/1/8.
 */

class ThreadTicketMachine extends Thread {
    //client side socket
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader sin;

    //constructor
    public ThreadTicketMachine(InetAddress addr) {
        try {
            socket = new Socket(addr, 3333);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //initialize the IO object
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            sin = new BufferedReader(new InputStreamReader(System.in));

            //start the thread
            start();
        } catch (IOException e) {
            // if there is any exception, close the socket
            try {
                socket.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void run() {
        try {
            //create the new counter
            String cmd = "new ticket machine";
            out.println(cmd);
            String reply = in.readLine();
            System.out.println(reply);
            //main body
            for (; ; ) {
                // cmd will be determined by the button pressed
                cmd = sin.readLine();
                out.println(cmd);
                switch (cmd) {
                    case "new customer":
                        reply = in.readLine();
                        System.out.println(reply);
                        break;
                    default:
                        break;
                }
                if (cmd.equals("stop ticket machine"))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class TicketMachineCreator_____ {
    public static void main(String[] args) throws IOException, InterruptedException {
        InetAddress addr = InetAddress.getByName("localhost");
//        InetAddress addr = InetAddress.getByName("10.171.44.41");
        new ThreadTicketMachine(addr);
    }
}
