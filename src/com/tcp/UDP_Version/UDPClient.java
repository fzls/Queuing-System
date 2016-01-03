package com.tcp.UDP_Version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by 风之凌殇 on 2016/1/3.
 */
public class UDPClient implements Runnable {
    public static String content;
    public static ClientBean client;

    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        client = new ClientBean();
        System.out.println("Client Start");
        while (true) {
            //get input
            content = br.readLine();
            //if end or null, terminate the loop
            if (content == null || content.equalsIgnoreCase("end") || content.equalsIgnoreCase(""))
                break;
            new Thread(new UDPClient()).start();
        }
    }

    public void run() {
        try {
            client.setContent(content);
            client.sendToServer();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
