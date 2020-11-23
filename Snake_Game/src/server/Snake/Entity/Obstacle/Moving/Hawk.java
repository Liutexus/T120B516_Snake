package server.Snake.Entity.Obstacle.Moving;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.Player;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Strategy.HostileMovement;
import server.Snake.Interface.IEntity;
import server.Snake.Interface.IMovingEntityBehaviour;

import java.awt.*;
import java.util.Map;

public class Hawk extends AbstractMovingEntity implements IEntity {
    private IMovingEntityBehaviour movingStrategy;
    private Map<String, Player> players;
    private int stepCooldown = 2;
    private int currentStep = 0;

    public Hawk(Entity entity){
        super(entity);
        this.setColor(new Color(255, 0, 0));
        this.movingStrategy = new HostileMovement();
    }

    public Hawk(Entity entity, Map players){
        super(entity);
        this.setColor(new Color(255, 0, 0));
        this.movingStrategy = new HostileMovement();
        this.players = players;
    }

    @Override
    public boolean move() {
        this.currentStep++;
        if(this.currentStep >= stepCooldown){
            this.movingStrategy.move(this, players);
            this.currentStep = 0;
        }
        return true;
    }

    @Override
    public void onCollide(Object collider) {
        if(collider.getClass() == Player.class){
            ((Player)collider).deltaScore(-75);
            ((Player)collider).getSnake().deltaTailLength(-1);
        }
    }
}
