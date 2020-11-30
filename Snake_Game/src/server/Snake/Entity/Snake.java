package server.Snake.Entity;

import client.Snake.Renderer.Interface.IDrawable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import server.Snake.Entity.Effect.EffectHandler;
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

    @Override
    @JsonIgnore
    public String getId(){
        return this.id;
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

            this.caretaker.addSnapshot(this.clone().createMemento());
        }

        EffectHandler.reactToEffect(this);
        return true;
    }

    @Override
    public void onCollide(Object collider) {
        if(collider.getClass() == Snake.class){
            if(!this.effects.containsKey(EEffect.STUN)){ // Don't apply stun if already stunned
                this.setEffect(EEffect.STUN, 10);
                this.deltaTailLength(-1);
            }
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
