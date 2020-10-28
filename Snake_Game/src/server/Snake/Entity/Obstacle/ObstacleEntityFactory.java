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
    public AbstractMovingEntity createMoving(float positionX, float positionY) {
        return null; // TODO: just Null for now
//        return new Hawk(new Entity(positionX, positionY), null);
    }

    // TODO: Implement a "smarter" way of using this factory, rather than just getting RNG types.
    @Override
    public AbstractMovingEntity createMoving(float positionX, float positionY, Map players) {
        return new Hawk(new Entity(positionX, positionY), players);
    }

    @Override
    public AbstractStaticEntity createStatic(float positionX, float positionY) {
        int randomCase = ThreadLocalRandom.current().nextInt(0, 2);
        switch (randomCase) {
            case 0:
                return new BearTrap(new Entity(positionX, positionY));
            case 1:
                return new PoisonousBerry(new Entity(positionX, positionY));
            default:
                return null;
        }
    }
}
