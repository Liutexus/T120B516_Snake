package server.Snake.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import server.Snake.Entity.Collectible.Bridge.BlueColor;
import server.Snake.Entity.Collectible.Bridge.RedColor;
import server.Snake.Entity.Collectible.Bridge.Triangle;
import server.Snake.Entity.Collectible.Bridge.Polygon;
import server.Snake.Entity.Collectible.Bridge.Shape;
import server.Snake.Enumerator.EEffect;
import server.Snake.Interface.IEntity;
import server.Snake.Utility.Adapter;

import java.awt.*;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Entity implements IEntity {
    protected float positionX;   // Current player horizontal position
    protected float positionY;   // Current player vertical position
    protected float sizeX; // How big is the player by X axis
    protected float sizeY; // How big is the player by Y axis

    public server.Snake.Entity.Collectible.Bridge.Shape shape;
    public int shapetype;

    protected Map<EEffect, Integer> effects = new ConcurrentHashMap<>();

    public Entity(float positionX, float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.sizeX = 1;
        this.sizeY = 1;
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

    public void setShape(Shape s) {
        this.shape = s;
    }

    public void setShapetype(int nr) {
        this.shapetype = nr;
    }

    public int getShapetype() {
        return this.shapetype;
    }

    public void deltaSize(float sizeX, float sizeY) {
        this.sizeX += sizeX;
        this.sizeY += sizeY;
    }

    public void setEffect(EEffect effect, int duration){
        if(!this.effects.containsKey(effect)) this.effects.put(effect, duration);
        else this.effects.replace(effect, duration);
    }

    public Shape getShape(){
        return this.shape;
    }

    @JsonIgnore
    public Map<EEffect, Integer> getEffects(){
        return this.effects;
    }

    @Override
    public String toString() {
        return Adapter.entityToString(this);
    }

}