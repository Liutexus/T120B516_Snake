package server.Snake.Entity.Collectible.Bridge;

import server.Snake.Interface.IColor;

import java.awt.*;

public class RedColor implements IColor {

    public void applyColor(Graphics g){
        g.setColor(new Color(255,0,0));
    }


}
