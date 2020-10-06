package client.Snake.Entity.Collectible;

import client.Snake.Entity.AbstractMovingEntity;
import client.Snake.Entity.AbstractStaticEntity;
import client.Snake.Entity.Collectible.Moving.Mouse;
import client.Snake.Entity.Collectible.Static.Leaf;
import client.Snake.Entity.Collectible.Static.SizeUp;
import client.Snake.Entity.Collectible.Static.SpeedUp;
import client.Snake.Entity.IFactory;

import java.util.concurrent.ThreadLocalRandom;

public class CollectibleFactory implements IFactory {

    // TODO: Implement a "smarter" way of using this factory, rather than just getting RNG types.
    @Override
    public AbstractMovingEntity createMoving(float positionX, float positionY) {
        return new Mouse(positionX, positionY);
    }

    @Override
    public AbstractStaticEntity createStatic(float positionX, float positionY) {
        int randomCase = ThreadLocalRandom.current().nextInt(0, 3);
        switch (randomCase) {
            case 0:
                return new Leaf(positionX, positionY);
            case 1:
                return new SizeUp(positionX, positionY);
            case 2:
                return new SpeedUp(positionX, positionY);
            default:
                return null;
        }
    }
}