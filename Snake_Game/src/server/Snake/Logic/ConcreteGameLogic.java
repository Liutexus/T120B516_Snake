// GameLogic.java is responsible of validating players' moves and determining game's state
package server.Snake.Logic;

import server.Snake.Entity.*;
import server.Snake.Entity.Collectible.CollectibleEntityFactory;
import server.Snake.Entity.Effect.CollisionHandler;
import server.Snake.Enumerator.EEffect;
import server.Snake.Interface.IEntityFactory;
import server.Snake.Entity.Obstacle.ObstacleEntityFactory;
import server.Snake.Interface.IHandler;
import server.Snake.MatchInstance;
import server.Snake.Network.Handler;
import server.Snake.Network.Packet.Packet;
import server.Snake.Utility.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcreteGameLogic implements Runnable, IHandler, IGameLogic {
    private Map<Integer, Handler> handlers = new ConcurrentHashMap<>();
    private MatchInstance matchInstance;
    private Map<String, Player> players = new ConcurrentHashMap<>(); // All current players
    private Map<String, Entity> terrainEntities = new ConcurrentHashMap<>(); // All entities on the map

    private int[][] terrain;

    private IEntityFactory collectibleFactory = new CollectibleEntityFactory();
    private IEntityFactory obstacleFactory = new ObstacleEntityFactory();

    private int staticCollectibleCount = 3;
    private int movingCollectibleCount = 2;
    private int staticObstacleCount = 2;
    private int movingObstacleCount = 2;

    private IHandler nextHandler;

    public ConcreteGameLogic(){}

    public ConcreteGameLogic(Map handlers, Map players, MatchInstance matchInstance, Map terrainEntities, int[][] terrain){
        this.handlers = handlers;
        this.players = players;
        this.matchInstance = matchInstance;
        this.terrainEntities = terrainEntities;
        this.terrain = terrain;
    }

    private void move() {
        for (Player player : players.values()){
            if(player.getSnake().getEffects().containsKey(EEffect.HASTE) && !player.getSnake().getEffects().containsKey(EEffect.STUN) && !player.getSnake().getEffects().containsKey(EEffect.ROLLBACK)){
                player.getSnake().move();
                checkEntityCollisions(player.getSnake());
            }
            player.getSnake().move();
        }

        terrainEntities.forEach((key, entity) -> {
            if(entity instanceof AbstractMovingEntity)
                ((AbstractMovingEntity) entity).move();
        });
    }

    private void checkCollisions() {
        // Checking collisions for players
        players.forEach((id1, player1) -> {
            // Checking collisions for players
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
            terrainEntities.forEach((id, entity) -> { // Going through all entities
                if(CollisionHandler.checkCollisionOnEntities(player1.getSnake(), entity)){
                    if(entity instanceof AbstractMovingEntity){ // To distinguish the type of entity
                        ((AbstractMovingEntity)entity).onCollide(player1.getSnake());
                    } else if(entity instanceof AbstractStaticEntity){
                        ((AbstractStaticEntity)entity).onCollide(player1.getSnake());
                    }
                    terrainEntities.remove(id);
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

    private void checkEntityCollisions(AbstractMovingEntity movingEntity){
        // Checking collisions for players
        players.forEach((id2, player2) -> {
            // Did both players snake's heads collided?
            if(movingEntity != player2.getSnake() && CollisionHandler.checkCollisionOnEntities(movingEntity, player2.getSnake()))
                movingEntity.onCollide(player2.getSnake());
            if(movingEntity instanceof Snake){
                int tailLength = player2.getSnake().getTailLength();
                int tailCollisionIndex = CollisionHandler.checkCollisionOnTails((Snake)movingEntity, player2.getSnake());
                if(tailCollisionIndex > 0){ // Did movingEntity bite player2's tail?
                    if(movingEntity != player2.getSnake())
                        ((Snake)movingEntity).deltaTailLength(tailLength - tailCollisionIndex);
                    player2.getSnake().deltaTailLength(-(tailLength - tailCollisionIndex));
                }
            }
        });

        // Checking player collision with map
        // Getting a position 'one ahead', to check if the player going to collide in the next move
        if(CollisionHandler.checkCollisionWithTerrain(movingEntity, terrain)) { // '6' is an index for "Wall"
            movingEntity.setEffect(EEffect.STUN, 5); // Apply a 'Stun' effect to the player for 5 moves
            if(movingEntity instanceof Snake)
                ((Snake)movingEntity).deltaTailLength(-1); // Decrease player's tail length by 1 on impact
        }

        // Checking player collision with entities (collectibles, traps, etc)
        terrainEntities.forEach((id, entity) -> { // Going through all entities
            if(CollisionHandler.checkCollisionOnEntities(movingEntity, entity)){
                if(entity instanceof AbstractMovingEntity){ // To distinguish the type of entity
                    ((AbstractMovingEntity)entity).onCollide(movingEntity);
                } else if(entity instanceof AbstractStaticEntity){
                    ((AbstractStaticEntity)entity).onCollide(movingEntity);
                }
                terrainEntities.remove(id);
            }
        });
    }

    private void checkTerrainEntities() {
        for(int i = 0; i < staticCollectibleCount; i++){
            if(!terrainEntities.containsKey("Food" + i)){
                terrainEntities.put("Food" + i, addStaticCollectible("Food" + i));
            }
        }

        for(int i = 0; i < movingCollectibleCount; i++) {
            if(!terrainEntities.containsKey("Mouse" + i)) {
                terrainEntities.put("Mouse" + i, addMovingCollectible("Mouse" + i));
            }
        }

//        for(int i = 0; i < staticObstacleCount; i++){
//            if(!terrainEntities.containsKey("Trap" + i)){
//                terrainEntities.put("Trap" + i, addStaticObstacle("Trap" + i));
//            }
//        }

        for(int i = 0; i < movingObstacleCount; i++) {
            if(!terrainEntities.containsKey("Hawk" + i)) {
                terrainEntities.put("Hawk" + i, addMovingObstacle("Hawk" + i));
            }
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

    @Override
    public void setNext(IHandler handler) {
        this.nextHandler = handler;
    }

    @Override
    public void handle(Object request) {
        switch (((Packet)request).header){
            case CLIENT_RESPONSE:
                this.updatePlayerField(((Packet)request).parseBody());
                break;
            default:
                this.setNext(this.matchInstance);
                this.nextHandler.handle(request);
                break;
        }
    }

    private Entity addStaticObstacle(String id) {
        int[] randPos = Utils.findFreeCell(this.terrain, this.players, 5, 45);
        Entity entity = this.collectibleFactory.createStatic(id, randPos[0], randPos[1]);
        return entity;
    }

    private Entity addMovingObstacle(String id){
        int[] randPos = Utils.findFreeCell(this.terrain, this.players, 5, 45);
        Entity entity = this.obstacleFactory.createMoving(id, randPos[0], randPos[1], this.players, terrain);
        return entity;
    }

    private Entity addStaticCollectible(String id) {
        int[] randPos = Utils.findFreeCell(this.terrain, this.players, 5, 45);
        Entity entity = this.collectibleFactory.createStatic(id, randPos[0], randPos[1]);
        return entity;
    }

    private Entity addMovingCollectible(String id) {
        int[] randPos = Utils.findFreeCell(this.terrain, this.players, 5, 45);
        Entity entity = this.collectibleFactory.createMoving(id, randPos[0], randPos[1], this.players, terrain);
        return entity;
    }

}
