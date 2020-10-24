package server.Snake.Entity.Collectible.Static;

import server.Snake.Entity.AbstractStaticEntity;
import server.Snake.Entity.Snake;
import client.Snake.Entity.Player;

public class SizeUp extends AbstractStaticEntity {

    public SizeUp(float positionX, float positionY) {
        super(positionX, positionY);
    }

    @Override
    // TODO: Implement this size buff in a smarter manner (perhaps temporarily with a timer).
    // Increases size of player snake by 1x1.
    public void onCollide(Object collider) {
        Snake playerSnake = ((Player)collider).getSnake();
        playerSnake.deltaSize(1, 1);
    }
}
