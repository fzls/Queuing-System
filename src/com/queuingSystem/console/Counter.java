package com.queuingSystem.console;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by 风之凌殇 on 2016/1/8.
 */
class ThreadCounter extends Thread {
    //client side socket
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader sin;

    //constructor
    public ThreadCounter(InetAddress addr) {
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
            String cmd = "new counter";
            out.println(cmd);
            String reply = in.readLine();
            System.out.println(reply);
            //main body
            for (; ; ) {
                // cmd will be determined by the button pressed
                cmd = sin.readLine();
                out.println(cmd);
                switch (cmd) {
                    case "start server the client":
                        reply = in.readLine();
                        if (reply.equals("there is no client in the queue") || reply.equals("WARNING : current customer has not finish service"))
                            System.out.println(reply);
                        else {
                            int customerId = Integer.parseInt(reply);
                            System.out.println("assigned customer " + customerId);
                        }
                        break;
                    case "finish serve client":
                        reply = in.readLine();
                        if (!reply.equals("WARNING : no customer in service")) {
                            System.out.println("customer " + reply + " finish the service");
                        } else {
                            System.out.println(reply);
                        }
                        break;
                    default:
                        break;
                }
                if (cmd.equals("stop counter"))
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

public class Counter {
    public static void main(String[] args) throws IOException, InterruptedException {
        //TODO set the ip in the Client UI
        InetAddress addr = InetAddress.getByName("localhost");
//        InetAddress addr = InetAddress.getByName("10.171.44.41");
        new ThreadCounter(addr);
    }
}
