package client.Snake.Entities;

import java.util.concurrent.ThreadLocalRandom;

public class Food {
    private float posX;
    private float posY;
    private float sizeX;
    private float sizeY;
    private int scoreAdd;
    private String boostAdd; // What boost should it give to the player?

    public Food(float x, float y){
        this.posX = x;
        this.posY = y;

        this.sizeX = 1;
        this.sizeY = 1;
        this.scoreAdd = 1;
        this.boostAdd = null;
    }

    public void nextPosition(float maxX, float maxY){
        posX = ThreadLocalRandom.current().nextInt(0, ((int)maxX) + 1);
        posY = ThreadLocalRandom.current().nextInt(0, ((int)maxY) + 1);
    }

    public void nextPosition(float minX, float minY, float maxX, float maxY){
        posX = ThreadLocalRandom.current().nextInt(((int)minX), ((int)maxX));
        posY = ThreadLocalRandom.current().nextInt(((int)minY), ((int)maxY));
    }

    public void setPosition(float x, float y){
        posX = x;
        posY = y;
    }

    public float[] getPosition(){
        return new float[] {this.posX, this.posY};
    }

    public int getScoreValue(){
        return scoreAdd;
    }





}
