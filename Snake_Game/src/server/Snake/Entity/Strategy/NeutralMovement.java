package server.Snake.Entity.Strategy;

import client.Snake.Entity.Player;
import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Interface.IMovingEntityBehaviour;

import java.util.Map;

public class NeutralMovement implements IMovingEntityBehaviour {

    @Override
    public void move(AbstractMovingEntity entity) {

    }

    @Override
    public void move(AbstractMovingEntity entity, Map<String, Player> players) {

    }

    @Override
    public void move(AbstractMovingEntity entity, Map<String, Player> players, int[][] terrain) {

    }
}
