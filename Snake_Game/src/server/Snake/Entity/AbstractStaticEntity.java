package server.Snake.Entity;

import server.Snake.Enumerator.EEffect;
import server.Snake.Interface.IEntity;

public abstract class AbstractStaticEntity extends Entity {

    public AbstractStaticEntity(IEntity entity) {
        super(entity.getPositionX(), entity.getPositionY());
        entity.getEffects().forEach((effect, duration) -> {
            super.setEffect((EEffect) effect, (Integer) duration);
        });
    }

    public AbstractStaticEntity(float positionX, float positionY) {
        super(positionX, positionY);
    }

    public void onCollide(Object collider) {
        super.effects.forEach((k, v) -> {
            ((Player)collider).getSnake().setEffect(k, v);
        });
    };
}
