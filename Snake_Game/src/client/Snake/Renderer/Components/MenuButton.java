package client.Snake.Renderer.Components;

import client.Snake.Interface.IMediator;

import javax.swing.*;
import java.awt.*;

public class MenuButton extends JButton {
    IMediator mediator;
    private Shape buttonShape = createShape();

    public MenuButton() {
        this(null);
    }

    public MenuButton(String text) {
        super(text);
        super.setContentAreaFilled(false);

        addActionListener(actionEvent -> {
            mediator.notify(this);
        });
    }

    public MenuButton(String text, IMediator mediator) {
        this(text);
        this.mediator = mediator;
    }

    public void paintBorder( Graphics g ) {
        ((Graphics2D)g).draw(buttonShape);
    }

    @Override
    public void paintComponent( Graphics g ) {
        Graphics2D g2d = (Graphics2D) g.create();

        if(getModel().isPressed()){
            g2d.setColor(Color.GRAY);
            g2d.fill(buttonShape);
        }

        super.paintComponent(g);
    }

    public Dimension getPreferredSize() {
        return new Dimension(201,51);
    }

    public boolean contains(int x, int y) {
        return buttonShape.contains(x, y);
    }

    private Shape createShape() {
        Rectangle r = new Rectangle();
        r.add(200, 50);
        return r;
    }

}
