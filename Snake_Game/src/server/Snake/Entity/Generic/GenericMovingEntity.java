// These generic classes are used for frontend to distinguish an entity from received packet
package server.Snake.Entity.Generic;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.Entity;

public class GenericMovingEntity extends AbstractMovingEntity {
    public GenericMovingEntity(Entity entity) {
        super(entity);
    }

    public GenericMovingEntity(float positionX, float positionY) {
        super(positionX, positionY);
    }

    @Override
    public boolean move() {
        return false;
    }

    @Override
    public void onCollide(Object collider) {
    }
}
