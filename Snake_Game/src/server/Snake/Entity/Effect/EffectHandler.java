package server.Snake.Entity.Effect;

import server.Snake.Entity.Entity;
import server.Snake.Entity.Snake;
import server.Snake.Enumerator.EEffect;

public class EffectHandler {

    public static void reactToEffect(Entity entity) {
        // TODO: Add checks for every effect and respond accordingly

        if(entity.getEffects().containsKey(EEffect.POINT_INCREASE)){
            // TODO: Add points to player
        }

        if(entity.getEffects().containsKey(EEffect.TAIL_INCREASE)){
            if(entity instanceof Snake)
                ((Snake)entity).deltaTailLength(1);
        }

        if(entity.getEffects().containsKey(EEffect.TAIL_DECREASE)){
            if(entity instanceof Snake)
                ((Snake)entity).deltaTailLength(-1);
        }

        if(entity.getEffects().containsKey(EEffect.SIZE_UP)){
            entity.setSizeX(entity.getSizeX() + entity.getEffects().get(EEffect.SIZE_UP));
            entity.setSizeY(entity.getSizeY() + entity.getEffects().get(EEffect.SIZE_UP));
        }

        if(entity.getEffects().containsKey(EEffect.ROLLBACK)){
            entity.setMemento(entity.getCaretaker().get());
        }

        if(entity.getEffects().containsKey(EEffect.HASTE)){
//            this.AddPreviousPositionX(positionX);
//            this.AddPreviousPositionY(positionY);
//
//            positionX += velocityX;
//            positionY += velocityY;
        }

        entity.getEffects().forEach((effect, duration) -> {
            entity.getEffects().put(effect, entity.getEffects().get(effect) - 1);
            if(duration <= 0) entity.getEffects().remove(effect);
        });
    }

}
