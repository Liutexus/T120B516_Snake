package server.Snake.Entity.Collectible.Moving;

import server.Snake.Entity.AbstractMovingEntity;

import java.util.concurrent.ThreadLocalRandom;

public class Mouse extends AbstractMovingEntity {

    public Mouse(float positionX, float positionY) {
        super(positionX, positionY);
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
