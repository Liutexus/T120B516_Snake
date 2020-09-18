package server.Snake;

import client.Snake.Entities.Player;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class Server {
    // TODO: A database of items here
    private static int concurrentClients = 20;
    private static ArrayList<Player> players = new ArrayList<Player>(); // All current players
    private static int count = 0;

    public static void main(String[] args) throws Exception {

        try (var listener = new ServerSocket(80)) {
            System.out.println("The server is running...");
            var pool = Executors.newFixedThreadPool(concurrentClients); // To take in x amount of clients at a time
            while (true) {
                pool.execute(new Handler(listener.accept(), players, count)); // Listen to a client
                count++;
            }
        }
    }




}