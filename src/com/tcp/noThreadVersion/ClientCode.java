package com.tcp.noThreadVersion;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by 风之凌殇 on 2016/1/3.
 */
public class ClientCode {
    public static int portNo = 3333;
    static String clientName = "Mike";

    public static void main(String[] args) throws IOException {
        //create a socket using localhost as it's address
        InetAddress addr = InetAddress.getByName("localhost");
        Socket socket = new Socket(addr, portNo);
        try {
            System.out.println("socket = " + socket);
            //create the sin, in and out for IO
            BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            //send message to
            String cmd, reply;
            while (true) {
                cmd = sin.readLine();
                out.println(cmd);
                out.flush();
                if (cmd.equals("byebye"))
                    break;
                reply = in.readLine();
                System.out.println("message received from server: " + reply);
            }
        } finally {
            System.out.println("close the Client socket and the io.");
            socket.close();
        }
    }
}
