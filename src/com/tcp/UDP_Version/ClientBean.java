package com.tcp.UDP_Version;

import java.io.IOException;
import java.net.*;

/**
 * Created by 风之凌殇 on 2016/1/3.
 */
public class ClientBean {
    //describe the DatagramSocket object of the UDP communication
    private DatagramSocket ds;
    //used for encapsulate the communication Strings
    private byte buffer[];
    //the port No of the client
    private int clientport;
    //the port No of the server
    private int serverport;
    //the content of the communication
    private String content;
    //describe the communication address
    private InetAddress ia;

    public ClientBean() throws SocketException, UnknownHostException {
        buffer = new byte[1024];
        clientport = 1985;
        serverport = 1986;
        content = "";
        ds = new DatagramSocket(clientport);
        ia = InetAddress.getByName("localhost");
    }

    //below is the Getter and Setter of the attributes
    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public int getClientport() {
        return clientport;
    }

    public void setClientport(int clientport) {
        this.clientport = clientport;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DatagramSocket getDs() {
        return ds;
    }

    public void setDs(DatagramSocket ds) {
        this.ds = ds;
    }

    public InetAddress getIa() {
        return ia;
    }

    public void setIa(InetAddress ia) {
        this.ia = ia;
    }

    public int getServerport() {
        return serverport;
    }

    public void setServerport(int serverport) {
        this.serverport = serverport;
    }

    public void sendToServer() throws IOException {
        buffer = content.getBytes();
        ds.send(new DatagramPacket(buffer, content.length(), ia, serverport));
    }

}
