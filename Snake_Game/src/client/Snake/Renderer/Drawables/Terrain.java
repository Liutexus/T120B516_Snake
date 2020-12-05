package client.Snake.Renderer.Drawables;

import client.Snake.Renderer.Interface.IDrawable;

import java.awt.*;

public class Terrain implements IDrawable {
    private int posX;
    private int posY;
    private Color color;

    public Terrain(int posX, int posY, Color color){
        this.posX = posX;
        this.posY = posY;
        this.color = color;
    }

    @Override
    public void drawRect(Graphics g, int windowWidth, int windowHeight, int cellWidth, int cellHeight) {
        int cellPositionX = ((this.posX) * windowWidth)/50;
        int cellPositionY = ((this.posY) * windowHeight)/50;
        if(this.color.equals(new Color(0, 0, 0))){
            Tile tile = TileFactory.getTile("wall", Color.black, "resources/Tiles/wall_tile.png");
            tile.drawRect(g, cellPositionX, cellPositionY, cellWidth, cellHeight);
        } else {
            g.setColor(this.color);
            g.fillRect(cellPositionX, cellPositionY, cellWidth, cellHeight);
        }
    }
}
