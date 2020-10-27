package server.Snake.Entity.Collectible.Bridge;

import server.Snake.Interface.IColor;
import java.awt.*;

public class Polygon extends Shape{
    int points;
    public Polygon(IColor c) {
        super(c);
        this.points = 6;
    }

    @Override
    public void applyColor(Graphics g) {
        color.applyColor(g);
    }

    public int getPoints(){
        return this.points;
    }

}