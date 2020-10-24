package server.Snake.Entity.Obstacle;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.AbstractStaticEntity;
import server.Snake.Interface.IFactory;
import server.Snake.Entity.Obstacle.Moving.Hawk;
import server.Snake.Entity.Obstacle.Static.BearTrap;
import server.Snake.Entity.Obstacle.Static.PoisonousBerry;

import java.util.concurrent.ThreadLocalRandom;

public class ObstacleFactory implements IFactory {

    // TODO: Implement a "smarter" way of using this factory, rather than just getting RNG types.
    @Override
    public AbstractMovingEntity createMoving(float positionX, float positionY) {
        return new Hawk(positionX, positionY);
    }

    @Override
    public AbstractStaticEntity createStatic(float positionX, float positionY) {
        int randomCase = ThreadLocalRandom.current().nextInt(0, 2);
        switch (randomCase) {
            case 0:
                return new BearTrap(positionX, positionY);
            case 1:
                return new PoisonousBerry(positionX, positionY);
            default:
                return null;
        }
    }
}
