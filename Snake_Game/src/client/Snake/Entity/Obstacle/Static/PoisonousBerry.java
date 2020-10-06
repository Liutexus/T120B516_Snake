package client.Snake.Entity.Obstacle.Static;

import client.Snake.Entity.AbstractStaticEntity;
import client.Snake.Player;

public class PoisonousBerry extends AbstractStaticEntity {

    public PoisonousBerry(float positionX, float positionY) {
        super(positionX, positionY);
    }

    @Override
    public void onCollide(Player player) {
        player.deltaScore(-50);
    }
}
