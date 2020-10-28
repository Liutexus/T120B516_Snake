package server.Snake.Entity.Collectible.Bridge;

import server.Snake.Interface.IColor;

import java.awt.*;

public class BlueColor implements IColor {

    public void applyColor(Graphics g){
        g.setColor(new Color(0,0,255));
    }

}