package server.Snake.Interface;

public interface IEntity {
    float getPositionX();
    void setPositionX(float positionX);
    float getPositionY();
    void setPositionY(float positionY);
    float[] getPosition();
    void setPosition(float posX, float posY);
    void nextPosition(int minX, int minY, int maxX, int maxY);
    float getSizeX();
    void setSizeX(float sizeX);
    float getSizeY();
    void setSizeY(float sizeY);
    float[] getSize();
    void setSize(float sizeX, float sizeY);
    void deltaSize(float sizeX, float sizeY);
}
