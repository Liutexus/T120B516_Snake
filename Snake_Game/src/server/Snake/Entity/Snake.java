package server.Snake.Entity;

import client.Snake.Renderer.Interface.IDrawable;
import server.Snake.Entity.Memento.Caretaker;
import server.Snake.Entity.Memento.Memento;
import server.Snake.Enumerator.EEffect;
import server.Snake.Interface.IVisitor;

import java.awt.*;
import java.util.ArrayList;

public class Snake extends AbstractMovingEntity implements Cloneable, IDrawable {
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
        } else {
            this.tailLength = 0;
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

        if(this.effects.containsKey(EEffect.POINT_INCREASE)){
            // TODO: Add points to player
        }

        if(this.effects.containsKey(EEffect.TAIL_INCREASE))
            this.deltaTailLength(1);

        if(this.effects.containsKey(EEffect.TAIL_DECREASE))
            this.deltaTailLength(-1);

        if(this.effects.containsKey(EEffect.SIZE_UP)){
            this.setSizeX(this.getSizeX() + this.effects.get(EEffect.SIZE_UP));
            this.setSizeY(this.getSizeY() + this.effects.get(EEffect.SIZE_UP));
        }

        if(this.effects.containsKey(EEffect.ROLLBACK)){
            this.setMemento(this.caretaker.get());
        } else {
            caretaker.addSnapshot(this.clone().createMemento()); // Add a state of current snake
        }

        if(this.effects.containsKey(EEffect.HASTE)){
            this.AddPreviousPositionX(positionX);
            this.AddPreviousPositionY(positionY);

            positionX += velocityX;
            positionY += velocityY;
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

        return true;
    }

    @Override
    public void onCollide(Object collider) {
        try {
            if(collider.getClass() == Snake.class){
                // If both snakes are going one at another, stop/stun/etc. them
                System.out.println(this.positionX + " : " + this.positionY);
                if(
                    (this.positionX + this.velocityX == ((Snake)collider).getPositionX() &&
                    this.positionY + this.velocityY == ((Snake)collider).getPositionY()) ||
                    (this.positionX == ((Snake)collider).getPositionX() + ((Snake)collider).getVelocityX() &&
                    this.positionY == ((Snake)collider).getPositionY() + ((Snake)collider).getVelocityY()))
                {
                    if(
                        ((this.velocityX == -((Snake)collider).getVelocityX()) || // Are players going one to another
                        (this.velocityY == -((Snake)collider).getVelocityY())) &&
                        (!this.effects.containsKey(EEffect.STUN) && !((Snake)collider).getEffects().containsKey(EEffect.STUN)) // Don't apply debuffs if they are already debuffed
                    )
                    {
                        this.setEffect(EEffect.STUN, 10);
                        this.deltaTailLength(-1);
                        ((Snake) collider).setEffect(EEffect.STUN, 10);
                    }
                }

                // Checking collisions with the tail
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

    @Override
    public void drawRect(Graphics g, int windowWidth, int windowHeight, int cellWidth, int cellHeight) {
        g.setColor(getColor());
        int cellPositionX = ((int)(getPositionX()) * windowWidth)/50;
        int cellPositionY = ((int)(getPositionY()) * windowHeight)/50;
        g.fillRect((int)cellPositionX, (int)cellPositionY, cellWidth*(int)(getSizeX()), cellHeight*(int)(getSizeY()));

        ArrayList prevPosX = getPreviousPositionsX();
        ArrayList prevPosY = getPreviousPositionsY();

            for(int i = 0; i < this.tailLength; i++){
                try{
                int colorStepR = this.getColor().getRed() / (this.tailLength + 1);
                int colorStepG = this.getColor().getGreen() / (this.tailLength + 1);
                int colorStepB = this.getColor().getBlue() / (this.tailLength + 1);
                Color tailColor = new Color(
                        colorStepR * (this.tailLength - i),
                        colorStepG * (this.tailLength - i),
                        colorStepB * (this.tailLength - i)
                );
                g.setColor(tailColor);
                float[] pos = new float[]{(Float.parseFloat(prevPosX.get(i).toString())),
                        Float.parseFloat(prevPosY.get(i).toString())};
                cellPositionX = ((int)(pos[0]) * windowWidth)/50;
                cellPositionY = ((int)(pos[1]) * windowHeight)/50;
                g.fillRect(cellPositionX, cellPositionY, cellWidth*(int)(getSizeX()), cellHeight*(int)(getSizeY()));

                } catch (Exception e) {
//                    e.printStackTrace();
                    // Just to reduce headache from exceptions at the start of the game
                    // when there's not enough previous positions to draw tail from.
                    break;
                }
            }

    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
