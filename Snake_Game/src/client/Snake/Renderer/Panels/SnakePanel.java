package client.Snake.Renderer.Panels;

import client.Snake.Command.NetworkCommand;
import client.Snake.Command.PlayerMoveCommand;
import client.Snake.Command.TemplateCommand;
import client.Snake.Interface.IDrawable;
import client.Snake.Interface.IIterator;
import client.Snake.Network.ClientListener;
import client.Snake.Network.ClientUpdater;
import client.Snake.Network.GameData;
import client.Snake.Renderer.Console.GameConsole;
import client.Snake.Renderer.Drawables.Terrain;
import client.Snake.Renderer.RenderState.InGameRenderState;
import client.Snake.Renderer.SwingRender;
import server.Snake.Entity.Player;
import server.Snake.Utility.BitmapConverter;
import server.Snake.Utility.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SnakePanel extends JPanel implements Runnable, IIterator {
    private static SnakePanel panelInstance = null;

    private Socket clientSocket;
    private OutputStreamWriter out;
    private InputStreamReader in;

    private GameData gameData; // Data containing all info about current game on screen

    private int gameTime = 0;

    private SnakePanel() {
        setFocusable(true);
        setDoubleBuffered(true);
        requestFocusInWindow();

        this.gameData = new GameData();

        GameConsole.setupConsole(this);

        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyResponse(e);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == '`')
                    e.consume();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Be useless for now
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    private SnakePanel(Socket clientSocket) {
        setFocusable(true);
        setDoubleBuffered(true);
        requestFocusInWindow();

        this.gameData = new GameData();

        GameConsole.setupConsole(this);

        this.clientSocket = clientSocket;
        // Assign socket's input and output streams
        try {
            if(this.clientSocket != null){
                out = new OutputStreamWriter(this.clientSocket.getOutputStream());
                in = new InputStreamReader(this.clientSocket.getInputStream(), StandardCharsets.UTF_8);
                System.out.println("Connection established with the server.");
            }
        } catch (IOException e) {
            System.out.println("Cannot establish connection to server.");
            e.printStackTrace();
        }

        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyResponse(e);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == '`')
                    e.consume();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Be useless for now
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    public static SnakePanel getInstance(){
        if (panelInstance == null)
            panelInstance = new SnakePanel();
        return panelInstance;
    }

    public static SnakePanel getInstance(Socket clientSocket) {
        if (panelInstance == null)
            panelInstance = new SnakePanel(clientSocket);
        return panelInstance;
    }

    public GameData getGameData(){
        return this.gameData;
    }

    public int getGameTime(){
        return this.gameTime;
    }

    private void keyResponse(KeyEvent key) {
        //                throw new IllegalStateException("Unexpected value: " + key.getKeyCode());
        switch (key.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> PlayerMoveCommand.moveUp(this.gameData.getId(), out);
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> PlayerMoveCommand.moveRight(this.gameData.getId(), out);
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> PlayerMoveCommand.moveDown(this.gameData.getId(), out);
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> PlayerMoveCommand.moveLeft(this.gameData.getId(), out);
            case KeyEvent.VK_SPACE -> PlayerMoveCommand.moveStop(this.gameData.getId(), out);
            case KeyEvent.VK_Z -> {
                TemplateCommand u = new PlayerMoveCommand();
                u.command(this.gameData.getId(), out);
            }
// To control console's visibility. Set key - " ` ";
            case 192 -> GameConsole.toggleVisibility();
            default -> System.out.println("Not defined key press '" + key.getKeyChar() + "'");
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // To create a nice grid
        Dimension windowSize = getSize();
        int windowWidth = windowSize.width;
        int windowHeight = windowSize.height;

        int horizontalCellCount = 50;
        int verticalCellCount = 50;
        int cellWidth = (windowSize.width / horizontalCellCount);
        int cellHeight = (windowSize.height / verticalCellCount);

        // Drawing the terrain
        this.gameData.getTerrain().forEach((y, arrayX) -> {
            for(int i = 0; i < arrayX.size(); i++){
                Terrain terrain = new Terrain(i, y, BitmapConverter.getColorByIndex((int)arrayX.get(i)));
                this.gameData.getAllDrawables().addDrawable(terrain);
            }
        });

        // Draw all players
        var snake = gameData.createIterator();
        while(snake.hasNext()){
            Player data = (Player) snake.next();
            this.gameData.getAllDrawables().addDrawable(data.getSnake());
        }

//        for (Player player : this.gameData.getSnakes().values()) {
//            this.gameData.getAllDrawables().addDrawable(player.getSnake());
//        }

        // Draw all objects placed on the map
        this.gameData.getStaticTerrainEntities().forEach((type, entity) -> this.gameData.getAllDrawables().addDrawable(entity));

        this.gameData.getMovingTerrainEntities().forEach((type, entity) -> this.gameData.getAllDrawables().addDrawable(entity));

        var drawables = gameData.getAllDrawables().createIterator();
        while(drawables.hasNext()){
            IDrawable drawable = (IDrawable) drawables.next();
            drawable.drawRect(g, windowWidth, windowHeight, cellWidth, cellHeight);
        }
//        this.gameData.getAllDrawables().drawRect(g, windowWidth, windowHeight, cellWidth, cellHeight);
        this.gameData.getAllDrawables().removeDrawables();
    }

    @Override
    public void run() {
        while(this.clientSocket == null){ // Just in case it somehow lost connection
            try {
                this.clientSocket = new Socket(
                        Utils.parseConfig("network", "address"),
                        Integer.parseInt(Utils.parseConfig("network", "port")));
                out = new OutputStreamWriter(this.clientSocket.getOutputStream());
                in = new InputStreamReader(this.clientSocket.getInputStream(), StandardCharsets.UTF_8);
                System.out.println("Connection established with the server.");

                NetworkCommand.requestMatchJoin("", new OutputStreamWriter(this.clientSocket.getOutputStream()));
            } catch (Exception e) {
                System.out.println("Cannot establish connection to server.");
                synchronized (this){
                    try { this.wait(1000); } catch (Exception ignored) { }
                }
//                e.printStackTrace();
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(2);
        ClientUpdater updater = new ClientUpdater();
        ClientListener listener = new ClientListener(in, updater);
        executor.execute(updater);
        executor.execute(listener);

        while(SwingRender.getInstance().getCurrentState().getClass() == InGameRenderState.class){
            this.gameTime++;
            try{Thread.sleep(100);} catch (Exception ignored){
            }
        }
    }

    @Override
    public Iterator createIterator() {
        return null;
    }
}
