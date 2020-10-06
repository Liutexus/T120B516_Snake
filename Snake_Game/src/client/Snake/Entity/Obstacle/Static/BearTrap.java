package client.Snake.Entity.Obstacle.Static;

import client.Snake.Entity.AbstractStaticEntity;
import client.Snake.Player;

public class BearTrap extends AbstractStaticEntity {

    public BearTrap(float positionX, float positionY) {
        super(positionX, positionY);
    }

    @Override
    public void onCollide(Player player) {
        player.deltaScore(-100);
        player.getSnake().setVelocity(0,0);
    }
}
