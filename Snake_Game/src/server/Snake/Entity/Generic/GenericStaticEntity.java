// These generic classes are used for frontend to distinguish an entity from received packet
package server.Snake.Entity.Generic;

import server.Snake.Entity.AbstractStaticEntity;
import server.Snake.Interface.IEntity;

public class GenericStaticEntity extends AbstractStaticEntity {
    public GenericStaticEntity(IEntity entity) {
        super(entity);
    }

    public GenericStaticEntity(float positionX, float positionY) {
        super(positionX, positionY);
    }
}

