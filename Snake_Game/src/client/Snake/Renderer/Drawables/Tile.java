package client.Snake.Renderer.Drawables;

import client.Snake.Interface.IDrawable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Tile implements IDrawable {
    private String name;
    private Color color;
    private String texture;

    private File bitmapFile;
    private Image image;

    public Tile(String name, Color color, String texture){
        this.name = name;
        this.color = color;
        this.texture = texture;
        this.bitmapFile = new File(texture);
        this.image = null;
        if(!bitmapFile.exists()) {
            System.out.println("File \"" + texture + "\" does not exist");
            return;
        }
        try {
            image = ImageIO.read(bitmapFile);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void drawRect(Graphics g, int posX, int posY, int cellWidth, int cellHeight) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(image, posX, posY, cellWidth, cellHeight, null);
    }
}
