package server.Snake.Entity.Obstacle.Static;

import server.Snake.Entity.Entity;
import server.Snake.Entity.StaticEntityDecorator;
import server.Snake.Enumerator.EEffect;
import server.Snake.Interface.IEntity;

import java.util.Map;

public class BearTrap extends StaticEntityDecorator implements IEntity {

    public BearTrap(Entity entity){
        super(entity);
    }

    @Override
    public Map<EEffect, Integer> getEffects(){
        super.effects.put(EEffect.POINT_DECREASE, 100);
        super.effects.put(EEffect.STUN, 20);
        return super.getEffects();
    }

}
