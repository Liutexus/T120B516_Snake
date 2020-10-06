package server.Snake;

import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static int concurrentMatches = 64; // How maximum concurrent clients can we run?
    private static Map<String, MatchInstance> matches = new ConcurrentHashMap<>(); // All current matches
    private static ExecutorService pool = Executors.newFixedThreadPool(concurrentMatches); // To take in x amount of clients at a time

    public static void main(String[] args) {
        try (var listener = new ServerSocket(80)) {
            System.out.println("The server is running...");

            while (true) {
                Handler handler = new Handler(listener.accept());
                MatchInstance matchInstance = returnAvailableMatch();
                matchInstance.registerObserver(handler);
            }
        } catch (Exception e) {
            System.out.println("Error running the server. Aborting...");
            return;
        }
    }

    private static String randomId() {
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

        return generatedString;
    }

    private static MatchInstance returnAvailableMatch() {
        for (MatchInstance match: matches.values())
            if(match.getCurrentPlayerCount() < match.getMaxPlayerCount() && !match.isGameStarted())
                return match;
        MatchInstance matchInstance = new MatchInstance();
        pool.execute(matchInstance);
        matches.put(randomId(), matchInstance);
        return matchInstance;
    }

}