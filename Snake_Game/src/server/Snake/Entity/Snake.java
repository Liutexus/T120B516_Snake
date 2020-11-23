package server.Snake.Entity;

import server.Snake.Entity.Memento.Caretaker;
import server.Snake.Entity.Memento.Memento;
import server.Snake.Enumerator.EEffect;

import java.util.ArrayList;

public class Snake extends AbstractMovingEntity implements Cloneable {
    private int tailLength;
    private String terrain; // What terrain is the player standing on?

    public Snake(float positionX, float positionY) {
        super(positionX, positionY);
        this.tailLength = 10;

        this.caretaker = new Caretaker();
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

        this.tailLength = Math.min(this.tailLength, maxLength);
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
        // TODO: Add checks for every effect and respond accordingly
        if(this.effects.containsKey(EEffect.STUN)){
//            this.effects.replace(EEffect.STUN, this.effects.get(EEffect.STUN) - 1);
        }

        if(this.effects.containsKey(EEffect.ROLLBACK)){
            // TODO: Try to implement Memento pattern here
        }

        if(this.effects.containsKey(EEffect.POINT_INCREASE)){
            // TODO: Add points to player
        }

        if(this.effects.containsKey(EEffect.SIZE_UP)){
            this.setSizeX(this.getSizeX() + this.effects.get(EEffect.SIZE_UP));
            this.setSizeY(this.getSizeY() + this.effects.get(EEffect.SIZE_UP));
        }

        if(this.effects.containsKey(EEffect.ROLLBACK)){
            this.setMemento(this.caretaker.get());
        }

        this.effects.forEach((effect, duration) -> {
            this.effects.replace(effect, this.effects.get(effect) - 1);
            if(duration <= 0) effects.remove(effect);
        });
    }

    @Override
    public Memento createMemento(){
        return new Memento(this);
    }

    @Override
    public void setMemento(Memento memento){
        super.setMemento(memento);
        this.tailLength = (((Snake) memento.getState()).getTailLength());
        this.terrain = (((Snake) memento.getState()).terrain);
    }

    @Override
    public boolean move() {
        if(!this.effects.containsKey(EEffect.STUN) && !this.effects.containsKey(EEffect.ROLLBACK)){
            this.AddPreviousPositionX(positionX);
            this.AddPreviousPositionY(positionY);

            positionX += velocityX;
            positionY += velocityY;

        }

        reactToEffect();

        checkCollisionWithTail();

        if(!this.effects.containsKey(EEffect.ROLLBACK))
            caretaker.addSnapshot(this.clone().createMemento()); // Add a state of current snake
        System.out.println(this.effects);
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

    public Snake clone(){
        try{
            return (Snake) super.clone();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Couldn't clone '" + this.getClass() + "' class.");
        }
        return null;
    }
}
