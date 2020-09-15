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
    private int horizontalCellCount = 50;
    private int verticalCellCount = 50;
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
                keyResponse(myPlayer, e);
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

    private void keyResponse(Player player, KeyEvent key) {

        switch (key.getKeyCode()){
            case KeyEvent.VK_UP:
                player.setMoveDirection(0, -1);
                break;
            case KeyEvent.VK_RIGHT:
                player.setMoveDirection(1, 0);
                break;
            case KeyEvent.VK_DOWN:
                player.setMoveDirection(0, 1);
                break;
            case KeyEvent.VK_LEFT:
                player.setMoveDirection(-1, 0);
                break;
            case KeyEvent.VK_SPACE:
                player.setMoveDirection(0, 0);
                break;
            case KeyEvent.VK_W: // Placeholder
                player.deltaSize(1, 1);
                break;
            case KeyEvent.VK_D: // Placeholder
                player.deltaSize(1, 0);
                break;
            case KeyEvent.VK_S: // Placeholder
                player.deltaSize(-1, -1);
                break;
            case KeyEvent.VK_A: // Placeholder
                player.deltaSize(0, 1);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + key.getKeyCode());
        }

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
            System.out.println(x.toString());
            drawRect(g, x.getPosition(), x.getSize(), Color.RED);

            ArrayList prevPosX = x.getPrevPositionsX();
            ArrayList prevPosY = x.getPrevPositionsY();
            for(int i = 0; i < x.getTailLength(); i++){
                try {
                    int colorStep = 255 / (x.getTailLength() + 1);
                    Color tailColor = new Color(colorStep * (x.getTailLength() - i), colorStep, colorStep);
                    prevPosX.get(i);
                    drawRect(g, new float[]{(float) prevPosX.get(i), (float) prevPosY.get(i)}, x.getSize(), tailColor);
                    System.out.println("TAIL: " + i);
                } catch (Exception e) {
                    break;
                }
            }

            // TODO: Draw the tail
        });

        objects.forEach(obj -> {
            // TODO: Draw landscape object here
        });

    }

private void drawRect(Graphics g, float[] pos, float[] size, Color color) {
        int cellPositionX = ((int)(pos[0]) * windowSize.width)/horizontalCellCount;
        int cellPositionY = ((int)(pos[1]) * windowSize.height)/verticalCellCount;

        g.setColor(color);
        g.fillRect(cellPositionX, cellPositionY, cellWidth*(int)(size[0]), cellHeight*(int)(size[1]));

//        g.setColor(Color.BLACK);
//        g.drawRect(cellPositionX, cellPositionY, cellWidth*(int)(size[0]), cellHeight*(int)(size[1]));
    }








}
