package client.Snake.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import client.Snake.Entities.Food;
import client.Snake.Entities.Player;

class SnakePanel extends JPanel {
    private Dimension windowSize;
    private int horizontalCellCount = 20;
    private int verticalCellCount = 20;
    private int cellWidth;
    private int cellHeight;

    ArrayList<Player> snakes;
    ArrayList<Food> objects;
    ArrayList terrain;

    public SnakePanel(Player myPlayer) {
        setFocusable(true);
        requestFocusInWindow();

        snakes = new ArrayList<Player>();
        objects = new ArrayList<Food>();
        terrain = new ArrayList();

        snakes.add(myPlayer);

        addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent e) {

                switch(e.getKeyCode()) {
                    case KeyEvent.VK_UP:

                        movePlayer(myPlayer, "UP");
                        break;
                    case KeyEvent.VK_DOWN:

                        movePlayer(myPlayer, "DOWN");
                        break;
                    case KeyEvent.VK_LEFT:

                        movePlayer(myPlayer, "LEFT");
                        break;
                    case KeyEvent.VK_RIGHT:

                        movePlayer(myPlayer, "RIGHT");
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

    private void movePlayer(Player player, String direction) {
        if(direction == "UP") player.setMoveDirection(0, -1);
        if(direction == "RIGHT") player.setMoveDirection(1, 0);
        if(direction == "DOWN") player.setMoveDirection(0, 1);
        if(direction == "LEFT") player.setMoveDirection(-1, 0);
        if(direction == "SPACE") player.setMoveDirection(0, 0);
        player.movePlayer();

//        System.out.println(player.toString());

        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        windowSize = getSize();
        cellWidth = (windowSize.width/horizontalCellCount);
        cellHeight = (windowSize.height/verticalCellCount);

        snakes.forEach(x -> {
//            System.out.println(x.toString());
            drawSnake(g, x);
        });

    }

    private void drawSnake(Graphics g, Player player) {
        float[] pos = player.getPosition();
//        float[] size = player.getSize();
        String color = player.getPlayerColor();

        g.setColor(Color.RED);
        g.fillRect(((int)(pos[0]) * cellWidth), ((int)pos[1] * cellHeight), cellWidth+(windowSize.width%horizontalCellCount), cellHeight+(windowSize.height%verticalCellCount));

        g.setColor(Color.BLACK);
        g.drawRect(((int) pos[0] * cellWidth), ((int)pos[1] * cellHeight), cellWidth+(windowSize.width%horizontalCellCount), cellHeight+(windowSize.height%verticalCellCount));

    }

}


