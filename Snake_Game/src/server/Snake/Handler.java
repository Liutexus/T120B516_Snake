package server.Snake;

import client.Snake.Entities.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Handler implements Runnable {
    private Socket socket;
    private ArrayList<Player> players;
    private int count = 0;


    Handler(Socket socket, ArrayList<Player> players, int count) {
        // TODO: Generate, create and assign an ID for new incoming client here
        this.socket = socket; // Current socket object
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
        System.out.println("Connected: " + socket);

        try {
            // TODO: Catch incoming inputs and update data on server here
            // We listen to our client here
            var in = new Scanner(socket.getInputStream());
            // We return data from server to the client through here
            var out = new PrintWriter(socket.getOutputStream(), true);

            out.println(randomId());

            // TODO: Logic which processes client's inputs goes here
            // TODO: Add loop which listens for client's messages here
            //synchronized(sync_object){} // Synchronize data

//            while (in.hasNextLine()) { // Main server loop
//                System.out.println(in.nextLine().toUpperCase());
//                out.println(in.nextLine().toUpperCase());
//            }


        } catch (Exception e) {
            System.out.println("Error:" + socket);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
            System.out.println("Closed: " + socket);
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
}
