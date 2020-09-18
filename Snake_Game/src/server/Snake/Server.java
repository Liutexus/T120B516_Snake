package server.Snake;

import client.Snake.Entities.Player;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;

public class Server {
    private static int concurrentClients = 20;
    private static ArrayList<Player> players = new ArrayList<Player>(); // All current players

    private static int count = 0;

    private boolean AddPlayerToGame(Player newPlayer){
        if(players.size() == 4){
            return  false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {

        try (var listener = new ServerSocket(80)) {
            System.out.println("The server is running...");
            var pool = Executors.newFixedThreadPool(concurrentClients); // To take in x amount of clients at a time
            while (true) {
                pool.execute(new Handler(listener.accept(), count)); // Listen to a client
                count++;
            }
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