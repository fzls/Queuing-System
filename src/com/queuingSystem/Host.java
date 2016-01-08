package com.queuingSystem;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Vector;


/**
 * Created by 风之凌殇 on 2016/1/8.
 */
class ServerThreadCode extends Thread {
    /* STATE */
    private static final int STOPPED = 0;
    private static final int OPENING = 1;
    private static final int NOT_IN_USE = 2;
    private static final int IN_USE = 3;
    private static final int ALREADY_SERVEED = 4;
    private static int counterSerializer = -1;
    private static int ticketMachineSerializer = -1;
    private static int customerSerializer = -1;
    private LinkedList<Integer> customers;
    private Vector<Integer> availableCounters;
    private Vector<Integer> availableTicketMachines;
    private HashMap<Integer, Socket> counters;
    private HashMap<Integer, Socket> ticketMachines;
    private boolean isIdle = true;
    //client id
    private int clientId;
    //the customer that this client is severed if this is a counter client
    private int customerId;
    //socket for the client
    private Socket clientSocket;
    //IO handle
    private BufferedReader in;
    private PrintWriter out;

    public ServerThreadCode(Socket s, LinkedList<Integer> _customers, Vector<Integer> _availableCounters, Vector<Integer> _availableTicketMachines, HashMap<Integer, Socket> _counters, HashMap<Integer, Socket> _ticketMachines) throws IOException {
        customers = _customers;
        availableCounters = _availableCounters;
        availableTicketMachines = _availableTicketMachines;
        counters = _counters;
        ticketMachines = _ticketMachines;
        clientSocket = s;
        //init the handle of in and out
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
        //start the thread
        start();
    }

    // the main body function of the thread running
    public void run() {
        try {
            for (; ; ) {
                String cmd = in.readLine();
                switch (cmd) {
                    /* receive from counter client */
                    case "new counter":
                        clientId = ++counterSerializer;
                        availableCounters.add(NOT_IN_USE);
                        counters.put(clientId, clientSocket);
                        out.println("Add counter " + clientId);
                        out.println(customers.size());
                        System.out.println("Add counter " + clientId);
                        break;

                    case "stop counter":
                        if (isIdle) {
                            availableCounters.setElementAt(STOPPED, clientId);
                            counters.remove(clientId);
                            System.out.println("Counter " + clientId + " is stopped");
                            out.println("success");
                        } else {
                            out.println("your current customer has not been served, please leave after you finish it");
                        }

                        break;

                    case "start server the client":
                        //若队列中有客户，则分配队列中的客户给该柜台
                        if (isIdle) {
                            if (!customers.isEmpty()) {
                                customerId = customers.pop();
                                System.out.println("Counter " + clientId + " start serving for customer " + customerId);
                                out.println(customerId);
                                out.println(customers.size());

                                availableCounters.setElementAt(IN_USE, clientId);
                                isIdle = false;
                            } else {
                                out.println("WARNING : there is no client in the queue");
                            }
                        } else {
                            out.println("WARNING : current customer has not finish service");
                        }
                        break;

                    case "finish serve client":
                        if (!isIdle) {
                            out.println(customerId);
                            System.out.println("customer " + customerId + " finish the service" + " in counter " + clientId);
                            availableCounters.setElementAt(NOT_IN_USE, clientId);
                            isIdle = true;
                        } else
                            out.println("WARNING : no customer in service");
                        break;

                    /* receive from ticket machine */
                    case "new ticket machine":
                        clientId = ++ticketMachineSerializer;
                        availableTicketMachines.add(OPENING);
                        ticketMachines.put(clientId, clientSocket);
                        out.println("Add ticket machines " + clientId);
                        out.println(customers.size());
                        System.out.println("Add ticket machines " + clientId);
                        break;

                    case "stop ticket machine":
                        availableTicketMachines.setElementAt(STOPPED, clientId);
                        ticketMachines.remove(clientId);
                        System.out.println("Ticket machine " + clientId + " is stopped");
                        break;

                    case "new customer":
                        customers.add(++customerSerializer);
                        out.println("customer id : " + customerSerializer);
                        out.println(customers.size());
                        System.out.println("customer " + customerSerializer + " is enqueue");
                        break;

                    //update queue size
                    case "current queue size":
                        out.println(customers.size());
                        break;
                    //if program works normally, this would not be invoked
                    default:
                        out.println("wrong command");
                        break;
                }
                if (isIdle && (cmd.equals("stop counter") || cmd.equals("stop ticket machine")))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Host {
    //TODO ip and portNo should be set in the UI
    //port
    private static int portNo = 2333;
    private static LinkedList<Integer> customers = new LinkedList<>();
    private static Vector<Integer> availableCounters = new Vector<>();
    private static Vector<Integer> availableTicketMachines = new Vector<>();
    private static HashMap<Integer, Socket> counters = new HashMap<>();
    private static HashMap<Integer, Socket> ticketMachines = new HashMap<>();

    public static void main(String[] args) throws IOException {
        //server side socket
        System.out.println("enter you local port number");
        Scanner in = new Scanner(System.in);
        portNo = in.nextInt();
        ServerSocket s = new ServerSocket(portNo);
        System.out.println("The Server is start: " + s);
        try {
            for (; ; ) {
                Socket socket = s.accept();
                new ServerThreadCode(socket, customers, availableCounters, availableTicketMachines, counters, ticketMachines);
            }
        } finally {
            s.close();
        }
    }


}