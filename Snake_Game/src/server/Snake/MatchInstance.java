package server.Snake;

import server.Snake.Entity.Player;
import server.Snake.Builder.HandlerBuilder;
import server.Snake.Entity.Entity;
import server.Snake.Enumerator.EClientStatus;
import server.Snake.Interface.IObserver;
import server.Snake.Interface.ISubject;
import server.Snake.Enumerator.EPacketHeader;
import server.Snake.Utility.BitmapConverter;
import server.Snake.Utility.Utils;

import java.awt.*;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class MatchInstance implements Runnable, ISubject {
    private String id;
    private static int concurrentThreads = 1;

    private Map<String, Player> players = new ConcurrentHashMap<>(); // All current players
    private Map<Integer, HandlerBuilder> handlers = new ConcurrentHashMap<>(); // All opened socket's to clients
    private Map<Integer, Entity> terrainEntities = new ConcurrentHashMap<>(); // All collectibles on the map

    private GameLogic gameLogic;
    private int[][] terrain;
    private int maxPlayerCount = Integer.parseInt(Utils.parseConfig("server", "maxPlayersPerMatch"));
    private int currentPlayerCount = 0;

    private boolean gameStarted = false;

    public MatchInstance(String id) {
        this.id = id;
        this.terrain = BitmapConverter.BMPToIntArray("resources/arena_test_01.png", 50, 50);
        this.gameLogic = new GameLogic(this.handlers, this.players, this.terrainEntities, this.terrain);
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

    private Player createPlayer(String id) {
        if(players.containsKey(id)) {
            System.out.println("Player already exists.");
            return null;
        }
        int[] randPos = Utils.findFreeCell(terrain, 5, 45);

        // This could be improved by some more fancier initial position assignment
        Player player = new Player(id, randPos[0], randPos[1]);

        Random rand = new Random();
        Color randomColor = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        player.getSnake().setColor(randomColor); // Assigning a random color for the player

        this.gameLogic.addPlayer(player); // Adding new client user to the players' pool
        return player;
    }

    private void matchSetup() {
        handlers.forEach((index, handlerBuilder) -> { // Setting and starting up all handlers
            handlerBuilder.setMatchInstance(this);
            handlerBuilder.setGameLogic(this.gameLogic);
            handlerBuilder.setStatus(EClientStatus.IN_GAME);
            handlerBuilder.setBuilder(handlerBuilder);
            handlerBuilder.setPlayers(this.players);
            handlerBuilder.setTerrainEntities(this.terrainEntities);
        });

        handlers.forEach((id, handlerBuilder) -> {
            String randId = Utils.randomId();
            handlerBuilder.getProduct().sendLoginInfo(randId, createPlayer(randId)); // Sending client it's info generated by server
            for (int i = 0; i < terrain.length; i++) { // Going through all vertical lines of generated terrain
                handlerBuilder.getProduct().sendPacket(EPacketHeader.TERRAIN, BitmapConverter.intArrayToJSON(this.terrain, i)); // Sending terrain data
            }
        });

        gameStarted = true;
    }

    @Override
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(concurrentThreads);
        pool.execute(gameLogic);
        while(true){
            if(currentPlayerCount != maxPlayerCount && !gameStarted) {
                handlers.forEach((index, handlerBuilder) -> {
                    handlerBuilder.setStatus(EClientStatus.LOBBY);
                });

                try {Thread.sleep(1000);} catch (Exception e) { };
                continue;
            }

            if(!gameStarted) // To set up the match
                matchSetup();

            if(currentPlayerCount == 0) // If all players have left the match
                break;

            try {Thread.sleep(100);} catch (Exception e) { };
        }

        System.out.println("Finishing match: " + this.id);
    }

    @Override
    public boolean registerObserver(IObserver o) {
        if(this.currentPlayerCount < this.maxPlayerCount){
            handlers.put(currentPlayerCount, (HandlerBuilder) o);
            this.currentPlayerCount++;
            return true;
        }
        return false;
    }

    @Override
    public boolean unregisterObserver(IObserver o) {
        try {
            handlers.remove(o);
            currentPlayerCount--;
        } catch (Exception e){
            return false;
        }

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
