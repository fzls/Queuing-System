package com.tcp.threadVersion;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 风之凌殇 on 2016/1/3.
 */
class ServerThreadCode extends Thread {
    //socket for the client
    private Socket clientSocket;
    //IO handle
    private BufferedReader sin;
    private PrintWriter sout;

    //default constructor
    public ServerThreadCode() {

    }

    public ServerThreadCode(Socket s) throws IOException {
        clientSocket = s;
        //init the handle of sin and sout
        sin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        sout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
        //start the thread
        start();
    }

    // the main body function of the thread running
    public void run() {
        try {
            for (; ; ) {
                String cmd = sin.readLine();
                if (cmd.equals("byebye"))
                    break;
                System.out.println("In Server reveived the info: " + cmd);
                sout.println(cmd);
            }
            System.out.println("closing the server socket!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("close the Server socket and the io.");
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class ThreadServer {
    //port
    static final int portNo = 3333;

    public static void main(String[] args) throws IOException {
        //server side socket
        ServerSocket s = new ServerSocket(portNo);
        System.out.println("The Server is start: " + s);
        try {
            for (; ; ) {
                Socket socket = s.accept();
                new ServerThreadCode(socket);
            }
        } finally {
            s.close();
        }
    }


}
