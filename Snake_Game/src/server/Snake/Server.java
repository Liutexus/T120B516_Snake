package server.Snake;

import server.Snake.Builder.HandlerBuilder;
import server.Snake.Utility.Utils;

import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static int concurrentMatches = Integer.parseInt(Utils.parseConfig("server", "maxMatches")); // How maximum concurrent clients can we run?
    private static Map<String, MatchInstance> matches = new ConcurrentHashMap<>(); // All current matches
    private static ExecutorService pool = Executors.newFixedThreadPool(concurrentMatches); // To take in x amount of clients at a time

    public static void main(String[] args) {
        try (var listener = new ServerSocket(Integer.parseInt(Utils.parseConfig("network", "port")))) {
            System.out.println("The server is running on port " + listener.getLocalPort());

            while (true) {
                HandlerBuilder handlerBuilder = new HandlerBuilder();
                handlerBuilder.setSocket(listener.accept());
                MatchInstance matchInstance = returnAvailableMatch();
                matchInstance.registerObserver(handlerBuilder);
            }
        } catch (Exception e) {
            System.out.println("Error running the server. Aborting...");
            return;
        }
    }

    private static MatchInstance returnAvailableMatch() {
        for (MatchInstance match: matches.values())
            if(match.getCurrentPlayerCount() < match.getMaxPlayerCount() && !match.isGameStarted())
                return match;
        MatchInstance matchInstance = new MatchInstance();
        pool.execute(matchInstance);
        matches.put(Utils.randomId(), matchInstance);
        return matchInstance;
    }

}