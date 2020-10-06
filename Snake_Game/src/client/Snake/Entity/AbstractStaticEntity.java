package client.Snake.Entity;
import client.Snake.Player;

public abstract class AbstractStaticEntity extends Entity {

    public AbstractStaticEntity(float positionX, float positionY) {
        super(positionX, positionY);
    }

    public abstract void onCollide(Player player);
}
