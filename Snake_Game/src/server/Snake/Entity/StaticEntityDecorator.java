package server.Snake.Entity;

import server.Snake.Enumerator.EEffect;
import server.Snake.Interface.IEntity;

import java.util.Map;

public abstract class StaticEntityDecorator extends AbstractStaticEntity implements IEntity {
    private final IEntity decoratedEntity;

    public StaticEntityDecorator(IEntity entity){
        super(entity);
        this.decoratedEntity = entity;
    }

    @Override
    public Map<EEffect, Integer> getEffects(){
        return decoratedEntity.getEffects();
    }
}
