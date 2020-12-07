package client.Snake.Renderer.Drawables;

import client.Snake.Renderer.Interface.IDrawable;
import client.Snake.Renderer.Interface.IIterator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AllDrawables implements IDrawable, IIterator {
    private List<IDrawable> drawableList = new ArrayList<IDrawable>();

    public void addDrawable(IDrawable item)
    {
        drawableList.add(item);
    }

    public void removeDrawable(IDrawable item)
    {
        drawableList.remove(item);
    }

    public void removeDrawables()
    {
        drawableList.clear();
    }

    @Override
    public void drawRect(Graphics g, int windowWidth, int windowHeight, int cellWidth, int cellHeight) {
        for(IDrawable item : drawableList)
            item.drawRect(g, windowWidth, windowHeight, cellWidth, cellHeight);
    }

    @Override
    public Iterator createIterator() {
        return drawableList.iterator();
    }
}
