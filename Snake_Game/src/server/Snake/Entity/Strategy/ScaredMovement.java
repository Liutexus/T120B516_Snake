package server.Snake.Entity.Strategy;

import server.Snake.Entity.Player;
import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Interface.IMovingEntityBehaviour;
import server.Snake.Utility.Utils;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ScaredMovement implements IMovingEntityBehaviour {

    @Override
    public void move(AbstractMovingEntity entity) {
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

        // Move the entity
        entity.setPositionX(posX + entity.getVelocityX());
        entity.setPositionY(posY + entity.getVelocityY());
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
        // Get velocity which brings the entity closer to a player
        float[] predictVelocity = Utils.vectorToPoint(posX, posY, chased.getSnake().getPositionX(), chased.getSnake().getPositionY());
        entity.setVelocity(-predictVelocity[0], -predictVelocity[1]); // Invert the velocity to go away from the player

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
        // Get velocity which brings the entity closer to a player
        float[] predictVelocity = Utils.vectorToPoint(posX, posY, chased.getSnake().getPositionX(), chased.getSnake().getPositionY());
        entity.setVelocity(-predictVelocity[0], -predictVelocity[1]); // Invert the velocity to go away from the player

        // TODO: Check terrain as well

        // Move the entity
        entity.setPositionX(posX + entity.getVelocityX());
        entity.setPositionY(posY + entity.getVelocityY());
    }
}
