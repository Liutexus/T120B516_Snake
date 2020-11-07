package server.Snake.Entity.Collectible.Moving;

import server.Snake.Entity.Player;
import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Strategy.ScaredMovement;
import server.Snake.Interface.IEntity;
import server.Snake.Interface.IMovingEntityBehaviour;

import java.util.Map;

public class Mouse extends AbstractMovingEntity implements IEntity {
    private IMovingEntityBehaviour movingStrategy;
    private Map<String, Player> players;

    public Mouse(Entity entity) {
        super(entity);
    }

    public Mouse(Entity entity, Map players){
        super(entity);
        this.movingStrategy = new ScaredMovement();
        this.players = players;
    }

    @Override
    // TODO: Possibly modify movement behaviour to avoid snakes.
    public boolean move() {
        this.movingStrategy.move(this, players);
        return true;
    }

    @Override
    public void onCollide(Object collider) {

    }
}
