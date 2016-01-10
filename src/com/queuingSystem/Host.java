package com.queuingSystem;

import com.queuingSystem.sounds.MusicPlayer;

import java.io.*;
import java.net.InetAddress;
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
    //    private static final int ALREADY_SERVEED = 4;
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
    //player
    private MusicPlayer musicPlayer = new MusicPlayer();
    private String[] digits = new String[]{
            "src/com/queuingSystem/sounds/0.wav",
            "src/com/queuingSystem/sounds/1.wav",
            "src/com/queuingSystem/sounds/2.wav",
            "src/com/queuingSystem/sounds/3.wav",
            "src/com/queuingSystem/sounds/4.wav",
            "src/com/queuingSystem/sounds/5.wav",
            "src/com/queuingSystem/sounds/6.wav",
            "src/com/queuingSystem/sounds/7.wav",
            "src/com/queuingSystem/sounds/8.wav",
            "src/com/queuingSystem/sounds/9.wav",
            "src/com/queuingSystem/sounds/ten.wav",
            "src/com/queuingSystem/sounds/hundred.wav",
            "src/com/queuingSystem/sounds/thousand.wav",
            "src/com/queuingSystem/sounds/tenThousand.wav",

    };
    private String please = "src/com/queuingSystem/sounds/please.wav";
    private String customer = "src/com/queuingSystem/sounds/customer.wav";
    private String desk = "src/com/queuingSystem/sounds/desk.wav";


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
                        out.println("Add desk " + clientId);
                        out.println(customers.size());
                        System.out.println("Add desk " + clientId);
                        break;

                    case "stop counter":
                        if (isIdle) {
                            availableCounters.setElementAt(STOPPED, clientId);
                            //stop and remove corresponding Updater <风之凌殇>
                            PrintWriter _out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(counters.get(clientId).getOutputStream())), true);
                            _out.println("stop");

                            counters.remove(clientId);
                            System.out.println("Desk " + clientId + " is stopped and still have " + customers.size() + " customers in queue");
                            out.println("success");
                        } else {
                            out.println("本座位的顾客仍在就餐中，请稍后再撤离座位。");
                        }

                        break;

                    case "start server the client":
                        if (isIdle) {
                            if (!customers.isEmpty()) {
                                customerId = customers.pop();
                                System.out.println("Desk " + clientId + " is now serving for customer " + customerId);
                                out.println(customerId);
                                playNotice(customerId, clientId);
                                //send new queue size to all the in-use Updater
                                fireUpdater();
                                availableCounters.setElementAt(IN_USE, clientId);
                                isIdle = false;
                            } else {
                                out.println("后面已无人等待，无人可以入座。");
                            }
                        } else {
                            out.println("当前座位有人，新客户无法入座。");
                        }
                        break;

                    case "finish serve client":
                        if (!isIdle) {
                            out.println(customerId);
                            System.out.println("customer " + customerId + " finish the service" + " in counter " + clientId);
                            availableCounters.setElementAt(NOT_IN_USE, clientId);
                            isIdle = true;
                        } else
                            out.println("当前座位无人。");
                        break;

				/* receive from ticket machine */
                    case "new ticket machine":
                        clientId = ++ticketMachineSerializer;
                        availableTicketMachines.add(OPENING);
                        out.println("Add ticket machines " + clientId);
                        out.println(customers.size());
                        System.out.println("Add ticket machines " + clientId);
                        break;

                    case "stop ticket machine":
                        availableTicketMachines.setElementAt(STOPPED, clientId);
                        PrintWriter _out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(ticketMachines.get(clientId).getOutputStream())), true);
                        _out.println("stop");
                        ticketMachines.remove(clientId);
                        System.out.println("Ticket machine " + clientId + " is stopped and still have " + customers.size() + " customers in queue");
                        break;

                    case "new customer":
                        customers.add(++customerSerializer);
                        out.println("customer id : " + customerSerializer);
                        fireUpdater();
                        System.out.println("customer " + customerSerializer + " is enqueue");
                        break;

				/* update queue size */
                    case "current queue size":
                        out.println(customers.size());
                        break;

                    case "new counter updater":
                        counters.put(counterSerializer, clientSocket);
                        break;

                    case "new ticketMachine updater":
                        ticketMachines.put(ticketMachineSerializer, clientSocket);
                        break;

                    case "stop updater":
                        break;

				/* if program works normally, this would not be invoked */
                    default:
                        out.println("wrong command");
                        break;
                }
                if (isIdle && (cmd.equals("stop counter") || cmd.equals("stop ticket machine") || cmd.equals("stop updater")))
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

    private void playNotice(int customerId, int deskId) {
        String _customerId = Integer.toString(customerId);
        String _deskId = Integer.toString(deskId);
        try {
            musicPlayer.play(please);
            playId(_customerId);
            musicPlayer.play(customer);
            playId(_deskId);
            musicPlayer.play(desk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playId(String Id) {
        //0~99999 can be pronounced correctly
        try {
            //deal with numbers like 10000, 1900 which ending with several successive zeros
            int endingZeros = 0, index = Id.length() - 1;
            while (index >= 0 && Id.charAt(index) == '0') {
                endingZeros++;
                index--;
            }
            for (int i = 0; i < Id.length(); ++i) {
                //deal with 10~19
                if (Id.length() == 2 && Id.charAt(0) == '1') {
                    musicPlayer.play(digits[10]);
                    if (Id.charAt(1) != '0')
                        musicPlayer.play(digits[Id.charAt(1) - '0']);
                    break;
                }
                //deal with successive zeros that not in the middle by reading only the last one
                if (i <= Id.length() - 2 && Id.charAt(i) == '0' && Id.charAt(i + 1) == '0')
                    continue;
                else
                    musicPlayer.play(digits[Id.charAt(i) - '0']);
                //pronounce 百，千，万 etc.
                int _digit = Id.length() - i + 8;
                if (Id.charAt(i) != '0' && _digit >= 10)
                    musicPlayer.play(digits[_digit]);
                //if comes to ending zero area, stop.
                if (i + endingZeros + 1 == Id.length())
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fireUpdater() {
        for (int i = 0; i < availableCounters.size(); ++i)
            if (availableCounters.get(i) != STOPPED) {
                try {
                    PrintWriter _out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(counters.get(i).getOutputStream())), true);
                    _out.println(customers.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        for (int i = 0; i < availableTicketMachines.size(); ++i) {
            if (availableTicketMachines.get(i) != STOPPED)
                try {
                    PrintWriter _out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(ticketMachines.get(i).getOutputStream())), true);
                    _out.println(customers.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }
}

public class Host {
    //port
    private static int portNo = 2333;
    private static LinkedList<Integer> customers = new LinkedList<>();
    private static Vector<Integer> availableCounters = new Vector<>();
    private static Vector<Integer> availableTicketMachines = new Vector<>();
    private static HashMap<Integer, Socket> counters = new HashMap<>();
    private static HashMap<Integer, Socket> ticketMachines = new HashMap<>();

    public static void main(String[] args) throws IOException {
        //server side socket
        System.out.println("enter you local port number, better larger than 1024, and it should be no larger than 65535 :)");
        Scanner in = new Scanner(System.in);
        portNo = in.nextInt();
        ServerSocket s = new ServerSocket(portNo);
        System.out.println("The Server is start");
        InetAddress local = InetAddress.getLocalHost();
        System.out.println("Hostname    : " + local.getHostName());
        System.out.println("HostAddress : " + local.getHostAddress() + ":" + portNo);
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
