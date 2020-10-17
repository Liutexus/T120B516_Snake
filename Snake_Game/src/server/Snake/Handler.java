// Handler.java class is responsible of keeping a connection with a client,
// receiving inputs and processing them accordingly.
package server.Snake;

import client.Snake.Player;
import server.Snake.Interface.IObserver;
import server.Snake.Packet.EPacketHeader;
import server.Snake.Packet.Packet;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Handler implements Runnable, IObserver {
    private Socket serverSocket;
    private MatchInstance match;
    private GameLogic gameLogic;
    private OutputStream out;
    private InputStream in;

    private Listener clientListener;
    private Sender clientSender;

    private Player clientPlayer;
    private String clientId;
    public Map<String, Player> players = new ConcurrentHashMap<>();

    public Handler(Socket serverSocket) {
        this.serverSocket = serverSocket; // Current socket object

        try {
            // We return data from server to the client through here
            out = serverSocket.getOutputStream();
            // We listen to our client here
            in = serverSocket.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        clientListener = new Listener(new InputStreamReader(in));
        clientSender = new Sender(new OutputStreamWriter(out, StandardCharsets.UTF_8));
    }

    public Handler(Socket serverSocket, GameLogic gameLogic, Map players) {
        this.serverSocket = serverSocket; // Current socket object
        this.gameLogic = gameLogic;
        this.players = players;

        try {
            // We return data from server to the client through here
            out = serverSocket.getOutputStream();
            // We listen to our client here
            in = serverSocket.getInputStream();
        } catch (Exception e) {
//            e.printStackTrace();
        }

        clientListener = new Listener(new InputStreamReader(in));
        clientSender = new Sender(new OutputStreamWriter(out, StandardCharsets.UTF_8));
    }

    public void setMatchInstance(MatchInstance match) {
        this.match = match;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public void setPlayers(Map players) {
        this.players = players;
    }

    public void sendLoginInfo(String id, Player player) {
        this.clientSender.sendClientLogin(id, player);
    }

    public void sendPacket(EPacketHeader header, String packet) {
        this.clientSender.sendPacket(header, packet);
    }

    @Override
    public void run() {
        System.out.println("Connected: " + serverSocket);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            executor.execute(clientListener);
            while (true) {
                if(serverSocket.isClosed()) break;
//                long start = System.currentTimeMillis(); // Benchmarking
                executor.execute(clientSender); // Sending packets to the client
//                System.out.println("Milliseconds passed: " + (System.currentTimeMillis() - start));
                try {Thread.sleep(100);} catch (Exception e) { };
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Error:" + serverSocket);
        }
    }

    @Override
    public void update() {

    }

    // --- Client listener class ---
    private class Listener implements Runnable {
        InputStreamReader in;

        public Listener(InputStreamReader in) {
            this.in = in;
        }

        private void parsePacket(String packetJson){
            Packet packet = new Packet(packetJson);
            switch (packet.header){
                case EMPTY:
                    break;
                case CLIENTRESPONSE:
                    gameLogic.updatePlayerField(packet.parseBody());
                    break;
                default:
                    System.out.println("Error. Not recognised packet header '" + packet.header.toString() + "'. ");
                    break;
            }
        }

        @Override
        public void run() {
            BufferedReader inb = new BufferedReader(in);
            while(true) {
                try {
                    parsePacket(inb.readLine());
                } catch (Exception e) {
                    if(serverSocket.isClosed()) break;
                    System.out.println("Couldn't receive packet from the client.");
                    if(e instanceof SocketException) {
                        try {
                            serverSocket.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        match.unregisterObserver(Handler.this);
                        break;
                    }
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    // --- Client sender class ---
    private class Sender implements Runnable {
        private OutputStreamWriter out;

        public Sender(OutputStreamWriter out) {
            this.out = out;
        }

        @Override
        public void run() {
            try {
                synchronized (players){
                    players.forEach((key, value) -> {
                        sendPacket(EPacketHeader.PLAYER, value.toString());
                    });
                }
            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println("Error sending a packet to the client.");
            }
        }

        public void sendClientLogin(String id, Player player) {
            clientId = id;
            sendPacket(EPacketHeader.ID, clientId);

            clientPlayer = player;
            sendPacket(EPacketHeader.CLIENTPLAYER, clientPlayer.toString());
        }

        private void sendPacket(EPacketHeader header, String body) {
            BufferedWriter bfw = new BufferedWriter(out);
            try {
                Packet packet = new Packet(header, body);
                bfw.write(packet.toString());
                bfw.flush();
            } catch (Exception e) {
                System.out.println("Error sending packet to client.");
                if(e instanceof SocketException) {
                    try {
                        serverSocket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    match.unregisterObserver(Handler.this);
                }
                e.printStackTrace();
            }
        }

    }
}
