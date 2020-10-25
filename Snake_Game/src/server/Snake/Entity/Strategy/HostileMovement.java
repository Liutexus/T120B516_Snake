package server.Snake.Entity.Strategy;

import client.Snake.Entity.Player;
import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Interface.IMovingEntityBehaviour;
import server.Snake.Utility.Utils;

import java.util.Map;

public class HostileMovement implements IMovingEntityBehaviour {

    @Override
    public void move(AbstractMovingEntity entity) {

    }

    @Override
    public void move(AbstractMovingEntity entity, Map<String, Player> players) {
        Player chased = null;
        float closestDistance = -1f;

        for (Player player : players.values()) {
            float distanceToPlayer = Utils.vectorLengthToPoint(
                    entity.getPositionX(),
                    entity.getPositionY(),
                    player.getSnake().getPositionX(),
                    player.getSnake().getPositionY());

            if (closestDistance == -1f || distanceToPlayer < closestDistance) {
                closestDistance = distanceToPlayer;
                chased = player;
            }
        }



        entity.setPositionX(entity.getPositionX() + entity.getVelocityX());
        entity.setPositionY(entity.getPositionY() + entity.getVelocityY());
    }

    @Override
    public void move(AbstractMovingEntity entity, Map<String, Player> players, int[][] terrain) {
        Player chased = null;
        float closestDistance = -1f;

        for (Player player : players.values()) {
            float distanceToPlayer = Utils.vectorLengthToPoint(
                    entity.getPositionX(),
                    entity.getPositionY(),
                    player.getSnake().getPositionX(),
                    player.getSnake().getPositionY());

            if (closestDistance == -1f || distanceToPlayer < closestDistance) {
                closestDistance = distanceToPlayer;
                chased = player;
            }
        }

        // TODO: Check around terrain

        entity.setPositionX(entity.getPositionX() + entity.getVelocityX());
        entity.setPositionY(entity.getPositionY() + entity.getVelocityY());
    }
}
