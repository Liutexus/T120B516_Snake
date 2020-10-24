package server.Snake.Entity.Collectible.Static;

import server.Snake.Entity.AbstractStaticEntity;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Snake;
import client.Snake.Entity.Player;
import server.Snake.Interface.IEntity;

public class SizeUp extends AbstractStaticEntity implements IEntity {

    public SizeUp(Entity entity){
        super(entity);
    }

    @Override
    // TODO: Implement this size buff in a smarter manner (perhaps temporarily with a timer).
    // Increases size of player snake by 1x1.
    public void onCollide(Object collider) {
        Snake playerSnake = ((Player)collider).getSnake();
        playerSnake.deltaSize(1, 1);
    }
}
