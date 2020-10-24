package server.Snake.Entity.Obstacle.Static;

import server.Snake.Entity.AbstractStaticEntity;
import client.Snake.Entity.Player;
import server.Snake.Entity.Entity;
import server.Snake.Enums.ESnakeEffect;
import server.Snake.Interface.IEntity;

public class PoisonousBerry extends AbstractStaticEntity implements IEntity {

    public PoisonousBerry(Entity entity){
        super(entity);
    }

    @Override
    public void onCollide(Object collider) {
        if(collider.getClass() == Player.class){
            ((Player)collider).deltaScore(-50);
            ((Player)collider).getSnake().setDebuff(ESnakeEffect.SLOW, 50);
        }
    }
}
