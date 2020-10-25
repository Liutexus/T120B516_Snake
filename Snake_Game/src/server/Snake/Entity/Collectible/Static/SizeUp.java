package server.Snake.Entity.Collectible.Static;

import server.Snake.Entity.Entity;
import server.Snake.Entity.StaticEntityDecorator;
import server.Snake.Enums.EEffect;
import server.Snake.Interface.IEntity;

import java.util.Map;

public class SizeUp extends StaticEntityDecorator implements IEntity {

    public SizeUp(Entity entity){
        super(entity);
    }

    @Override
    public Map<EEffect, Integer> getEffects(){
        super.effects.put(EEffect.SIZE_UP, 1);
        return super.getEffects();
    }

}
