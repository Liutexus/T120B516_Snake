package server.Snake.Entity.Effect;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Player;
import server.Snake.Entity.Snake;
import server.Snake.Enumerator.EEffect;
import server.Snake.GameLogic;

import java.util.Map;

public class EffectHandler {

    public static void reactToEffect(Entity entity) {
        // TODO: Add checks for every effect and respond accordingly
        Map<EEffect, Integer> effects = entity.getEffects();

        if(effects.containsKey(EEffect.POINT_INCREASE)){
            // TODO: Add points to player
        }

        if(effects.containsKey(EEffect.TAIL_INCREASE)){
            if(entity instanceof Snake)
                ((Snake)entity).deltaTailLength(1);
        }

        if(effects.containsKey(EEffect.TAIL_DECREASE)){
            if(entity instanceof Snake)
                ((Snake)entity).deltaTailLength(-1);
        }

        if(effects.containsKey(EEffect.SIZE_UP)){
            entity.setSizeX(entity.getSizeX() + effects.get(EEffect.SIZE_UP));
            entity.setSizeY(entity.getSizeY() + effects.get(EEffect.SIZE_UP));
        }

        if(effects.containsKey(EEffect.ROLLBACK)){
            entity.setMemento(entity.getCaretaker().get());
        }

        if(effects.containsKey(EEffect.HASTE) && !effects.containsKey(EEffect.STUN) && !effects.containsKey(EEffect.ROLLBACK)){

        }

        effects.forEach((effect, duration) -> {
            effects.put(effect, effects.get(effect) - 1);
            if(duration <= 0) effects.remove(effect);
        });
    }

}
