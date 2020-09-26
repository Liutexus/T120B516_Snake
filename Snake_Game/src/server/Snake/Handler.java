// Handler.java class is responsible of keeping a connection with a client,
// receiving inputs and processing them accordingly.
package server.Snake;

import client.Snake.Entities.Player;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Handler implements Runnable {
    private Socket serverSocket;
    public static GameLogic gameLogic;
    private OutputStream out;
    private InputStream in;

    private Player clientPlayer;
    private String clientId;
    private Map<String, Player> players = new ConcurrentHashMap<>();

    Handler(Socket serverSocket, GameLogic gameLogic) {

        this.serverSocket = serverSocket; // Current socket object
        this.gameLogic = gameLogic;
        this.players = gameLogic.getPlayers();

        try {
            // We return data from server to the client through here
            out = serverSocket.getOutputStream();
            // We listen to our client here
            in = serverSocket.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Connected: " + serverSocket);

//        Listener clientListener = new Listener(in);
        Sender clientSender = new Sender(new OutputStreamWriter(out, StandardCharsets.UTF_8));

        try {
            clientSender.sendClientLogin();
            while (true){
//                clientSender.run(); // Sending packets to the client
                try {Thread.sleep(100);} catch (Exception e) { };
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error:" + serverSocket);
        }
    }

    public void updatePlayersMap(Map<String, Player> players) {
        this.players = players;
//        this.notify();
    }

    // Some utilities
    // Used to generate a random ID for current client
    private String randomId() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

//        System.out.println(generatedString);
        return generatedString;
    }

    private Player createPlayer(String id) {
        if(players.containsKey(id)) {
            System.out.println("Player already exists.");
            return null;
        }
        int randX = ThreadLocalRandom.current().nextInt(5, 45);
        int randY = ThreadLocalRandom.current().nextInt(5, 45);
        // This could be improved by some more fancier initial position assignment
        Player player = new Player(id, randX, randY);

        gameLogic.addPlayer(player);
//        players.put(id, player); // Adding new client user to the players' pool
        return player;
    }

    private void updateDirection(String id, float x, float y) {
        synchronized(players) {
            players.get(id).setMoveDirection(x, y);
        }
    }

    // Client listener class
    private class Listener implements Runnable {
        ObjectInputStream in;

        public Listener(ObjectInputStream in) {
            this.in = in;
        }

        @Override
        public void run() {
            // Listen to client's messages
            try {
                if (in.available() != 0) {
                    in.readByte();
                    Player receivedClientPlayer = (Player) in.readUnshared();
                    float[] directions = receivedClientPlayer.getMoveDirection();
                    updateDirection(receivedClientPlayer.getId(), directions[0], directions[1]);
                }
            } catch (Exception e) {
                System.out.println("Error at reading client's messages");
                e.printStackTrace();
            }
        }
    }

    // Client sender class
    private class Sender implements Runnable {
        private OutputStreamWriter out;

        public Sender(OutputStreamWriter out) {
            this.out = out;
        }

        @Override
        public void run() {
            try {
                long start = System.currentTimeMillis(); // Benchmarking

                players.forEach((key, value) -> {
                    try {
                        out.write(value.toString());
                        out.flush();
                    } catch (Exception e) {

                    }
                });

                System.out.println("Milliseconds passed: " + (System.currentTimeMillis() - start));
//                try {Thread.sleep(100);} catch (Exception e) { };

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error sending a packet to the client.");
            }
        }

//        private void sendUnsharedPacket(Object packet) throws IOException {
//
//            out.writeUnshared(packet); // Return a randomized ID to the connected client
//        }

        public void sendClientLogin() throws IOException {
            clientId = randomId();
            out.write(clientId); // Return a randomized ID to the connected client
            out.flush();
            clientPlayer = createPlayer(clientId);
            out.write(clientPlayer.toString()); // Return generated client's 'Player' object
            out.flush();
        }



    }
}
