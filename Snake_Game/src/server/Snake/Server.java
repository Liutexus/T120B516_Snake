package server.Snake;

import client.Snake.Entities.Player;

import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class Server {
    private static int concurrentClients = 20;
    private static Map<String, Player> players = new ConcurrentHashMap<String, Player>(); // All current players
    private static int count = 0; // Current player count

    public static void main(String[] args) throws Exception {

        try (var listener = new ServerSocket(80)) {
            System.out.println("The server is running...");
            var pool = Executors.newFixedThreadPool(concurrentClients); // To take in x amount of clients at a time
            pool.execute(new GameLogic(players)); // Running main game's logic class
            while (true) {
                pool.execute(new Handler(listener.accept(), players)); // Listening to a client
                count++;
            }
        }
    }

}