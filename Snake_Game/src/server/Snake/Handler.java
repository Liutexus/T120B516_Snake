// Handler.java class is responsible of keeping a connection with a client,
// receiving inputs and processing them accordingly.
package server.Snake;

import client.Snake.Entities.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Handler implements Runnable {
    private static Socket serverSocket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    private Player clientPlayer;
    private String clientId;
    private ArrayList<Player> players;
    private int count = 0;


    Handler(Socket serverSocket, ArrayList<Player> players) {
        // Generate, create and assign an ID for new incoming client here
        this.serverSocket = serverSocket; // Current socket object
        this.players = players; // Get all existing players
    }

    @Override
    public void run() {
        System.out.println("Connected: " + serverSocket);

        try {
            // We return data from server to the client through here
            out = new ObjectOutputStream(serverSocket.getOutputStream());
            // We listen to our client here
            in = new ObjectInputStream(serverSocket.getInputStream());

            clientId = randomId();
            out.writeObject(clientId); // Return a randomized ID to the connected client
            clientPlayer = createPlayer(clientId);
            out.writeObject(clientPlayer); // Return generated client's 'Player' object

            while (true) { // Loop to listen to client's messages
                synchronized(players) { // To safely access 'players' variable and not conflict with other threads
                    out.reset();
                    out.writeObject(players);

                    // TODO: Listen to client's messages
//                    if(in.available() != 0){
//                        Player receivedClientPlayer = (Player)in.readObject(); // ERROR: Waits for input
//                        System.out.println(receivedClientPlayer.toString());
//                        updatePlayer(receivedClientPlayer);
//                    }
                }
                try {Thread.sleep(100);} catch (Exception e) { };
            }


        } catch (Exception e) {
            System.out.println("Error:" + serverSocket);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
            System.out.println("Closed: " + serverSocket);
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
        Random random = new Random(); int min = 5; int max = 45;
        Float randX = min + random.nextFloat() * (max - min);
        Float randY = min + random.nextFloat() * (max - min);

        // This could be improved by some more fancier initial position assignment
        Player player = new Player(id, randX, randY);

        players.add(player); // Adding new client user to the players' pool

        return player;
    }
}
