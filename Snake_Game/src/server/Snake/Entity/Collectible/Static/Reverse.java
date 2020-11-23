package server.Snake.Entity.Collectible.Static;

import server.Snake.Entity.Entity;
import server.Snake.Entity.StaticEntityDecorator;
import server.Snake.Enumerator.EEffect;
import server.Snake.Interface.IEntity;

import java.awt.*;
import java.util.Map;

public class Reverse extends StaticEntityDecorator implements IEntity {

    public Reverse(Entity entity){
        super(entity);
        super.setColor(Color.BLUE);
    }

    @Override
    public Map<EEffect, Integer> getEffects(){
        super.getEffects().put(EEffect.ROLLBACK, 30);
        return super.getEffects();
    }

}
