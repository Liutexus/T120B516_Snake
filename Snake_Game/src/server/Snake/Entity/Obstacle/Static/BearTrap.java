package server.Snake.Entity.Obstacle.Static;

import server.Snake.Entity.AbstractStaticEntity;
import client.Snake.Entity.Player;
import server.Snake.Enums.ESnakeEffect;
import server.Snake.Interface.IEntity;

public class BearTrap extends AbstractStaticEntity implements IEntity {

    public BearTrap(float positionX, float positionY) {
        super(positionX, positionY);
    }

    @Override
    public void onCollide(Object collider) {
        if(collider.getClass() == Player.class){
            ((Player)collider).deltaScore(-100);
            ((Player)collider).getSnake().setDebuff(ESnakeEffect.STUN, 50);
        }
    }
}
