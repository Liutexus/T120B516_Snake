// GameLogic.java is responsible of validating players' moves and determining game's state
package server.Snake;

import server.Snake.Entity.Collectible.CollectibleFactory;
import server.Snake.Entity.Entity;
import server.Snake.Enums.EEffect;
import server.Snake.Interface.IFactory;
import server.Snake.Entity.Obstacle.ObstacleFactory;
import client.Snake.Entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameLogic implements Runnable {
    private Map<Integer, Handler> handlers = new ConcurrentHashMap<>();
    private Map<String, Player> players = new ConcurrentHashMap<>(); // All current players
    private Map<String, Entity> terrainEntities = new ConcurrentHashMap<>(); // All entities on the map
    private int[][] terrain;

    private IFactory CollectibleFactory = new CollectibleFactory();
    private IFactory ObstacleFactory = new ObstacleFactory();

    public GameLogic(Map handlers, Map players, Map terrainEntities, int[][] terrain){
        this.handlers = handlers;
        this.players = players;
        this.terrainEntities = terrainEntities;
        this.terrain = terrain;
    }

    private void movePlayers() {
        for (Player player : players.values()) {
            // --- JUST FOR FUN --- // For testing purposes
//            float[] directs = player.getSnake().getVelocity();
//            int randx = ThreadLocalRandom.current().nextInt(-1, 2);
//            if(randx == directs[0]*-1) randx = (int)directs[0];
//
//            int randy = 0;
//            if(randx == 0){
//                randy = ThreadLocalRandom.current().nextInt(-1, 2);
//            }
//            player.getSnake().setVelocity(randx, randy);
            // --- FUN ZONE OVER ---
            player.getSnake().move();
        }
    }

    private void checkCollisions() {
        // Checking collisions with other players
        players.forEach((id1, player1) -> {
            players.forEach((id2, player2) -> {
                if(player1 != player2)
                    player1.getSnake().onCollide(player2.getSnake());
            });

            // Checking collisions with terrain obstacles
            try {
                if(player1.getSnake().getEffects().size() == 0){ // Is player alright?
                    // Getting a position 'one ahead', to check if the player going to collide in the next move
                    int tPosX = (int)player1.getSnake().getPositionX()+(int)player1.getSnake().getVelocityX();
                    int tPosY = (int)player1.getSnake().getPositionY()+(int)player1.getSnake().getVelocityY();
                    if(terrain[tPosY][tPosX] == 6) { // '6' is an index for "Wall"
                        player1.getSnake().setEffect(EEffect.STUN, 10); // Apply a 'Stun' effect to the player for 10 moves
                        player1.getSnake().deltaTailLength(-1); // Decrease player's tail length by 1 on impact
                    }
                }
            } catch (Exception e){
                if(e instanceof ArrayIndexOutOfBoundsException) return; // Player is out of map
                e.printStackTrace();
            }

            // TODO: Checking collisions with other entities
        });
    }

    private void checkTerrainEntities() {
        if(!terrainEntities.containsKey("Food")) terrainEntities.put("Food", addStaticCollectible());
    }

    public void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    public void updatePlayerField(Map map) {
        if(map.containsKey("directionX") && map.containsKey("directionY")) {
            float currVelX = players.get(map.get("id")).getSnake().getVelocityX();
            float currVelY = players.get(map.get("id")).getSnake().getVelocityY();

            if((currVelX != -Float.parseFloat((String)map.get("directionX")) &&
                    currVelY != -Float.parseFloat((String)map.get("directionY"))) ||
                    (Float.parseFloat((String)map.get("directionX")) == 0 && Float.parseFloat((String)map.get("directionY")) == 0) ||
                    (currVelX == 0 && currVelY == 0))
                players.get(map.get("id")).getSnake().setVelocity(Float.parseFloat((String)map.get("directionX")), Float.parseFloat((String)map.get("directionY")));
        }
    }

    @Override
    public void run() {
        while(true) { // Main game loop
            // Game logic goes here
            checkCollisions();
            checkTerrainEntities();

            movePlayers();
            try {Thread.sleep(100);} catch (Exception e) { };
        }
    }

    // TODO: Add functionality to be able to add any Entity type to game grid.
    private void addObstacle() {
        // TODO: Add clauses which specify more concretely when to make which kind of obstacle (certain number of static/moving obstacles per map and etc.)
        this.ObstacleFactory.createMoving(5, 5);
        this.ObstacleFactory.createStatic(5, 5);
        // TODO: Add created Obstacle to game grid.
    }
    private Entity addStaticCollectible() {
        return this.CollectibleFactory.createStatic(5, 5);
    }
}
