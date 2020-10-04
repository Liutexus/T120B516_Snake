package client.Snake.Entity;

import java.util.concurrent.ThreadLocalRandom;

public class Entity {
    float posX, sizeX;
    float posY, sizeY;

    public float[] getPosition() {
        return new float[] {posX, posY};
    }

    public void setPosition(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void nextPosition(int minX, int minY, int maxX, int maxY) {
        this.posX = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
        this.posY = ThreadLocalRandom.current().nextInt(minY, maxY + 1);
    }

    public float[] getSize() {
        return new float[] {sizeX, sizeY};
    }

    public void setSize(float sizeX, float sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public void deltaSize(float sizeX, float sizeY) {
        this.sizeX += sizeX;
        this.sizeY += sizeY;
    }
}