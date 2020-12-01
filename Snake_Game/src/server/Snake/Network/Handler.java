// Handler.java class is responsible of keeping a connection with a client,
// receiving inputs and processing them accordingly.
package server.Snake.Network;

import server.Snake.Entity.Builder.HandlerBuilder;
import server.Snake.Entity.Player;
import server.Snake.Entity.Entity;
import server.Snake.Enumerator.EClientStatus;
import server.Snake.Enumerator.EPacketHeader;
import server.Snake.GameLogic;
import server.Snake.Interface.IHandler;
import server.Snake.MatchInstance;
import server.Snake.Network.Packet.Packet;
import server.Snake.Server;

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

    private HandlerListener clientListener;
    private HandlerSender clientSender;

    private EClientStatus status;

    private ExecutorService executor = Executors.newFixedThreadPool(2);;

    private String clientId;
    private Player clientPlayer;
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

        clientListener = new HandlerListener(this, new InputStreamReader(in));
        clientSender = new HandlerSender(this, new OutputStreamWriter(out, StandardCharsets.UTF_8));
    }

    public String getClientId(){
        return this.clientId;
    }

    public void setClientId(String id){
        this.clientId = id;
    }

    public Player getClientPlayer(){
        return this.clientPlayer;
    }

    public void setClientPlayer(Player player){
        this.clientPlayer = player;
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

        clientListener = new HandlerListener(this, new InputStreamReader(in));
        clientSender = new HandlerSender(this, new OutputStreamWriter(out, StandardCharsets.UTF_8));
    }

    public Socket getServerSocket(){
        return this.serverSocket;
    }

    public void setMatchInstance(MatchInstance match) {
        this.match = match;
    }

    public MatchInstance getMatchInstance(){
        return this.match;
    }

    public void setStatus(EClientStatus status){
        this.status = status;
    }

    public EClientStatus getStatus(){
        return this.status;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public GameLogic getGameLogic(){
        return this.gameLogic;
    }

    public void setBuilder(HandlerBuilder builder){
        this.builder = builder;
    }

    public HandlerBuilder getBuilder(){
        return this.builder;
    }

    public void setPlayers(Map players) {
        this.players = players;
    }

    public Map getPlayers(){
        return this.players;
    }

    public void setTerrainEntities(Map terrainEntities) {
        this.terrainEntities = terrainEntities;
    }

    public Map getTerrainEntities(){
        return this.terrainEntities;
    }

    public void sendLoginInfo(String id, Player player) {
        if(this.clientSender != null){
            this.clientSender.sendClientLogin(id, player);
            this.status = EClientStatus.IN_GAME;
        }
    }

    public void sendPacket(EPacketHeader header, String packet) {
        if(this.clientSender != null){
            try {
                this.clientSender.sendPacket(header, packet);
            } catch (IOException e) {
                System.out.println("Error sending a packet to the client.");
                e.printStackTrace();
            }
        }
    }

    public void shutdown(){
        try {
            this.status = EClientStatus.DISCONNECTED;
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Connected: " + serverSocket);
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

}
