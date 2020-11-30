// GameLogic.java is responsible of validating players' moves and determining game's state
package server.Snake;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.AbstractStaticEntity;
import server.Snake.Entity.Collectible.CollectibleEntityFactory;
import server.Snake.Entity.Effect.CollisionHandler;
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

    private IEntityFactory collectibleFactory = new CollectibleEntityFactory();
    private IEntityFactory obstacleFactory = new ObstacleEntityFactory();

    public GameLogic(){}

    public GameLogic(Map handlers, Map players, Map terrainEntities, int[][] terrain){
        this.handlers = handlers;
        this.players = players;
        this.terrainEntities = terrainEntities;
        this.terrain = terrain;
    }

    private void move() {
        for (Player player : players.values())
            player.getSnake().move();

        terrainEntities.forEach((key, entity) -> {
            if(entity instanceof AbstractMovingEntity)
                ((AbstractMovingEntity) entity).move();
        });
    }

    private void checkCollisions() {
        // Checking collisions for players
        players.forEach((id1, player1) -> {
            players.forEach((id2, player2) -> {
                // Did both players snake's heads collided?
                if(player1 != player2 && CollisionHandler.checkCollisionOnEntities(player1.getSnake(), player2.getSnake()))
                    player1.getSnake().onCollide(player2.getSnake());

                int tailLength = player2.getSnake().getTailLength();
                int tailCollisionIndex = CollisionHandler.checkCollisionOnTails(player1.getSnake(), player2.getSnake());
                if(tailCollisionIndex > 0){ // Did player1 bite player2's tail?
                    if(player1 != player2)
                        player1.getSnake().deltaTailLength(tailLength - tailCollisionIndex);
                    player2.getSnake().deltaTailLength(-(tailLength - tailCollisionIndex));
                }
            });

            // Checking player collision with map
            // Getting a position 'one ahead', to check if the player going to collide in the next move
            if(CollisionHandler.checkCollisionWithTerrain(player1.getSnake(), terrain)) { // '6' is an index for "Wall"
                player1.getSnake().setEffect(EEffect.STUN, 5); // Apply a 'Stun' effect to the player for 5 moves
                player1.getSnake().deltaTailLength(-1); // Decrease player's tail length by 1 on impact
            }

            // Checking player collision with entities (collectibles, traps, etc)
            terrainEntities.forEach((name, entity) -> { // Going through all entities
                if(CollisionHandler.checkCollisionOnEntities(player1.getSnake(), entity)){
                    if(entity instanceof AbstractMovingEntity){ // To distinguish the type of entity
                        ((AbstractMovingEntity)entity).onCollide(player1);
                    } else if(entity instanceof AbstractStaticEntity){
                        ((AbstractStaticEntity)entity).onCollide(player1);
                    }
                    terrainEntities.remove(name);
                }
            });
        });

        // Checking collisions for entities
        terrainEntities.forEach((id, entity) -> {
            if(entity instanceof AbstractMovingEntity){
                if(CollisionHandler.checkCollisionWithTerrain((AbstractMovingEntity) entity, terrain)) { // '6' is an index for "Wall"
                    entity.setEffect(EEffect.STUN, 5); // Apply a 'Stun' effect to the player for 5 moves
                }
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

        if(!terrainEntities.containsKey("Mouse")) {
            terrainEntities.put("Mouse", addMovingCollectible());
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
        Entity entity = this.obstacleFactory.createMoving(randPos[0], randPos[1], this.players, terrain);
        return entity;
    }

    private Entity addMovingCollectible() {
        int[] randPos = Utils.findFreeCell(this.terrain, this.players, 5, 45);
        Entity entity = this.collectibleFactory.createMoving(randPos[0], randPos[1], this.players, terrain);
        return entity;
    }

    private Entity addStaticCollectible() {
        int[] randPos = Utils.findFreeCell(this.terrain, this.players, 5, 45);
        Entity entity = this.collectibleFactory.createStatic(randPos[0], randPos[1]);
        return entity;
    }
}
