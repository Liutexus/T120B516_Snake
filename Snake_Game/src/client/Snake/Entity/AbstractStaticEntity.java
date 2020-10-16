package client.Snake.Entity;

public abstract class AbstractStaticEntity extends Entity {

    public AbstractStaticEntity(float positionX, float positionY) {
        super(positionX, positionY);
    }

    public abstract void onCollide(Object collider);
}
