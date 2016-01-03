package com.tcp;

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

            out.println("Hello Server,I am " + clientName);
            String readline, str;
            while (true) {
                str = in.readLine();
                System.out.println("message received from server: " + str);
                readline = sin.readLine();
                out.println(readline);
                out.flush();
                if (readline.equals("byebye"))
                    break;
            }
        } finally {
            System.out.println("close the Client socket and the io.");
            socket.close();
        }
    }
}
