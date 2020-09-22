// Handler.java class is responsible of keeping a connection with a client,
// receiving inputs and processing them accordingly.
package server.Snake;

import client.Snake.Entities.Player;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Handler implements Runnable {
    private Socket serverSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Player clientPlayer;
    private String clientId;
    public static Map<String, Player> players;

    Handler(Socket serverSocket, Map players) {
        // Generate, create and assign an ID for new incoming client here
        this.serverSocket = serverSocket; // Current socket object
        this.players = players; // Get all existing players

        try {
            // We return data from server to the client through here
            out = new ObjectOutputStream(serverSocket.getOutputStream());
            // We listen to our client here
            in = new ObjectInputStream(serverSocket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Connected: " + serverSocket);

        try {
            Listener clientListener = new Listener(in);
            Sender clientSender = new Sender(out);
            clientId = randomId();
            out.writeUnshared(clientId); // Return a randomized ID to the connected client
            clientPlayer = createPlayer(clientId);
            out.writeUnshared(clientPlayer); // Return generated client's 'Player' object

            while (true) { // Loop to listen to client's messages
                long start = System.currentTimeMillis(); // Benchmarking
                synchronized(players) { // To safely access 'players' variable and not conflict with other threads
                    out.reset(); // <- FIX: Lags as hell

                    out.writeObject(players);
                }
                clientListener.run(); // Listening to client's messages
                System.out.println("Milliseconds passed: " + (System.currentTimeMillis() - start));
                try {Thread.sleep(100);} catch (Exception e) { };
            }

        } catch (Exception e) {
            System.out.println("Error:" + serverSocket);
        }
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
        players.put(id, player); // Adding new client user to the players' pool
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
        ObjectOutputStream out;

        public Sender(ObjectOutputStream out) {
            this.out = out;
        }

        @Override
        public void run() {


            // TODO: Send a packet and receive one for "PING"

        }
    }
}
