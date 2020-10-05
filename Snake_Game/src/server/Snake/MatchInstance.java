package server.Snake;

import client.Snake.Entities.Player;
import server.Snake.Interface.Observer;
import server.Snake.Interface.Subject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class MatchInstance implements Runnable, Subject {
    private static int concurrentClients = 4; // How maximum concurrent clients can we run?
    private static Map<String, Player> players = new ConcurrentHashMap<>(); // All current players
    private static Map<Integer, Handler> handlers = new ConcurrentHashMap<>(); // All opened socket's to clients

    private GameLogic gameLogic;
    private int maxPlayerCount = 4;
    private int currentPlayerCount = 0;

    private boolean gameStarted = false;

    public MatchInstance() {
        this.gameLogic = new GameLogic(this.handlers, this.players);
    }

    public int getCurrentPlayerCount() {
        return this.currentPlayerCount;
    }

    public int getMaxPlayerCount() {
        return this.maxPlayerCount;
    }

    @Override
    public void run() {
        var pool = Executors.newFixedThreadPool(concurrentClients); // To take in x amount of clients at a time
        pool.execute(gameLogic);
        while(true) {
            if(currentPlayerCount != maxPlayerCount) {
                try {Thread.sleep(100);} catch (Exception e) { };
                continue;
            }

            if(!gameStarted){
                handlers.forEach((index, handler) -> {
                    handler.setGameLogic(this.gameLogic);
                    handler.setPlayers(this.players);
                    pool.execute(handler);
                });
                gameStarted = true;
            }

            try {Thread.sleep(100);} catch (Exception e) { };
        }

    }

    @Override
    public boolean registerObserver(Observer o) {
        if(this.currentPlayerCount < this.maxPlayerCount){
            handlers.put(currentPlayerCount, (Handler)o);
            this.currentPlayerCount++;
            return true;
        }
        return false;
    }

    @Override
    public boolean unregisterObserver(Observer o) {
        handlers.remove(o);
        return true;
    }

    @Override
    public boolean notifyObservers() {
        return false;
    }
}
