// GameLogic.java is responsible of validating players' moves and determining game's state
package server.Snake;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.Collectible.CollectibleEntityFactory;
import server.Snake.Entity.Entity;
import server.Snake.Enumerator.EEffect;
import server.Snake.Interface.IEntityFactory;
import server.Snake.Entity.Obstacle.ObstacleEntityFactory;
import server.Snake.Entity.Player;
import server.Snake.Utility.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameLogic implements Runnable {
    private Map<Integer, Handler> handlers = new ConcurrentHashMap<>();
    private Map<String, Player> players = new ConcurrentHashMap<>(); // All current players
    private Map<String, Entity> terrainEntities = new ConcurrentHashMap<>(); // All entities on the map

    private int[][] terrain;

    private IEntityFactory CollectibleFactory = new CollectibleEntityFactory();
    private IEntityFactory ObstacleFactory = new ObstacleEntityFactory();

    public GameLogic(){}

    public GameLogic(Map handlers, Map players, Map terrainEntities, int[][] terrain){
        this.handlers = handlers;
        this.players = players;
        this.terrainEntities = terrainEntities;
        this.terrain = terrain;
    }

    private void move() {
        for (Player player : players.values()) {
            player.getSnake().move();
        }

        terrainEntities.forEach((key, entity) -> {
            if(entity instanceof AbstractMovingEntity){
                ((AbstractMovingEntity) entity).move();
            }
        });
    }

    private void checkCollisions() {
        // Checking collisions with other players
        players.forEach((id1, player1) -> {
            players.forEach((id2, player2) -> {
                if(player1 != player2)
                    player1.getSnake().onCollide(player2.getSnake());
            });

            // Checking collisions with terrain entities
            try {
                // Checking player collision with map
                // Getting a position 'one ahead', to check if the player going to collide in the next move
                int tPosX = (int)player1.getSnake().getPositionX()+(int)player1.getSnake().getVelocityX();
                int tPosY = (int)player1.getSnake().getPositionY()+(int)player1.getSnake().getVelocityY();
                if(terrain[tPosY][tPosX] == 6 && !player1.getSnake().getEffects().containsKey(EEffect.STUN)) { // '6' is an index for "Wall"
                    player1.getSnake().setEffect(EEffect.STUN, 10); // Apply a 'Stun' effect to the player for 10 moves
                    player1.getSnake().deltaTailLength(-1); // Decrease player's tail length by 1 on impact
                }

                // Checking player collision with entities (collectibles, traps, etc)
                terrainEntities.forEach((name, entity) -> { // Going through all entities
                    if(entity.getPositionX() == tPosX && entity.getPositionY() == tPosY){
                        entity.getEffects().forEach((effect, duration) -> { // Transferring all terrain entities effects to player
                            player1.getSnake().setEffect(effect, duration);
                        });
                        terrainEntities.remove(name);
                        player1.getSnake().deltaTailLength(1); // increase snake tail +1
                    }
                });
            } catch (Exception e){
                if(e instanceof ArrayIndexOutOfBoundsException) return; // Player is out of map
                e.printStackTrace();
            }
        });
    }

    private void checkTerrainEntities() {
        if(!terrainEntities.containsKey("Food")){
            terrainEntities.put("Food", addStaticCollectible());
        }

        if(!terrainEntities.containsKey("Hawk")) {
            terrainEntities.put("Hawk", addMovingObstacle());
        }
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

            move();

            try {Thread.sleep(100);} catch (Exception e) { };
        }
    }

    private Entity addMovingObstacle(){
        int[] randPos = Utils.findFreeCell(this.terrain, this.players, 5, 45);
        Entity entity = this.ObstacleFactory.createMoving(randPos[0], randPos[1], this.players);
        return entity;
    }

    private Entity addStaticCollectible() {
        int[] randPos = Utils.findFreeCell(this.terrain, this.players, 5, 45);
        Entity entity = this.CollectibleFactory.createStatic(randPos[0], randPos[1]);
        return entity;
    }
}
