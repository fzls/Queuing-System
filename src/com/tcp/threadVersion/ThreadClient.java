package com.tcp.threadVersion;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by 风之凌殇 on 2016/1/3.
 */
public class ThreadClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        int threadNo = 0;
//        InetAddress addr = InetAddress.getByName("localhost");
        InetAddress addr = InetAddress.getByName("10.171.44.41");
        for (threadNo = 0; threadNo < 3; ++threadNo) {
            new ClientThreadCode(addr);
        }
    }
}

class ClientThreadCode extends Thread {
    //thread count, used for serialise numbering the thread
    private static int cnt = 0;
    //client side socket
    private Socket socket;
    private int clientId = cnt++;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader sin;

    //constructor
    public ClientThreadCode(InetAddress addr) {
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
            String cmd = "Hello Server,My id is " + clientId;
            out.println(cmd);
            String reply = in.readLine();
            System.out.println(reply);
            for (; ; ) {
                cmd = sin.readLine();
                out.println(cmd);
                if (cmd.equals("byebye"))
                    break;
                reply = in.readLine();
                System.out.println(reply);
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