package server.Snake.Entity.Collectible.Static;

import server.Snake.Entity.AbstractStaticEntity;
import client.Snake.Entity.Player;
import server.Snake.Entity.Entity;
import server.Snake.Interface.IEntity;

public class SpeedUp extends AbstractStaticEntity implements IEntity {

    public SpeedUp(Entity entity){
        super(entity);
    }

    @Override
    // TODO: Apply Speed buff in a smarter manner.
    // Doubles the current velocity of player snake.
    public void onCollide(Object collider) {
        float[] originalVelocity = ((Player)collider).getSnake().getVelocity();
        ((Player)collider).getSnake().setVelocity(originalVelocity[0] * 2, originalVelocity[1] * 2);
    }
}
