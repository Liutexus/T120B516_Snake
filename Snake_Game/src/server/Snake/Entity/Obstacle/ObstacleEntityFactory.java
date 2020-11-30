package server.Snake.Entity.Obstacle;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.AbstractStaticEntity;
import server.Snake.Entity.Entity;
import server.Snake.Interface.IEntityFactory;
import server.Snake.Entity.Obstacle.Moving.Hawk;
import server.Snake.Entity.Obstacle.Static.BearTrap;
import server.Snake.Entity.Obstacle.Static.PoisonousBerry;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ObstacleEntityFactory implements IEntityFactory {

    @Override
    public AbstractMovingEntity createMoving(String id, float positionX, float positionY, Map players, int[][] terrain) {
        return new Hawk(new Entity(id, positionX, positionY), players, terrain);
    }

    @Override
    public AbstractStaticEntity createStatic(String id, float positionX, float positionY) {
        int randomCase = ThreadLocalRandom.current().nextInt(0, 2);
        switch (randomCase) {
            case 0:
                return new BearTrap(new Entity(id, positionX, positionY));
            case 1:
                return new PoisonousBerry(new Entity(id, positionX, positionY));
            default:
                return null;
        }
    }
}
