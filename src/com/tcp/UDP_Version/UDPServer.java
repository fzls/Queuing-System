package com.tcp.UDP_Version;

import java.io.IOException;

/**
 * Created by 风之凌殇 on 2016/1/4.
 */
public class UDPServer {
    public static void main(String args[]) throws IOException {
        System.out.println("Server Start");
        //init the ServerBean object
        ServerBean server = new ServerBean();
        //start listen
        server.listenClient();
    }
}
