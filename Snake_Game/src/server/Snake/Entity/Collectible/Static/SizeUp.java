package server.Snake.Entity.Collectible.Static;

import server.Snake.Entity.Entity;
import server.Snake.Entity.StaticEntityDecorator;
import server.Snake.Enumerator.EEffect;
import server.Snake.Interface.IEntity;

import java.awt.*;
import java.util.Map;

public class SizeUp extends StaticEntityDecorator implements IEntity {

    public SizeUp(Entity entity){
        super(entity);
        super.setColor(Color.ORANGE);
    }

    @Override
    public Map<EEffect, Integer> getEffects(){
        super.getEffects().put(EEffect.SIZE_UP, 1);
        super.getEffects().put(EEffect.TAIL_INCREASE, 1);
        return super.getEffects();
    }

}
