package server.Snake.Interface;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.AbstractStaticEntity;

import java.util.Map;

public interface IEntityFactory {
    AbstractMovingEntity createMoving(float positionX, float positionY);
    AbstractMovingEntity createMoving(float positionX, float positionY, Map players);
    AbstractMovingEntity createMoving(float positionX, float positionY, Map players, int[][] terrain);
    AbstractStaticEntity createStatic(float positionX, float positionY);
}
