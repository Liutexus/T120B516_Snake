package server.Snake.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import server.Snake.Entity.Memento.Caretaker;
import server.Snake.Entity.Memento.Memento;
import server.Snake.Enumerator.EEffect;
import server.Snake.Interface.IEntity;
import server.Snake.Utility.Adapter;
import server.Snake.Utility.Visitor.IVisitable;
import server.Snake.Utility.Visitor.Visitor;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Entity implements IEntity, IVisitable {
    protected float positionX; // Current player horizontal position
    protected float positionY; // Current player vertical position
    protected float sizeX; // How big is the player by X axis
    protected float sizeY; // How big is the player by Y axis

    protected Color color = Color.BLACK; // What color is the entity?

    protected Map<EEffect, Integer> effects = new ConcurrentHashMap<>();

    protected Caretaker caretaker;

    public Entity(float positionX, float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.sizeX = 1;
        this.sizeY = 1;
    }

    @Override
    public void accept(Visitor visitor) {
        //visitor.mapToEntity();
    }

    @JsonIgnore
    public float getPositionX() {
        return this.positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    @JsonIgnore
    public float getPositionY() {
        return this.positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public float[] getPosition() {
        return new float[] {positionX, positionY};
    }

    public void setPosition(float posX, float posY) {
        this.positionX = posX;
        this.positionY = posY;
    }

    public void nextPosition(int minX, int minY, int maxX, int maxY) {
        this.positionX = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
        this.positionY = ThreadLocalRandom.current().nextInt(minY, maxY + 1);
    }

    @JsonIgnore
    public float getSizeX() {
        return this.sizeX;
    }

    public void setSizeX(float sizeX) {
        this.sizeX = sizeX;
    }

    @JsonIgnore
    public float getSizeY() {
        return this.sizeY;
    }

    public void setSizeY(float sizeY) {
        this.sizeY = sizeY;
    }

    public float[] getSize() {
        return new float[] {sizeX, sizeY};
    }

    public void setSize(float sizeX, float sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public void deltaSize(float sizeX, float sizeY) {
        this.sizeX += sizeX;
        this.sizeY += sizeY;
    }

    @JsonIgnore
    public Color getColor() {
        return this.color;
    }

    public int getColorRGB() { return this.color.getRGB();}

    public void setColor(Color color) {
        this.color = color;
    }

    public void setEffect(EEffect effect, int duration){
        if(!this.effects.containsKey(effect)) this.effects.put(effect, duration);
        else this.effects.replace(effect, duration);
    }

    @JsonIgnore
    public Map<EEffect, Integer> getEffects(){
        return this.effects;
    }

    @Override
    public Memento createMemento() {
        return new Memento(this);
    }

    @Override
    public void setMemento(Memento memento) {
        this.positionX = (((Entity) memento.getState()).getPositionX());
        this.positionY = (((Entity) memento.getState()).getPositionY());
        this.sizeX = (((Entity) memento.getState()).getSizeX());
        this.sizeY = (((Entity) memento.getState()).getSizeY());
        this.color = (((Entity) memento.getState()).getColor());
        this.effects = (((Entity) memento.getState()).getEffects());
    }

    @Override
    public String toString() {
        return Adapter.entityToString(this);
    }

}
