package server.Snake.Interface;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.AbstractStaticEntity;

import java.util.Map;

public interface IEntityFactory {
    AbstractMovingEntity createMoving(String id, float positionX, float positionY, Map players, int[][] terrain);
    AbstractStaticEntity createStatic(String id, float positionX, float positionY);
}
