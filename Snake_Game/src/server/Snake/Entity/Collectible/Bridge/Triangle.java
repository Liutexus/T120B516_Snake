package server.Snake.Entity.Collectible.Bridge;
import java.awt.*;
import server.Snake.Interface.IColor;

public class Triangle extends Shape{
    int points;
    public Triangle(IColor c) {
        super(c);
        this.points = 3;
    }

    @Override
    public void applyColor(Graphics g) {
        color.applyColor(g);
    }

    @Override
    public int getPoints() {
        return this.points;
    }

}