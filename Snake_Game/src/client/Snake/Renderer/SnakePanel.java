package client.Snake.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

class SnakePanel extends JPanel {

    public int directionX = 0;
    public int directionY = 0;


    ArrayList shapes;

    public SnakePanel() {
        setFocusable(true);
        requestFocusInWindow();

        shapes = new ArrayList<Shape>();
        shapes.add(new Rectangle.Float(50, 50, 20, 20));
        shapes.add(new Rectangle.Float(200, 200, 40, 10));
        //setBorder(BorderFactory.createLineBorder(Color.black));

//        addMouseListener(new MouseAdapter() {
//            public void mousePressed(MouseEvent e) {
//                moveSquare(e.getX(),e.getY());
//            }
//        });
//
//        addMouseMotionListener(new MouseAdapter() {
//            public void mouseDragged(MouseEvent e) {
//                moveSquare(e.getX(),e.getY());
//            }
//        });

        addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    directionY = 1;

                    moveSquare(0, "UP");
                }
                if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    directionY = -1;

                    moveSquare(0, "DOWN");
                }
                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    directionX = -1;

                    moveSquare(0, "LEFT");
                }
                if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    directionX = 1;

                    moveSquare(0, "RIGHT");
                }

                if(e.getKeyCode() == KeyEvent.VK_W){
                    moveSquare(1, "UP");
                }
                if(e.getKeyCode() == KeyEvent.VK_S){
                    moveSquare(1, "DOWN");
                }
                if(e.getKeyCode() == KeyEvent.VK_A){
                    moveSquare(1, "LEFT");
                }
                if(e.getKeyCode() == KeyEvent.VK_D){
                    moveSquare(1, "RIGHT");
                }
                if(e.getKeyCode() == KeyEvent.VK_SPACE){
                    System.out.println("Pressed SPACE");

                    //paint();

                }

            }

            @Override
            public void keyTyped(KeyEvent e) {
                // Be useless for now
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Be useless for now
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    private void moveSquare(int index, String direction) {

        Shape temp = (Shape) shapes.get(index);
        Rectangle rec = temp.getBounds();

        if(direction == "UP") rec.y -= 10;
        if(direction == "DOWN") rec.y += 10;
        if(direction == "LEFT") rec.x -= 10;
        if(direction == "RIGHT") rec.x += 10;

        shapes.set(index, rec);

        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        shapes.forEach(x -> {
                Shape temp = (Shape)x;
                g.setColor(Color.RED);
                g.fillRect(temp.getBounds().x, temp.getBounds().y, temp.getBounds().width, temp.getBounds().height);
                g.setColor(Color.BLACK);
                g.drawRect(temp.getBounds().x, temp.getBounds().y, temp.getBounds().width, temp.getBounds().height);

        });

    }
}
