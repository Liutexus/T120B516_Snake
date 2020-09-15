package client.Snake.Entities;

public interface Entity {

    public float[] getPosition();
    public void setPosition(float x, float y);

    public void nextPosition(float minX, float minY, float maxX, float maxY);

    public float[] getSize();
    public void setSize(float sizeX, float sizeY);
    public void deltaSize(float sizeX, float sizeY);

}
