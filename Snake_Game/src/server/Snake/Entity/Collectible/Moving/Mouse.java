package server.Snake.Entity.Collectible.Moving;

import server.Snake.Entity.Player;
import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Strategy.ScaredMovement;
import server.Snake.Interface.IEntity;
import server.Snake.Interface.IMovingEntityBehaviour;

import java.awt.*;
import java.util.Map;

public class Mouse extends AbstractMovingEntity implements IEntity {
    private IMovingEntityBehaviour movingStrategy;
    private Map<String, Player> players;
    private int stepCooldown = 2;
    private int currentStep = 0;

    public Mouse(Entity entity) {
        super(entity);
        this.setColor(new Color(173, 90, 0));
        this.movingStrategy = new ScaredMovement();
    }

    public Mouse(Entity entity, Map players){
        super(entity);
        this.setColor(new Color(173, 90, 0));
        this.movingStrategy = new ScaredMovement();
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

    }
}
