package server.Snake;

import server.Snake.Builder.HandlerBuilder;
import server.Snake.Enumerator.EClientStatus;
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
    private static ExecutorService handlerPool = Executors.newFixedThreadPool(255);

    public static void main(String[] args) {
        run();
    }

    public static void run(){
        try (var listener = new ServerSocket(Integer.parseInt(Utils.parseConfig("network", "port")))) {
            System.out.println("The server is running on port " + listener.getLocalPort());

            while (true) {
                HandlerBuilder handlerBuilder = new HandlerBuilder();
                handlerBuilder.setSocket(listener.accept());
                handlerBuilder.setBuilder(handlerBuilder);
                handlerBuilder.setStatus(EClientStatus.MENU);
                handlerPool.execute(handlerBuilder.getProduct());
            }
        } catch (Exception e) {
            System.out.println("Error running the server. Aborting...");
//            e.printStackTrace();
            return;
        }
    }

    public static MatchInstance returnAvailableMatch() {
        for (MatchInstance match: matches.values())
            if(match.getCurrentPlayerCount() < match.getMaxPlayerCount() && !match.isGameStarted())
                return match;
        String id = Utils.randomId();
        MatchInstance matchInstance = new MatchInstance(id);
        pool.execute(matchInstance);
        matches.put(id, matchInstance);
        return matchInstance;
    }

    public static void unlistMatch(MatchInstance match){
        matches.remove(match.getId());
    }

}