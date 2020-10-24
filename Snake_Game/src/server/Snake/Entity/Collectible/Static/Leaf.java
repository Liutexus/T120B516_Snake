package server.Snake.Entity.Collectible.Static;

import server.Snake.Entity.AbstractStaticEntity;
import client.Snake.Entity.Player;

public class Leaf extends AbstractStaticEntity {

    public Leaf(float positionX, float positionY) {
        super(positionX, positionY);
    }

    @Override
    public void onCollide(Object collider) {
        ((Player)collider).deltaScore(50);
    }
}
