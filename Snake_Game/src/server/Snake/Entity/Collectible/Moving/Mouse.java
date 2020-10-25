package server.Snake.Entity.Collectible.Moving;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.Entity;
import server.Snake.Interface.IEntity;

import java.util.concurrent.ThreadLocalRandom;

public class Mouse extends AbstractMovingEntity implements IEntity {

    public Mouse(Entity entity) {
        super(entity);
    }

    @Override
    // TODO: Possibly modify movement behaviour to avoid snakes.
    public boolean move() {
        // RNG chance to change direction, brainlessly.
        int changeDirection = ThreadLocalRandom.current().nextInt(0, 100) + 1;
        if (changeDirection > 70) {
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

    }
}
