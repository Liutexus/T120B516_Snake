package server.Snake.Entity.Strategy;

import server.Snake.Entity.Effect.CollisionHandler;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Player;
import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Interface.IMovingEntityBehaviour;
import server.Snake.Utility.Utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ScaredMovement implements IMovingEntityBehaviour {

    @Override
    public void move(AbstractMovingEntity entity, int[][] terrain) {
        float posX = entity.getPositionX();
        float posY = entity.getPositionY();
        float[] directs = entity.getVelocity();
        // Determine a random x velocity
        int randx = ThreadLocalRandom.current().nextInt(-1, 2);
        if(randx == directs[0]*-1) randx = (int)directs[0];

        int randy = 0;
        if(randx == 0){ // If x = 0, determine a random y velocity
            randy = ThreadLocalRandom.current().nextInt(-1, 2);
        }
        entity.setVelocity(randx, randy);

        if(Arrays.deepEquals(terrain, new int[][]{})){
            // Move the entity
            entity.setPositionX(posX + entity.getVelocityX());
            entity.setPositionY(posY + entity.getVelocityY());
        } else {
            Entity tempClone = entity.clone();
            tempClone.setPositionX(posX + entity.getVelocityX());
            tempClone.setPositionY(posY + entity.getVelocityY());
            if(!CollisionHandler.checkCollisionWithTerrain((AbstractMovingEntity) tempClone, terrain)){ // Check if the entity collides with terrain in next move
                // Move the entity
                entity.setPositionX(posX + entity.getVelocityX());
                entity.setPositionY(posY + entity.getVelocityY());
            }
        }
    }

    @Override
    public void move(AbstractMovingEntity entity, Map<String, Player> players) {
        Player chased = null;
        float closestDistance = -1f;
        float posX = entity.getPositionX();
        float posY = entity.getPositionY();

        for (Player player : players.values()) { // Going through all players and determining which player is the closest
            float distanceToPlayer = Utils.vectorLengthToPoint(
                    posX,
                    posY,
                    player.getSnake().getPositionX(),
                    player.getSnake().getPositionY());
            if (closestDistance == -1f || distanceToPlayer < closestDistance) { // Is other player closer than previous one?
                closestDistance = distanceToPlayer;
                chased = player;
            }
        }

        if(closestDistance > 15){ // If player is not close
            this.move(entity, new int[][]{});
            return;
        }

        // Get velocity which brings the entity closer to a player
        try{
            float[] predictVelocity = Utils.vectorToPoint(posX, posY, chased.getSnake().getPositionX(), chased.getSnake().getPositionY());
            entity.setVelocity(predictVelocity[0], predictVelocity[1]); // Invert the velocity to go away from the player
        } catch (Exception e){}

        // Move the entity
        entity.setPositionX(posX + entity.getVelocityX());
        entity.setPositionY(posY + entity.getVelocityY());
    }

    @Override
    public void move(AbstractMovingEntity entity, Map<String, Player> players, int[][] terrain) {
        Player chased = null;
        float closestDistance = -1f;
        float posX = entity.getPositionX();
        float posY = entity.getPositionY();

        for (Player player : players.values()) { // Going through all players and determining which player is the closest
            float distanceToPlayer = Utils.vectorLengthToPoint(
                    posX,
                    posY,
                    player.getSnake().getPositionX(),
                    player.getSnake().getPositionY());
            if (closestDistance == -1f || distanceToPlayer < closestDistance) { // Is other player closer than previous one?
                closestDistance = distanceToPlayer;
                chased = player;
            }
        }

        if(closestDistance > 15){ // If player is not close
            this.move(entity, terrain);
            return;
        }

        // Get velocity which brings the entity closer to a player
        try{
            float[] predictVelocity = Utils.vectorToPoint(posX, posY, chased.getSnake().getPositionX(), chased.getSnake().getPositionY());
            entity.setVelocity(predictVelocity[0], predictVelocity[1]); // Invert the velocity to go away from the player
        } catch (Exception e){}

        Entity tempClone = entity.clone();
        tempClone.setPositionX(posX + entity.getVelocityX());
        tempClone.setPositionY(posY + entity.getVelocityY());
        if(!CollisionHandler.checkCollisionWithTerrain((AbstractMovingEntity) tempClone, terrain)){ // Check if the entity collides with terrain in next move
            // Move the entity
            entity.setPositionX(posX + entity.getVelocityX());
            entity.setPositionY(posY + entity.getVelocityY());
        }

    }
}
