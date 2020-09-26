package client.Snake.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import client.Snake.Entities.Food;
import client.Snake.Entities.Player;

class SnakePanel extends JPanel {
    private static ObjectOutputStream out;
    private static InputStreamReader in;

    private Dimension windowSize;
    private int horizontalCellCount = 50;
    private int verticalCellCount = 50;
    private int cellWidth;
    private int cellHeight;

    private Player currentPlayer;
    private String Id;
    private Socket clientSocket;

    private Map<String, Player> snakes = new ConcurrentHashMap<String, Player>();
    private ArrayList<Food> objects;
    private ArrayList terrain;

    public SnakePanel(Socket clientSocket) {
        setFocusable(true);
        requestFocusInWindow();

        this.objects = new ArrayList<Food>();
        this.terrain = new ArrayList();

        this.clientSocket = clientSocket;
        // Assign socket's input and output streams
        try {
            out = new ObjectOutputStream(this.clientSocket.getOutputStream());
            in = new InputStreamReader(this.clientSocket.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Cannot establish connection to server.");
            e.printStackTrace();
        }

        this.Id = getId();
        System.out.println(this.Id);
        this.currentPlayer = getRemoteCurrentPlayer();

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

    public Player getLocalCurrentPlayer() {
        return this.currentPlayer;
    }

    public Player getRemoteCurrentPlayer() {
        BufferedReader inb = new BufferedReader(in);
        char[] chars = new char[1024];
        while (true) {
            try {
                Player tempPlayer = new Player(null);
                inb.read(chars);
                String receivedPlayer = new String(chars).trim();

                tempPlayer.jsonToObject(receivedPlayer);

                if(tempPlayer != null) return tempPlayer;
            } catch (Exception e) {
                System.out.println("Player object from server not received.");
                e.printStackTrace();
            }
        }
    }

    private String getId() {
        BufferedReader inb = new BufferedReader(in);
        char[] chars = new char[256];
        while (true) {
            try {
                inb.read(chars);
//                System.out.println(chars);
                if(chars.length != 0) return new String(chars).trim();
                else return null;
            } catch (Exception e) {
                System.out.println("Failed to receive ID from server.");
//                e.printStackTrace();
            }
        }
    }

//    public void updatePlayers() {
//
////        Map<String, Player> remotePlayers = null;
//        try {
//            System.out.println(in.);
////            remotePlayers = (Map<String, Player>) in.readObject();
//        } catch (Exception e) {
//            System.out.println("Couldn't receive players from the server.");
//            return;
////            e.printStackTrace();
//        }
////        snakes = remotePlayers;
////        currentPlayer = remotePlayers.get(Id);
//    }

    private void keyResponse(KeyEvent key) {

        try {
            out.reset(); // Removing any previously sent messages
            switch (key.getKeyCode()){
                case KeyEvent.VK_UP:
                    currentPlayer.setMoveDirection(0, -1);
                    out.writeByte(1); // TODO: Find a better way to send inputs
                    out.writeUnshared(currentPlayer);
                    break;
                case KeyEvent.VK_RIGHT:
                    currentPlayer.setMoveDirection(1, 0);
                    out.writeByte(1);
                    out.writeUnshared(currentPlayer);
                    break;
                case KeyEvent.VK_DOWN:
                    currentPlayer.setMoveDirection(0, 1);
                    out.writeByte(1);
                    out.writeUnshared(currentPlayer);
                    break;
                case KeyEvent.VK_LEFT:
                    currentPlayer.setMoveDirection(-1, 0);
                    out.writeByte(1);
                    out.writeUnshared(currentPlayer);
                    break;
                case KeyEvent.VK_SPACE:
                    currentPlayer.setMoveDirection(0, 0);
                    out.writeByte(1);
                    out.writeUnshared(currentPlayer);
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

        } catch (Exception e) {
            System.out.println("Error sending an input to the server.");
//            e.printStackTrace();
        }
    }

    // Rendering functions
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // To create a nice grid
        windowSize = getSize();
        cellWidth = (windowSize.width/horizontalCellCount);
        cellHeight = (windowSize.height/verticalCellCount);

        // Draw all players
        for (Map.Entry<String, Player> entry : snakes.entrySet()) {
            drawRect(g, entry.getValue().getPosition(), entry.getValue().getSize(), Color.RED); // Drawing the snake's head

            // Drawing the tail
            ArrayList prevPosX = entry.getValue().getPrevPositionsX();
            ArrayList prevPosY = entry.getValue().getPrevPositionsY();

            for(int i = 0; i < entry.getValue().getTailLength(); i++){
                try {
                    int colorStep = 255 / (entry.getValue().getTailLength() + 1);
                    Color tailColor = new Color(colorStep * (entry.getValue().getTailLength() - i), colorStep, colorStep);
                    prevPosX.get(i);
                    drawRect(g, new float[]{(float) prevPosX.get(i), (float) prevPosY.get(i)}, entry.getValue().getSize(), tailColor);
                } catch (Exception e) {
                    // Just to reduce headache from exceptions at the start of the game
                    // when there's not enough previous positions to draw tail from.
                    break;
                }
            }
        }

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
