package server.Snake.Entity.Obstacle.Static;

import server.Snake.Entity.Entity;
import server.Snake.Entity.StaticEntityDecorator;
import server.Snake.Enumerator.EEffect;
import server.Snake.Interface.IEntity;

import java.util.Map;

public class PoisonousBerry extends StaticEntityDecorator implements IEntity {

    public PoisonousBerry(Entity entity){
        super(entity);
    }

    @Override
    public Map<EEffect, Integer> getEffects(){
        super.effects.put(EEffect.POINT_DECREASE, 50);
        super.effects.put(EEffect.SLOW, 50);
        return super.getEffects();
    }

}
