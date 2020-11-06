// Handler.java class is responsible of keeping a connection with a client,
// receiving inputs and processing them accordingly.
package server.Snake;

import server.Snake.Builder.HandlerBuilder;
import server.Snake.Entity.Player;
import server.Snake.Entity.Entity;
import server.Snake.Enumerator.EClientStatus;
import server.Snake.Enumerator.EPacketHeader;
import server.Snake.Packet.Packet;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Handler implements Runnable {
    private Socket serverSocket;
    private MatchInstance match;
    private GameLogic gameLogic;
    private HandlerBuilder builder;
    private OutputStream out;
    private InputStream in;

    private Listener clientListener;
    private Sender clientSender;

    private EClientStatus status;

    private Player clientPlayer;
    private String clientId;
    public Map<String, Player> players = new ConcurrentHashMap<>();
    public Map<String, Entity> terrainEntities = new ConcurrentHashMap<>();

    public Handler(){}

    public Handler(Socket serverSocket) {
        this.serverSocket = serverSocket; // Current socket object
        this.status = EClientStatus.MENU;

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

    public void setServerSocket(Socket serverSocket){
        this.serverSocket = serverSocket; // Current socket object
        this.status = EClientStatus.MENU;

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

    public void setMatchInstance(MatchInstance match) {
        this.match = match;
    }

    public void setStatus(EClientStatus status){
        this.status = status;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public void setBuilder(HandlerBuilder builder){
        this.builder = builder;
    }

    public void setPlayers(Map players) {
        this.players = players;
    }

    public void setTerrainEntities(Map terrainEntities) {
        this.terrainEntities = terrainEntities;
    }

    public void sendLoginInfo(String id, Player player) {
        this.clientSender.sendClientLogin(id, player);
        this.status = EClientStatus.IN_GAME;
    }

    public void sendPacket(EPacketHeader header, String packet) {
        try {
            this.clientSender.sendPacket(header, packet);
        } catch (IOException e) {
            System.out.println("Error sending a packet to the client.");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Connected: " + serverSocket);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            executor.execute(clientListener); // Listening to packets from client
            executor.execute(clientSender); // Sending packets to the client

            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            System.out.println("Error:" + serverSocket);
            e.printStackTrace();
        }

        this.match.unregisterObserver(this.builder);
        System.out.println("Disconnected: " + serverSocket);
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
                case CLIENT_RESPONSE:
                    gameLogic.updatePlayerField(packet.parseBody());
                    break;
                case CLIENT_MATCH_REQUEST:
                    Handler.this.status = EClientStatus.LOBBY;
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
//                    System.out.println("Couldn't receive packet from the client.");
                    if(e instanceof SocketException) {
                        break;
                    }
//                    e.printStackTrace();
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
            final boolean[] alive = {true};
            while(alive[0]){
                if(Handler.this.status == EClientStatus.IN_GAME){ // If the player is in game
                    synchronized (players){
                        players.forEach((key, value) -> { // Send all other match players
                            try {
                                sendPacket(EPacketHeader.PLAYER, value.toString());
                            } catch (IOException e) {
                                alive[0] = false;
                                System.out.println("Client disconnected");
//                                e.printStackTrace();
                            }
                        });
                    }
                    if(!alive[0]) break;
                    synchronized (terrainEntities){
                        terrainEntities.forEach((key, value) -> { // Send all other match entities
                            try {
                                sendPacket(EPacketHeader.ENTITY, value.toString());
                            } catch (IOException e) {
                                alive[0] = false;
                                System.out.println("Error sending a packet to the client.");
//                                e.printStackTrace();
                            }
                        });
                    }
                }
                try {Thread.sleep(100);} catch (Exception e) { };
            }
        }

        public void sendClientLogin(String id, Player player) {
            try{
                clientId = id;
                sendPacket(EPacketHeader.ID, clientId);

                clientPlayer = player;
                sendPacket(EPacketHeader.CLIENT_PLAYER, clientPlayer.toString());

            } catch (Exception e) {
                System.out.println("Error sending login info to the client.");
//                e.printStackTrace();
            }
        }

        private void sendPacket(EPacketHeader header, String body) throws IOException {
            BufferedWriter bfw = new BufferedWriter(out);
            Packet packet = new Packet(header, body);
            bfw.write(packet.toString());
            bfw.flush();
        }

    }
}
