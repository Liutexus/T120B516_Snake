package client.Snake.Entity;

import client.Snake.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Snake extends AbstractMovingEntity {
    private int tailLength;
    private String boost; // What boost player currently has?
    private String terrain; // What terrain is the player standing on?

    public Snake(float positionX, float positionY) {
        super(positionX, positionY);
        this.tailLength = 10;
    }

    public int getTailLength () {
        return this.tailLength;
    }

    public void setTailLength (int tailLength) {
        this.tailLength = tailLength;
    }

    public void deltaTailLength(int delta){
        if(this.tailLength + delta >= 0){
            this.tailLength += delta;
        }
    }

    public void trimTailSizeAndPrevPos(int maxLength) {
        if(previousPositionsX.size() < maxLength || previousPositionsY.size() < maxLength)
            return;

        this.tailLength = maxLength;
        ArrayList tempX = new ArrayList();
        ArrayList tempY = new ArrayList();
        for(int i = 0; i < maxLength; i++){
            tempX.add(previousPositionsX.get(i));
            tempY.add(previousPositionsY.get(i));
        }
        previousPositionsX = tempX;
        previousPositionsY = tempY;
    }

    public boolean checkCollisionWithTail() {
        for(int i = 0; i < this.tailLength; i++){
            if(velocityX != 0 || velocityY != 0) // is player moving?
                if(positionX == (float)previousPositionsY.get(i) && positionY == (float)previousPositionsY.get(i))
                    return true;
        }
        return false;
    }

    public void mapToObject(HashMap<String, Object> map) {
        map.forEach((field, value) -> {
            ArrayList<Object> temp;
            switch (field){
                case "previousPositionsX":
                    this.previousPositionsX = (ArrayList<Float>)value;
                    break;
                case "previousPositionsY":
                    this.previousPositionsY = (ArrayList<Float>)value;
                    break;
                case "tailLength":
                    this.tailLength = (int)value;
                    break;
                case "velocity":
                    temp = (ArrayList<Object>) value;
                    this.velocityX = (float)(double)temp.get(0);
                    this.velocityY = (float)(double)temp.get(1);
                    break;
                case "size":
                    temp = (ArrayList<Object>) value;
                    this.sizeX = (float)(double)temp.get(0);
                    this.sizeY = (float)(double)temp.get(1);
                    break;
                case "position":
                    temp = (ArrayList<Object>) value;
                    this.positionX = (float)(double)temp.get(0);
                    this.positionY = (float)(double)temp.get(1);
                    break;
                default:
                    System.out.println("Attribute: '" + field + "' is not recognised.");
                    break;
            }
        });

    }

    @Override
    public boolean move() {
        this.AddPreviousPositionX(positionX);
        this.AddPreviousPositionY(positionY);

        positionX += velocityX;
        positionY += velocityY;

        try {
            for (int i = 0; i < this.tailLength; i++) {
                if(this.previousPositionsX.get(i) == positionX &&
                        this.previousPositionsY.get(i) == positionY &&
                        (this.velocityX != 0 ||
                        this.velocityY != 0)) {
                    int initTailSize = this.tailLength;
                    this.deltaTailLength(-(initTailSize - i));
                    // TODO: Point/health reduction
                    break;
                }
            }
        } catch (Exception e) {
            // Just to reduce some headache
        }

        return true;
    }

    @Override
    public void onCollide(Object collider) {
        try {
            if(collider.getClass() == Snake.class){
                for (int i = 0; i < ((Snake)collider).tailLength; i++){
                    if(((Snake)collider).previousPositionsX.get(i) == this.positionX &&
                            ((Snake)collider).previousPositionsY.get(i) == this.positionY) {
                        int initTailSize = ((Snake)collider).tailLength;
                        ((Snake)collider).deltaTailLength(-(initTailSize - i));
                        this.deltaTailLength(initTailSize - i);
                        // TODO: Point/health distribution
                        break;
                    }
                }
            }
        } catch (Exception e) {
            // TODO fix: This throws an exception at beginning of the match.
//            System.out.println("Error at checking collisons for snake at (x: " + this.positionX + ", y: " + this.positionY + ").");
//            e.printStackTrace();
        }
    }
}
