package server.Snake.Interface;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.AbstractStaticEntity;

import java.util.Map;

public interface IFactory {
    AbstractMovingEntity createMoving(float positionX, float positionY);
    AbstractMovingEntity createMoving(float positionX, float positionY, Map players);
    AbstractStaticEntity createStatic(float positionX, float positionY);
}
