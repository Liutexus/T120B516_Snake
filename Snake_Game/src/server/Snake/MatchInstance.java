package server.Snake;

import client.Snake.Player;
import server.Snake.Interface.IObserver;
import server.Snake.Interface.ISubject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class MatchInstance implements Runnable, ISubject {
    private static int concurrentThreads = 5;
    private Map<String, Player> players = new ConcurrentHashMap<>(); // All current players
    private Map<Integer, Handler> handlers = new ConcurrentHashMap<>(); // All opened socket's to clients

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

    public boolean isGameStarted() {
        return gameStarted;
    }

    @Override
    public void run() {
        var pool = Executors.newFixedThreadPool(concurrentThreads);
        pool.execute(gameLogic);
        while(true) {
            if(currentPlayerCount != maxPlayerCount) {
                try {Thread.sleep(100);} catch (Exception e) { };
                continue;
            }

            if(!gameStarted){
                handlers.forEach((index, handler) -> {
                    handler.setMatchInstance(this);
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
    public boolean registerObserver(IObserver o) {
        if(this.currentPlayerCount < this.maxPlayerCount){
            handlers.put(currentPlayerCount, (Handler)o);
            this.currentPlayerCount++;
            return true;
        }
        return false;
    }

    @Override
    public boolean unregisterObserver(IObserver o) {
        handlers.remove(o);
        return true;
    }

    @Override
    public boolean notifyObservers() {
        try {
            handlers.forEach((index, handler) -> {
                handler.update();
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
