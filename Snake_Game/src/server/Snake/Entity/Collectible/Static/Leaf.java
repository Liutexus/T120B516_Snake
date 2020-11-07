package server.Snake.Entity.Collectible.Static;

import server.Snake.Entity.Entity;
import server.Snake.Entity.StaticEntityDecorator;
import server.Snake.Enumerator.EEffect;
import server.Snake.Interface.IEntity;

import java.awt.*;
import java.util.Map;

public class Leaf extends StaticEntityDecorator implements IEntity {

    public Leaf(Entity entity){
        super(entity);
        this.setColor(Color.GREEN);
    }

    @Override
    public Map<EEffect, Integer> getEffects(){
        super.effects.put(EEffect.POINT_INCREASE, 10);
        return super.getEffects();
    }

}
