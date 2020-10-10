package client.Snake.Entity.Obstacle.Static;

import client.Snake.Entity.AbstractStaticEntity;
import client.Snake.Player;

public class PoisonousBerry extends AbstractStaticEntity {

    public PoisonousBerry(float positionX, float positionY) {
        super(positionX, positionY);
    }

    @Override
    public void onCollide(Object collider) {
        if(collider.getClass() == Player.class)
            ((Player)collider).deltaScore(-50);
    }
}
