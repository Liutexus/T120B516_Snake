package client.Snake.Entity.Collectible.Static;

import client.Snake.Entity.AbstractStaticEntity;
import client.Snake.Entity.Snake;
import client.Snake.Player;

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
