package server.Snake.Entity.Strategy;

import client.Snake.Entity.Player;
import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Interface.IMovingEntityBehaviour;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class NeutralMovement implements IMovingEntityBehaviour {

    @Override
    public void move(AbstractMovingEntity entity) {
        float posX = entity.getPositionX();
        float posY = entity.getPositionY();
        float[] directs = entity.getVelocity();
        // Determine a random x velocity
        int randx = ThreadLocalRandom.current().nextInt(-1, 2);
        if(randx == directs[0]*-1) randx = (int)directs[0];

        int randy = 0;
        if(randx == 0){ // If x = 0, determine a random y velocity
            randy = ThreadLocalRandom.current().nextInt(-1, 2);
        }
        entity.setVelocity(randx, randy);

        // Move the entity
        entity.setPositionX(posX + entity.getVelocityX());
        entity.setPositionY(posY + entity.getVelocityY());
    }

    @Override
    public void move(AbstractMovingEntity entity, Map<String, Player> players) {
        this.move(entity);
    }

    @Override
    public void move(AbstractMovingEntity entity, Map<String, Player> players, int[][] terrain) {
        // TODO: make that the entity doesn't go inside unreachable terrain (e.g. inside walls)
        this.move(entity);
    }
}
