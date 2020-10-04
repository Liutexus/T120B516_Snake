package client.Snake.Entity;

import java.util.ArrayList;

public abstract class AbstractMovingEntity extends Entity {

    protected ArrayList<Float> previousPositionsX; // Previous horizontal positions.
    protected ArrayList<Float> previousPositionsY; // Previous vertical positions.
    protected float velocityX; // How fast is the entity going horizontally?
    protected float velocityY; // How fast is the entity going vertically?

    public ArrayList getPreviousPositionsX() {
        return this.previousPositionsX;
    }

    public void setPreviousPositionsX(ArrayList<Float> previousPositionsX) {
        this.previousPositionsX = previousPositionsX;
    }

    public void AddPreviousPositionX(float previousPositionX) {
        this.previousPositionsX.add(previousPositionX);
    }

    public ArrayList getPreviousPositionsY() {
        return this.previousPositionsY;
    }

    public void setPreviousPositionsY(ArrayList<Float> previousPositionsY) {
        this.previousPositionsY = previousPositionsY;
    }

    public void AddPreviousPositionY(float previousPositionY) {
        this.previousPositionsY.add(previousPositionY);
    }

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public void setVelocity(float velocityX, float velocityY){
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public abstract boolean move();
}
