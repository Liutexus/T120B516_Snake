// Handler.java class is responsible of keeping a connection with a client,
// receiving inputs and processing them accordingly.
package server.Snake;

import client.Snake.Entities.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Handler implements Runnable {
    private static Socket serverSocket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    private Player clientPlayer;
    private String clientId;
    private ArrayList<Player> players;


    Handler(Socket serverSocket, ArrayList<Player> players) {
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
            Listener clientListener = new Listener(in, players);
            clientId = randomId();
            out.writeObject(clientId); // Return a randomized ID to the connected client
            clientPlayer = createPlayer(clientId);
            out.writeObject(clientPlayer); // Return generated client's 'Player' object

            while (true) { // Loop to listen to client's messages
                synchronized(players) { // To safely access 'players' variable and not conflict with other threads
                    out.reset();
                    out.writeObject(players);
                    clientListener.run();
                }

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
        for (Player player: players)
            if(player.getId() == id) {
                System.out.println("Player already exists.");
                return null;
            }
        int randX = ThreadLocalRandom.current().nextInt(5, 45);
        int randY = ThreadLocalRandom.current().nextInt(5, 45);

        // This could be improved by some more fancier initial position assignment
        Player player = new Player(id, randX, randY);

        players.add(player); // Adding new client user to the players' pool

        return player;
    }

//    private void updatePlayer(Player player) {
//        synchronized(players) {
//            for(int i = 0; i < players.size(); i++)
//                if(players.get(i).getId().compareTo(player.getId()) == 0)
//                    players.set(i, player);
//        }
//    }

    private void updateDirection(String id, float x, float y) {
        synchronized(players) {
            for(int i = 0; i < players.size(); i++)
                if(players.get(i).getId().compareTo(id) == 0)
                    players.get(i).setMoveDirection(x, y);
        }
    }

    // Client listener class
    private class Listener implements Runnable {
        ObjectInputStream in;

        ArrayList<Player> players;

        public Listener(ObjectInputStream in, ArrayList<Player> players) {
            this.in = in;
            this.players = players;
        }

        @Override
        public void run() {
            // Listen to client's messages
            try {
                if (in.available() != 0) {
                    in.readByte();
                    Player receivedClientPlayer = (Player) in.readUnshared();
                    float[] directions = receivedClientPlayer.getMoveDirection();
//                    updatePlayer(receivedClientPlayer); // <- This lags as hell (Leaving it here for a bit to remember my mistakes)
                    updateDirection(receivedClientPlayer.getId(), directions[0], directions[1]);
                }
            } catch (Exception e) {
                System.out.println("Error at reading client's messages");
                e.printStackTrace();
            }
        }
    }
}
