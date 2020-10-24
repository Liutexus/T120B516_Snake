package server.Snake.Entity.Collectible.Static;

import server.Snake.Entity.AbstractStaticEntity;
import client.Snake.Entity.Player;
import server.Snake.Entity.Entity;
import server.Snake.Interface.IEntity;

public class Leaf extends AbstractStaticEntity implements IEntity {

    public Leaf(Entity entity){
        super(entity);
    }

    @Override
    public void onCollide(Object collider) {
        ((Player)collider).deltaScore(50);
    }
}
