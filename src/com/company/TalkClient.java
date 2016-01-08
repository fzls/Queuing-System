package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TalkClient {

    public static void main(String[] args) {
        // write your code here
        try {
            Socket socket = new Socket("10.171.44.41", 4700);
            BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter os = new PrintWriter(socket.getOutputStream());
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String readline;
            readline = sin.readLine();
            while (!readline.equals("bye")) {
                os.print(readline);
                os.flush();
                System.out.println("Client:" + readline);
                System.out.println("Server:" + is.readLine());
                readline = sin.readLine();
            }
            os.close();
            is.close();
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
    }
}
