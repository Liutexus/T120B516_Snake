package server.Snake;

import client.Snake.Entities.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Handler implements Runnable {
    private Socket serverSocket;
    private ArrayList<Player> players;
    private int count = 0;


    Handler(Socket serverSocket, ArrayList<Player> players, int count) {
        // TODO: Generate, create and assign an ID for new incoming client here
        this.serverSocket = serverSocket; // Current socket object
        this.players = players; // Get all existing players
        this.count = count;

    }

    private boolean AddPlayerToGame(Player newPlayer){
        if(players.size() == 4){
            return  false;
        }
        return true;
    }

    @Override
    public void run() {
        System.out.println("Connected: " + serverSocket);

        try {
            // TODO: Catch incoming inputs and update data on server here
            // We return data from server to the client through here
            var out = new ObjectOutputStream(serverSocket.getOutputStream());
            // We listen to our client here
            var in = new ObjectInputStream(serverSocket.getInputStream());

            String clientId = randomId();
            out.writeObject(clientId); // Return a randomized ID to the connected client
            Player clientPlayer = createPlayer(clientId);
            out.writeObject(clientPlayer); // Return generated client's 'Player' object

            // TODO: Logic which processes client's inputs goes here
            // TODO: Add loop which listens for client's messages here
            //synchronized(sync_object){} // Synchronize data
            while (true) { // Main server loop
                out.reset();
                movePlayer(clientId);
                out.writeObject(players);
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

    private void parseInput(String line){
        // TODO: Add listener's commands to change server's data
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

        Player player = new Player(id);
        // TODO: Assign a nice position for new player so all players are nicely places on the map
        players.add(player);

        return player;
    }

    private boolean movePlayer(String id) {
        for (int i = 0; i < players.size(); i++){
            if(players.get(i).getId() == id){
                players.get(i).setMoveDirection(1, 1);
                players.get(i).movePlayer();
                return true;
            }
        }

        return false;
    }
}
