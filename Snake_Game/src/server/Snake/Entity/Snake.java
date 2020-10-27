package server.Snake.Entity;

import server.Snake.Enumerator.EEffect;

import java.util.ArrayList;

public class Snake extends AbstractMovingEntity {
    private int tailLength;
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
        try {
            for (int i = 0; i < this.tailLength; i++) {
                // Did the snake bite itself?
                if(this.previousPositionsX.get(i) == positionX &&
                        this.previousPositionsY.get(i) == positionY &&
                        (this.velocityX != 0 ||
                                this.velocityY != 0)) {
                    int initTailSize = this.tailLength;
                    this.deltaTailLength(-(initTailSize - i));
                    // TODO: Point/health reduction
                    return true;
                }
            }
        } catch (Exception e) {
            // Just to reduce some headache
        }
        return false;
    }

    private void reactToEffect() {
        if(this.effects.containsKey(EEffect.STUN)){
            this.effects.replace(EEffect.STUN, this.effects.get(EEffect.STUN) - 1);
        }
        // TODO: Add checks for every effect and respond accordingly

        this.effects.forEach((k, v) -> {
            v--;
            if(v <= 0) effects.remove(k);
        });
    }

    @Override
    public boolean move() {
        if(this.effects.size() == 0){
            this.AddPreviousPositionX(positionX);
            this.AddPreviousPositionY(positionY);

            positionX += velocityX;
            positionY += velocityY;

        } else {
            reactToEffect();
        }
        checkCollisionWithTail();

        return true;
    }

    @Override
    public void onCollide(Object collider) {
        try {
            if(collider.getClass() == Snake.class){
                // TODO: If both snakes are going one at another, stop/stun/etc. them
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
