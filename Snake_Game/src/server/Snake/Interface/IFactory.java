package server.Snake.Interface;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.AbstractStaticEntity;

public interface IFactory {
    AbstractMovingEntity createMoving(float positionX, float positionY);
    AbstractStaticEntity createStatic(float positionX, float positionY);
}
