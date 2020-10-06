package client.Snake.Entity;

public interface IFactory {
    public AbstractMovingEntity createMoving(float positionX, float positionY);
    public AbstractStaticEntity createStatic(float positionX, float positionY);
}
