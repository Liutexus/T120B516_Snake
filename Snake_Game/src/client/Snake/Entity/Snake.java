package client.Snake.Entity;

import client.Snake.Player;

import java.util.ArrayList;

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

    @Override
    public boolean move() {
        this.previousPositionsX.add(positionX);
        this.previousPositionsY.add(positionY);

        positionX += velocityX;
        positionY += velocityY;

        // TODO: Check if new position do not collide with previous positions or another player.
        return true;
    }

    // TODO: Implement Snake<->Snake collision handling.
    @Override
    public void onCollide(Player player) {

    }
}
