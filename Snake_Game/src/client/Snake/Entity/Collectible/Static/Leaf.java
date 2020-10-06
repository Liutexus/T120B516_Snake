package client.Snake.Entity.Collectible.Static;

import client.Snake.Entity.AbstractStaticEntity;
import client.Snake.Player;

public class Leaf extends AbstractStaticEntity {

    public Leaf(float positionX, float positionY) {
        super(positionX, positionY);
    }

    @Override
    public void onCollide(Player player) {
        player.deltaScore(50);
    }
}
