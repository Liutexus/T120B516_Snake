package server.Snake.Entity;

import client.Snake.Renderer.SnakePanel;
import server.Snake.Enumerator.EEffect;
import server.Snake.Interface.IEntity;

import java.awt.*;

public abstract class AbstractStaticEntity extends Entity {

    public AbstractStaticEntity(IEntity entity) {
        super(entity.getId(), entity.getPositionX(), entity.getPositionY());
        entity.getEffects().forEach((effect, duration) -> {
            super.setEffect((EEffect) effect, (Integer) duration);
        });
    }

    public AbstractStaticEntity(float positionX, float positionY) {
        super(positionX, positionY);
    }

    public void onCollide(Object collider) {
        if(collider instanceof Entity){
            super.effects.forEach((k, v) -> {
                ((Entity)collider).setEffect(k, v);
            });
        }
    };

    @Override
    public void drawRect(Graphics g, int windowWidth, int windowHeight, int cellWidth, int cellHeight) {
        Graphics2D g2 = (Graphics2D) g;
        int cellPositionX = ((int)(getPositionX()) * windowWidth)/50;
        int cellPositionY = ((int)(getPositionY()) * windowHeight)/50;
        g2.setColor(getColor());
        g2.fillRect(cellPositionX, cellPositionY, cellWidth*(int)(getSizeX()), cellHeight*(int)(getSizeY()));

        int colR = getColor().getRed();
        int colG = getColor().getGreen();
        int colB = getColor().getBlue();

        int level = SnakePanel.getInstance().getGameTime() % 7 + 1;
        if(level > 4) level = 8 - level; // To make the 'shrinking' effect

        g2.setColor(new Color(colR + 255/(colR+1)/level, colG + 255/(colG+1)/level, colB + 255/(colB+1)/level));

        g2.setStroke(new BasicStroke((int)(level/2)));

        g2.drawRect(cellPositionX, cellPositionY, cellWidth*(int)(getSizeX()), cellHeight*(int)(getSizeY()));
    }

}
