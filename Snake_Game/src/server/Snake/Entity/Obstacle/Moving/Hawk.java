package server.Snake.Entity.Obstacle.Moving;

import server.Snake.Entity.AbstractMovingEntity;
import client.Snake.Entity.Player;
import server.Snake.Entity.Entity;
import server.Snake.Handler;
import server.Snake.Interface.IEntity;

import java.util.concurrent.ThreadLocalRandom;

public class Hawk extends AbstractMovingEntity implements IEntity {

    public Hawk(Entity entity){
        super(entity);
    }

    // TODO: Implement smarter moving behaviour, perhaps the Hawk could chase snakes in a visibility range.
    @Override
    public boolean move() {
        // RNG chance to change direction, brainlessly.
        int changeDirection = ThreadLocalRandom.current().nextInt(0, 100) + 1;
        if (changeDirection > 90) {
            // Normalize range [0; 1] to [-1; 1]
            this.velocityX = ThreadLocalRandom.current().nextFloat() * 2 - 1;
            this.velocityY = ThreadLocalRandom.current().nextFloat() * 2 - 1;
        }
        this.positionX += velocityX;
        this.positionY += velocityY;
        return true;
    }

    @Override
    public void onCollide(Object collider) {
        if(collider.getClass() == Player.class){
            ((Player)collider).deltaScore(-75);
        }
    }
}
