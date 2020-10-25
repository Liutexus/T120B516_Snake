package server.Snake.Entity.Collectible.Static;

import server.Snake.Entity.Entity;
import server.Snake.Entity.StaticEntityDecorator;
import server.Snake.Enums.EEffect;
import server.Snake.Interface.IEntity;

import java.util.Map;

public class Reverse extends StaticEntityDecorator implements IEntity {

    public Reverse(Entity entity){
        super(entity);
    }

    @Override
    public Map<EEffect, Integer> getEffects(){
        super.effects.put(EEffect.ROLLBACK, 10);
        return super.getEffects();
    }

}
