package server.Snake;

import client.Snake.Entities.Player;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.Executors;
import java.util.Dictionary;

public class Server {
    private static int concurrentClients = 20;
    private static ArrayList<Player> players = new ArrayList<Player>(); // All current players
//    private static Dictionary players = new Hashtable();
    private static int count = 0;

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