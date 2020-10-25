package server.Snake.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public abstract class AbstractMovingEntity extends Entity {
    protected ArrayList<Float> previousPositionsX; // Previous horizontal positions.
    protected ArrayList<Float> previousPositionsY; // Previous vertical positions.
    protected float velocityX; // How fast is the entity going horizontally?
    protected float velocityY; // How fast is the entity going vertically?


    public AbstractMovingEntity(Entity entity){
        super(entity.positionX, entity.positionY);
    }

    public AbstractMovingEntity(float positionX, float positionY) {
        super(positionX, positionY);
        this.previousPositionsX = new ArrayList<Float>();
        this.previousPositionsY = new ArrayList<Float>();
        this.velocityX = 0;
        this.velocityY = 0;
    }

    public ArrayList getPreviousPositionsX() {
        return this.previousPositionsX;
    }

    public void setPreviousPositionsX(ArrayList<Float> previousPositionsX) {
        this.previousPositionsX = previousPositionsX;
    }

    public void AddPreviousPositionX(float previousPositionX) {
        this.previousPositionsX.add(0, previousPositionX);
    }

    public ArrayList getPreviousPositionsY() {
        return this.previousPositionsY;
    }

    public void setPreviousPositionsY(ArrayList<Float> previousPositionsY) {
        this.previousPositionsY = previousPositionsY;
    }

    public void AddPreviousPositionY(float previousPositionY) {
        this.previousPositionsY.add(0, previousPositionY);
    }

    @JsonIgnore
    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    @JsonIgnore
    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public float[] getVelocity() {
        return new float[] {this.velocityX, this.velocityY};
    }

    public void setVelocity(float velocityX, float velocityY){
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public abstract boolean move();

    public abstract void onCollide(Object collider);
}
