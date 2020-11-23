package server.Snake.Entity.Collectible;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.AbstractStaticEntity;
import server.Snake.Entity.Collectible.Moving.Mouse;
import server.Snake.Entity.Collectible.Static.Leaf;
import server.Snake.Entity.Collectible.Static.Reverse;
import server.Snake.Entity.Collectible.Static.SizeUp;
import server.Snake.Entity.Collectible.Static.SpeedUp;
import server.Snake.Entity.Entity;
import server.Snake.Interface.IEntityFactory;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class CollectibleEntityFactory implements IEntityFactory {

    // TODO: Implement a "smarter" way of using this factory, rather than just getting RNG types.
    @Override
    public AbstractMovingEntity createMoving(float positionX, float positionY) {
        return null; // TODO: just Null for now
//        return new Mouse(new Entity(positionX, positionY));
    }

    @Override
    public AbstractMovingEntity createMoving(float positionX, float positionY, Map players) {
        return new Mouse(new Entity(positionX, positionY), players);
    }

    @Override
    public AbstractStaticEntity createStatic(float positionX, float positionY) {
        int randomCase = ThreadLocalRandom.current().nextInt(0, 4);
        switch (randomCase) {
            case 0:
                return new Leaf(new Entity(positionX, positionY));
            case 1:
                return new SpeedUp(new Entity(positionX, positionY));
            case 2:
                return new Leaf(new SpeedUp(new Entity(positionX, positionY)));
            case 3:
                return new Reverse(new Leaf(new Entity(positionX, positionY)));
            default:
                return null;
        }
    }
}
