package client.Snake.Entity.Obstacle.Static;

import client.Snake.Entity.AbstractStaticEntity;
import client.Snake.Player;

public class BearTrap extends AbstractStaticEntity {

    public BearTrap(float positionX, float positionY) {
        super(positionX, positionY);
    }

    @Override
    public void onCollide(Object collider) {
        if(collider.getClass() == Player.class){
            ((Player)collider).deltaScore(-100);
            ((Player)collider).getSnake().setVelocity(0,0);
        }
    }
}
