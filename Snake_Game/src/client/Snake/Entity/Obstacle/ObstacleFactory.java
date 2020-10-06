package client.Snake.Entity.Obstacle;

import client.Snake.Entity.AbstractMovingEntity;
import client.Snake.Entity.AbstractStaticEntity;
import client.Snake.Entity.Collectible.Static.Leaf;
import client.Snake.Entity.Collectible.Static.SizeUp;
import client.Snake.Entity.Collectible.Static.SpeedUp;
import client.Snake.Entity.IFactory;
import client.Snake.Entity.Obstacle.Moving.Hawk;
import client.Snake.Entity.Obstacle.Static.BearTrap;
import client.Snake.Entity.Obstacle.Static.PoisonousBerry;

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
