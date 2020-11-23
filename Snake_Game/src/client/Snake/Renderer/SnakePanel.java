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

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.AbstractStaticEntity;
import client.Snake.Renderer.Command.NetworkCommand;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Player;
import client.Snake.Renderer.Command.PlayerMoveCommand;
import server.Snake.Packet.Packet;
import server.Snake.Utility.Adapter;
import server.Snake.Utility.BitmapConverter;
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

    private String id;
    private Socket clientSocket;

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
        cellWidth = (windowSize.width/horizontalCellCount);
        cellHeight = (windowSize.height/verticalCellCount);

        // Drawing the terrain
        terrain.forEach((y, arrayX) -> {
            for(int i = 0; i < arrayX.size(); i++){
                drawRect(g, new float[]{i, y}, new float[]{1, 1}, BitmapConverter.getColorByIndex((int)arrayX.get(i)));
            }
        });

        // Draw all players
        for (Player player : snakes.values()) {
            drawRect(g, player.getSnake().getPosition(), player.getSnake().getSize(), player.getSnake().getColor()); // Drawing the snake's head

            // Drawing the tail
            ArrayList prevPosX = player.getSnake().getPreviousPositionsX();
            ArrayList prevPosY = player.getSnake().getPreviousPositionsY();

            int tailLength = player.getSnake().getTailLength();
            for(int i = 0; i < tailLength; i++){
                try {
                    int colorStepR = player.getSnake().getColor().getRed() / (tailLength + 1);
                    int colorStepG = player.getSnake().getColor().getGreen() / (tailLength + 1);
                    int colorStepB = player.getSnake().getColor().getBlue() / (tailLength + 1);
                    Color tailColor = new Color(
                            colorStepR * (tailLength - i),
                            colorStepG * (tailLength - i),
                            colorStepB * (tailLength - i)
                    );
                    drawRect(
                            g,
                            new float[]{(Float.parseFloat(prevPosX.get(i).toString())),
                            Float.parseFloat(prevPosY.get(i).toString())},
                            player.getSnake().getSize(),
                            tailColor
                            );
                } catch (Exception e) {
//                    e.printStackTrace();
                    // Just to reduce headache from exceptions at the start of the game
                    // when there's not enough previous positions to draw tail from.
                    break;
                }
            }
        }

        // Draw all objects placed on the map
        staticTerrainEntities.forEach((type, entity) -> {
            drawRect(g, entity.getPosition(), entity.getSize(), entity.getColor() );
        });
        movingTerrainEntities.forEach((type, entity) -> {
            drawRect(g, entity.getPosition(), entity.getSize(), entity.getColor() );
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
                    Adapter.mapToPlayer(packetPlayer, packetMap); // Parsing the received player packet
                    break;
                case PLAYER:
                    packetMap = packet.parseBody();
                    Adapter.mapToPlayer(packetPlayer, packetMap); // Parsing the received player packet
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
                    if (packetMap.containsKey("velocity")) {
                        movingTerrainEntities.put(String.valueOf((int)packetMap.get("colorRGB")), Adapter.mapToMovingEntity(packetMap));
                    }
                    else {
                        staticTerrainEntities.put("Entity", Adapter.mapToStaticEntity(packetMap));
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
