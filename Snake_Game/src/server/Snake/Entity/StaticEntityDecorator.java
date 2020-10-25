package server.Snake.Entity;

import server.Snake.Enums.ESnakeEffect;
import server.Snake.Interface.IEntity;

public abstract class EntityDecorator implements IEntity {
    private final IEntity decoratedEntity;

    public EntityDecorator(IEntity entity){
        this.decoratedEntity = entity;
    }

    @Override
    public ESnakeEffect getBoost(){

    }

    @Override
    public int getBoostDuration(){

    }

    @Override
    public ESnakeEffect getDebuff(){

    }

    @Override
    public int getDebuffDuration(){

    }


}
