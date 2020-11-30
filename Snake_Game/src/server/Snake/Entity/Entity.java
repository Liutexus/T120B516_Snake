package server.Snake.Entity;

import client.Snake.Renderer.Interface.IDrawable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import server.Snake.Entity.Memento.Caretaker;
import server.Snake.Entity.Memento.Memento;
import server.Snake.Enumerator.EEffect;
import server.Snake.Interface.IEntity;
import server.Snake.Interface.IVisitor;
import server.Snake.Utility.Adapter;
import server.Snake.Interface.IVisitable;
import server.Snake.Utility.MapToObjectVisitor;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Entity implements IEntity, IDrawable, IVisitable, Cloneable {
    protected String id;
    protected float positionX; // Current player horizontal position
    protected float positionY; // Current player vertical position
    protected float sizeX; // How big is the player by X axis
    protected float sizeY; // How big is the player by Y axis

    protected Color color = Color.BLACK; // What color is the entity?

    protected Map<EEffect, Integer> effects = new ConcurrentHashMap<>();

    protected Caretaker caretaker;

    public Entity(){
        this.id = "";
        this.positionX = 0;
        this.positionY = 0;
        this.sizeX = 1;
        this.sizeY = 1;
    }

    public Entity(String id){
        this.id = id;
        this.positionX = 0;
        this.positionY = 0;
        this.sizeX = 1;
        this.sizeY = 1;
    }

    public Entity(float positionX, float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.sizeX = 1;
        this.sizeY = 1;
    }

    public Entity(String id, float positionX, float positionY){
        this.id = id;
        this.positionX = positionX;
        this.positionY = positionY;
        this.sizeX = 1;
        this.sizeY = 1;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
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

    @JsonIgnore
    public Caretaker getCaretaker(){
        return this.caretaker;
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
    public void drawRect(Graphics g, int windowWidth, int windowHeight, int cellWidth, int cellHeight) {
        int cellPositionX = ((int)(getPositionX()) * windowWidth)/50;
        int cellPositionY = ((int)(getPositionY()) * windowHeight)/50;
        g.setColor(getColor());
        g.fillRect(cellPositionX, cellPositionY, cellWidth*(int)(getSizeX()), cellHeight*(int)(getSizeY()));
    }

    @Override
    public String toString() {
        return Adapter.entityToString(this);
    }

    public Entity clone(){
        try{
            return (Entity) super.clone();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Couldn't clone '" + this.getClass() + "' class.");
        }
        return null;
    }

    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
