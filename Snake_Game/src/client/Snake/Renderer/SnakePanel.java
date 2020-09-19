package client.Snake.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import client.Snake.Entities.Food;
import client.Snake.Entities.Player;

class SnakePanel extends JPanel {
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    private Dimension windowSize;
    private int horizontalCellCount = 50;
    private int verticalCellCount = 50;
    private int cellWidth;
    private int cellHeight;

    private Player currentPlayer;
    private String Id;
    private Socket clientSocket;

    ArrayList<Player> snakes;
    ArrayList<Food> objects;
    ArrayList terrain;

    public SnakePanel(Socket clientSocket) {
        setFocusable(true);
        requestFocusInWindow();

        this.snakes = new ArrayList<Player>();
        this.objects = new ArrayList<Food>();
        this.terrain = new ArrayList();

        this.clientSocket = clientSocket;
        // Assign socket input and output
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Cannot establish connection to server.");
//            e.printStackTrace();
        }

        this.Id = getId();
        this.currentPlayer = getCurrentPlayer();
        snakes.add(currentPlayer);

        addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent e) {
                // TODO: Rewrite keyevents to send outputs to the server
                keyResponse(e);
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

    public int getCurrentPlayerIndex(){
        for(int i = 0; i < snakes.size(); i++){
            if(snakes.get(i).getId() == currentPlayer.getId()){
                return i;
            }
        }
        return -1;
    }

    public Player getCurrentPlayer(){
        // Request server to create a 'Player' object
        while (true) {
            try {
                Player player = (Player)in.readObject();
//                System.out.println(player.toString());
                if(player != null) return player;
            } catch (Exception e) {
                System.out.println("Player object from server not received.");
//                e.printStackTrace();
            }
        }

    }

    private String getId(){
        // Wait for server to assign an ID
        while (true) {
            try {
                String id = (String)in.readObject();
                System.out.println(id);
//                out.writeObject(true);
                if(id.length() != 0) return id;
            } catch (Exception e) {
                System.out.println("Failed to receive ID from server.");
//                e.printStackTrace();
            }
        }
    }

    public boolean updatePlayer(Player player) {
        for(int i = 0; i < snakes.size(); i++) {
            if(snakes.get(i).getId() == player.getId()){
                snakes.set(i, player);
                return true;
            }
        }
        return false;
    }

    public void updatePlayers(Player[] players) {
        for(int i = 0; i < snakes.size(); i++) {
            for(int j = 0; j < players.length; j++){
                if(snakes.get(i).getId() == players[j].getId()){
                    snakes.set(i, players[j]);
                    // TODO Recommended: Remove 'players[j]' from the array
                    // to save up some computing time
                    break;
                }
            }
        }
    }

    private void keyResponse(KeyEvent key) {

        switch (key.getKeyCode()){
            case KeyEvent.VK_UP:
//                player.setMoveDirection(0, -1);
                break;
            case KeyEvent.VK_RIGHT:
//                player.setMoveDirection(1, 0);
                break;
            case KeyEvent.VK_DOWN:
//                player.setMoveDirection(0, 1);
                break;
            case KeyEvent.VK_LEFT:
//                player.setMoveDirection(-1, 0);
                break;
            case KeyEvent.VK_SPACE:
//                player.setMoveDirection(0, 0);
                break;
            case KeyEvent.VK_W: // Placeholder
//                player.deltaSize(1, 1);
                break;
            case KeyEvent.VK_D: // Placeholder
//                player.changeTailSize(1);
                break;
            case KeyEvent.VK_S: // Placeholder
//                player.deltaSize(-1, -1);
                break;
            case KeyEvent.VK_A: // Placeholder
//                player.changeTailSize(-1);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + key.getKeyCode());
        }


    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // To create a nice grid
        windowSize = getSize();
        cellWidth = (windowSize.width/horizontalCellCount);
        cellHeight = (windowSize.height/verticalCellCount);

        // Draw all players
        snakes.forEach(x -> {
//            System.out.println(x.toString());
            drawRect(g, x.getPosition(), x.getSize(), Color.RED); // Drawing the snake's head

            // Drawing the tail
            ArrayList prevPosX = x.getPrevPositionsX();
            ArrayList prevPosY = x.getPrevPositionsY();
            for(int i = 0; i < x.getTailLength(); i++){
                try {
                    int colorStep = 255 / (x.getTailLength() + 1);
                    Color tailColor = new Color(colorStep * (x.getTailLength() - i), colorStep, colorStep);
                    prevPosX.get(i);
                    drawRect(g, new float[]{(float) prevPosX.get(i), (float) prevPosY.get(i)}, x.getSize(), tailColor);
                } catch (Exception e) {
                    // Just to reduce headache from exceptions at the start of the game
                    // when there's not enough previous positions to draw tail from.
                    break;
                }
            }
        });

        // Draw all objects placed on the map
        objects.forEach(obj -> {
            // TODO: Draw map objects here
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
