package client.Snake.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import server.Snake.Utility.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

public class Snake extends AbstractMovingEntity {
    private int tailLength;
    private ESnakeEffect boost = ESnakeEffect.NONE; // What boost player currently has?
    private int boostDuration; // How long does the boost last?
    private ESnakeEffect debuff = ESnakeEffect.NONE; // What debuffs player current has?
    private int debuffDuration; // How long does the debuff last?
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

    public void setBoost(ESnakeEffect boost, int duration){
        this.boost = boost;
        this.boostDuration = duration;
    }

    @JsonIgnore
    public ESnakeEffect getBoost(){
        return this.boost;
    }

    @JsonIgnore
    public int getBoostDuration() {
        return this.boostDuration;
    }

    public void setDebuff(ESnakeEffect debuff, int duration) {
        this.debuff = debuff;
        this.debuffDuration = duration;
    }

    @JsonIgnore
    public ESnakeEffect getDebuff(){
        return this.debuff;
    }

    @JsonIgnore
    public int getDebuffDuration() {
        return this.debuffDuration;
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
        Adapter.mapToSnake(this, map);
    }

    private void reactToEffect() {
        if(this.debuff == ESnakeEffect.STUN){
            this.debuffDuration--;
        }
        if(this.debuffDuration == 0){
            this.debuff = ESnakeEffect.NONE;
        }
    }

    @Override
    public boolean move() {
        if(this.debuff == ESnakeEffect.NONE){
            this.AddPreviousPositionX(positionX);
            this.AddPreviousPositionY(positionY);

            positionX += velocityX;
            positionY += velocityY;

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
                        break;
                    }
                }
            } catch (Exception e) {
                // Just to reduce some headache
                return false;
            }
        } else {
            reactToEffect();
        }

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
