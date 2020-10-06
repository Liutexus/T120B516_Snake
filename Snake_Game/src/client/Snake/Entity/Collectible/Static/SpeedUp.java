package client.Snake.Entity.Collectible.Static;

import client.Snake.Entity.AbstractStaticEntity;
import client.Snake.Player;

public class SpeedUp extends AbstractStaticEntity {

    public SpeedUp(float positionX, float positionY) {
        super(positionX, positionY);
    }

    @Override
    // TODO: Apply Speed buff in a smarter manner.
    // Doubles the current velocity of player snake.
    public void onCollide(Player player) {
        float[] originalVelocity = player.getSnake().getVelocity();
        player.getSnake().setVelocity(originalVelocity[0] * 2, originalVelocity[1] * 2);
    }
}
