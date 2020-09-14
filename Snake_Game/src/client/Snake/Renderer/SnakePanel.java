package client.Snake.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import client.Snake.Entities.Food;
import client.Snake.Entities.Player;

class SnakePanel extends JPanel {
    public int directionX = 0;
    public int directionY = 0;

    ArrayList<Player> snakes;
    ArrayList<Food> objects;
    ArrayList terrain;

    public SnakePanel(Player myPlayer) {
        setFocusable(true);
        requestFocusInWindow();

        snakes = new ArrayList<Player>();
        objects = new ArrayList<Food>();
        terrain = new ArrayList();

        addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent e) {

                switch(e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        directionY = 1;

                        moveSquare(myPlayer, "UP");
                        break;
                    case KeyEvent.VK_DOWN:
                        directionY = -1;

                        moveSquare(myPlayer, "DOWN");
                        break;
                    case KeyEvent.VK_LEFT:
                        directionX = -1;

                        moveSquare(myPlayer, "LEFT");
                        break;
                    case KeyEvent.VK_RIGHT:
                        directionX = 1;

                        moveSquare(myPlayer, "RIGHT");
                        break;
                    case KeyEvent.VK_SPACE:
                        System.out.println("Pressed SPACE");
                        break;
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

    public void addPlayer(String id){
        snakes.add(new Player(id));
    }

    public void addPlayer(Player player){
        snakes.add(player);
    }

    private void moveSquare(Player player, String direction) {
        if(direction == "UP") player.setMoveDirection(0, 1);
        if(direction == "RIGHT") player.setMoveDirection(1, 0);
        if(direction == "DOWN") player.setMoveDirection(0, -1);
        if(direction == "LEFT") player.setMoveDirection(-1, 0);
        if(direction == "SPACE") player.setMoveDirection(0, 0);
        player.movePlayer();

        System.out.println(player.toString());

        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

//        snakes.forEach(x -> {
//                Player temp = (Player)x;
//                g.setColor(Color.RED);
//                g.fillRect(temp.getBounds().x, temp.getBounds().y, temp.getBounds().width, temp.getBounds().height);
//                g.setColor(Color.BLACK);
//                g.drawRect(temp.getBounds().x, temp.getBounds().y, temp.getBounds().width, temp.getBounds().height);
//        });

    }
}


