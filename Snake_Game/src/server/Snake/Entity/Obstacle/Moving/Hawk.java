package server.Snake.Entity.Obstacle.Moving;

import server.Snake.Entity.AbstractMovingEntity;
import client.Snake.Entity.Player;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Strategy.HostileMovement;
import server.Snake.Handler;
import server.Snake.Interface.IEntity;
import server.Snake.Interface.IMovingEntityBehaviour;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Hawk extends AbstractMovingEntity implements IEntity {
    private IMovingEntityBehaviour movingStrategy;
    private Map<String, Player> players;

    public Hawk(Entity entity){
        super(entity);
        this.movingStrategy = new HostileMovement();
    }

    public Hawk(Entity entity, Map players){
        super(entity);
        this.movingStrategy = new HostileMovement();
        this.players = players;
    }

    @Override
    public boolean move() {
        this.movingStrategy.move(this);
        return true;
    }

    @Override
    public void onCollide(Object collider) {
        if(collider.getClass() == Player.class){
            ((Player)collider).deltaScore(-75);
        }
    }
}
