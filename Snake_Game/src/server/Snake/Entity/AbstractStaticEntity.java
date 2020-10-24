package server.Snake.Entity;

public abstract class AbstractStaticEntity extends Entity {

    public AbstractStaticEntity(Entity entity) {
        super(entity.getPositionX(), entity.getPositionY());
    }

    public AbstractStaticEntity(float positionX, float positionY) {
        super(positionX, positionY);
    }

    public abstract void onCollide(Object collider);
}
