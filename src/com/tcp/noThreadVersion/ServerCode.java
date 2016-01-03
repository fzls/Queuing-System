package com.tcp.noThreadVersion;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 风之凌殇 on 2016/1/3.
 */
public class ServerCode {
    public static int portNo = 3333;

    public static void main(String[] args) throws IOException {
        //create a server socket using a port no
        ServerSocket s = new ServerSocket(portNo);
        System.out.println("The Server is start: " + s);
        //wait until a client send a connect request
        Socket socket = s.accept();
        try {
            System.out.println("Accept the Client: " + socket);
            // create a buffered input stream and output stream to relieve and send the message
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            while (true) {
                //terminate until client send "byebye"
                String str = in.readLine();
                if (str.equals("byebye"))
                    break;
                //print to local console
                System.out.println("In Server received the info: " + str);
                //reply to the client
                out.println(str);
            }
        } finally {
            //when the connection terminated
            System.out.println("close the Server socket and the op.");
            socket.close();
            s.close();
        }
    }
}
