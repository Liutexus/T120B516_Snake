package server.Snake.Interface;

import server.Snake.Entity.Player;
import server.Snake.Entity.AbstractMovingEntity;

import java.util.Map;

public interface IMovingEntityBehaviour {
    void move(AbstractMovingEntity entity, int[][] terrain);
    void move(AbstractMovingEntity entity, Map<String, Player> players);
    void move(AbstractMovingEntity entity, Map<String, Player> players, int[][] terrain);
}
