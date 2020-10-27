package server.Snake.Entity.Collectible.Bridge;

import server.Snake.Interface.IColor;

import java.awt.*;

public abstract class Shape {
    //Composition - implementor
    protected IColor color;
    //constructor with implementor as input argument
    public Shape(IColor c){
        this.color=c;
    }

    abstract public void applyColor(Graphics g);
    abstract public int getPoints();
}