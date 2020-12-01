package client.Snake.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import client.Snake.Renderer.Command.TemplateCommand;
import client.Snake.Renderer.Drawables.AllDrawables;
import client.Snake.Renderer.Drawables.Terrain;
import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.AbstractStaticEntity;
import client.Snake.Renderer.Command.NetworkCommand;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Generic.GenericMovingEntity;
import server.Snake.Entity.Generic.GenericStaticEntity;
import server.Snake.Entity.Player;
import client.Snake.Renderer.Command.PlayerMoveCommand;
import server.Snake.Network.Packet.Packet;
import server.Snake.Utility.BitmapConverter;
import server.Snake.Utility.MapToObjectVisitor;
import server.Snake.Utility.Utils;

class SnakePanel extends JPanel implements Runnable {
    private static SnakePanel panelInstance = null;

    private OutputStreamWriter out;
    private InputStreamReader in;

    private Dimension windowSize;
    private int horizontalCellCount = 50;
    private int verticalCellCount = 50;
    private int cellWidth;
    private int cellHeight;

    private int windowWidth;
    private int windowHeight;
    private String id;
    private Socket clientSocket;
    private AllDrawables allDrawables = new AllDrawables();
    private Map<String, Player> snakes = new ConcurrentHashMap<>();
    private Map<String, AbstractStaticEntity> staticTerrainEntities;
    private Map<String, AbstractMovingEntity> movingTerrainEntities;
    private Map<Integer, ArrayList> terrain;

    private SnakePanel(Socket clientSocket) {
        setFocusable(true);
        setDoubleBuffered(true);
        requestFocusInWindow();

        this.staticTerrainEntities = new ConcurrentHashMap<>();
        this.movingTerrainEntities = new ConcurrentHashMap<>();
        this.terrain = new ConcurrentHashMap<>();

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

    public static SnakePanel getInstance(Socket clientSocket) {
        if (panelInstance == null)
            panelInstance = new SnakePanel(clientSocket);
        return panelInstance;
    }

    private void keyResponse(KeyEvent key) {
        switch (key.getKeyCode()){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                PlayerMoveCommand.moveUp(this.id, out);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                PlayerMoveCommand.moveRight(this.id, out);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                PlayerMoveCommand.moveDown(this.id, out);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                PlayerMoveCommand.moveLeft(this.id, out);
                break;
            case KeyEvent.VK_SPACE:
                PlayerMoveCommand.moveStop(this.id, out);
                break;
            case KeyEvent.VK_Z:
                TemplateCommand u = new PlayerMoveCommand();
                u.command(this.id, out);
                break;
            default:
                System.out.println("Not defined key press '" + key.getKeyChar() + "'");
//                throw new IllegalStateException("Unexpected value: " + key.getKeyCode());
        }
    }

    // Rendering functions
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // To create a nice grid
        windowSize = getSize();
        windowWidth = windowSize.width;
        windowHeight = windowSize.height;

        cellWidth = (windowSize.width/horizontalCellCount);
        cellHeight = (windowSize.height/verticalCellCount);

        // Drawing the terrain
        terrain.forEach((y, arrayX) -> {
            for(int i = 0; i < arrayX.size(); i++){
                Terrain terain = new Terrain(i ,y, BitmapConverter.getColorByIndex((int)arrayX.get(i)));
                allDrawables.addDrawable(terain);
            }
        });

        // Draw all players
        for (Player player : snakes.values()) {
            allDrawables.addDrawable(player.getSnake());
        }

        // Draw all objects placed on the map
        staticTerrainEntities.forEach((type, entity) -> {
            allDrawables.addDrawable(entity);
        });

        movingTerrainEntities.forEach((type, entity) -> {
            allDrawables.addDrawable(entity);
        });

        allDrawables.drawRect(g, windowWidth, windowHeight, cellWidth, cellHeight);
        allDrawables.removeDrawables();
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
                    try { this.wait(1000); } catch (Exception ex) { }
                }
//                e.printStackTrace();
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(2);
        ClientUpdater updater = new ClientUpdater();
        ClientListener listener = new ClientListener(in, updater);
        executor.execute(updater);
        executor.execute(listener);
    }

    private class ClientListener implements Runnable {
        private InputStreamReader in;
        private ClientUpdater updater;

        public ClientListener(InputStreamReader in, ClientUpdater updater) {
            this.in = in;
            this.updater = updater;
        }

        @Override
        public void run() {
            BufferedReader inb = new BufferedReader(in);
            while(true) {
                try {
                    updater.addPacket(inb.readLine());
                    if(updater.sleeping)
                        synchronized (updater) {
                            updater.notify();
                        }
                } catch (Exception e) {
                    System.out.println("Couldn't receive players from the server.");
//                    e.printStackTrace();
                    try {Thread.sleep(100);} catch (Exception ex) { };
                    continue;
                }
            }
        }
    }

    private class ClientUpdater implements Runnable {
        public boolean sleeping = false;
        private ArrayDeque<String> packets;
        ExecutorService executor;

        public ClientUpdater() {
            packets = new ArrayDeque<>();
            executor = Executors.newFixedThreadPool(2);
        }

        public void addPacket(String packet) {
            packets.add(packet);
        }

        private void parsePacket(String packetJson) {
            MapToObjectVisitor visitor = new MapToObjectVisitor();
            Packet packet = new Packet(packetJson);
            Map packetMap; // To store parsed packet map

            Player packetPlayer = new Player(null);
            switch (packet.header) {
                case ID:
                    id = (String) packet.parseBody().get(packet.header.toString());
                    System.out.println("Client ID: " + id);
                    break;
                case CLIENT_PLAYER:
                    packetMap = packet.parseBody();
                    visitor.setMap((HashMap) packetMap);
                    packetPlayer.accept(visitor); // Parsing the received player packet
                    break;
                case PLAYER:
                    packetMap = packet.parseBody();
                    visitor.setMap((HashMap) packetMap);
                    packetPlayer.accept(visitor); // Parsing the received player packet
                    if (packetPlayer.getId() != null)
                        if (!snakes.containsKey(packetPlayer)) snakes.put(packetPlayer.getId(), packetPlayer);
                        else snakes.replace(packetPlayer.getId(), packetPlayer);
                    break;
                case TERRAIN:
                    packetMap = packet.parseBody();
                    packetMap.forEach((key, array) -> { // Because of laziness
                        if (!terrain.containsKey(key)) // Do we already have this line of terrain?
                            terrain.put(Integer.parseInt((String) key), (ArrayList) array); // Putting a new line of terrain
                    });
                    break;
                case ENTITY:
                    packetMap = packet.parseBody();
                    visitor.setMap((HashMap) packetMap);
                    Entity packetEntity;
                    if (packetMap.containsKey("velocity")) {
                        packetEntity = new GenericMovingEntity(0, 0);
                        packetEntity.accept(visitor);
                        movingTerrainEntities.put((String)packetMap.get("id"), (AbstractMovingEntity) packetEntity);
                    }
                    else {
                        packetEntity = new GenericStaticEntity(0, 0);
                        packetEntity.accept(visitor);
                        staticTerrainEntities.put((String)packetMap.get("id"), (GenericStaticEntity) packetEntity);
                    }
                    break;
                default:
                    System.out.println("Error. Not recognised packet header '" + packet.header.toString() + "'. ");
                    break;
            }
        }

        @Override
        public void run() {
            while(true) {
                if (packets.size() == 0) {
                    sleeping = true;
                    try { this.wait(); } catch (Exception e) { }
                } else {
                    sleeping = false;
                    parsePacket(packets.pop());
                    repaint();
                }
            }
        }
    }
}
