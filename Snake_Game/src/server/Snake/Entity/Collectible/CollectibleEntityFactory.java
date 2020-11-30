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
import server.Snake.Utility.Utils;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class CollectibleEntityFactory implements IEntityFactory {

    @Override
    public AbstractMovingEntity createMoving(String id, float positionX, float positionY, Map players, int[][] terrain) {
        return new Mouse(new Entity(id, positionX, positionY), players, terrain);
    }

    @Override
    public AbstractStaticEntity createStatic(String id, float positionX, float positionY) {
        int randomCase = ThreadLocalRandom.current().nextInt(0, 4);
        switch (randomCase) {
            case 0:
                return new Leaf(new Entity(id, positionX, positionY));
            case 1:
                return new SpeedUp(new Entity(id, positionX, positionY));
            case 2:
                return new Leaf(new SpeedUp(new Entity(id, positionX, positionY)));
            case 3:
                return new Reverse(new Leaf(new Entity(id, positionX, positionY)));
            default:
                return null;
        }
    }
}
