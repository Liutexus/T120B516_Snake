package client.Snake.Renderer.Network;

import client.Snake.Renderer.GameData;
import client.Snake.Renderer.SnakePanel;
import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Generic.GenericMovingEntity;
import server.Snake.Entity.Generic.GenericStaticEntity;
import server.Snake.Entity.Player;
import server.Snake.Network.Packet.Packet;
import server.Snake.Utility.MapToObjectVisitor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientUpdater implements Runnable{
    private SnakePanel snakePanel = SnakePanel.getInstance();
    private GameData gameData = snakePanel.getGameData();

    public boolean sleeping = false;
    private ArrayDeque<String> packets;

    public ClientUpdater() {
        packets = new ArrayDeque<>();
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
                String id = (String) packet.parseBody().get(packet.header.toString());
                this.gameData.setId(id);
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
                    this.gameData.getSnakes().put(packetPlayer.getId(), packetPlayer);
                break;
            case TERRAIN:
                packetMap = packet.parseBody();
                packetMap.forEach((key, array) -> { // Because of laziness
                    if (!this.gameData.getTerrain().containsKey(key)) // Do we already have this line of terrain?
                        this.gameData.getTerrain().put(Integer.parseInt((String) key), (ArrayList) array); // Putting a new line of terrain
                });
                break;
            case ENTITY:
                packetMap = packet.parseBody();
                visitor.setMap((HashMap) packetMap);
                Entity packetEntity;
                if (packetMap.containsKey("velocity")) {
                    packetEntity = new GenericMovingEntity(0, 0);
                    packetEntity.accept(visitor);
                    this.gameData.putMovingTerrainEntity((String) packetMap.get("id"), (AbstractMovingEntity) packetEntity);
                }
                else {
                    packetEntity = new GenericStaticEntity(0, 0);
                    packetEntity.accept(visitor);
                    this.gameData.putStaticTerrainEntity((String)packetMap.get("id"), (GenericStaticEntity) packetEntity);
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
                snakePanel.repaint();
            }
        }
    }
}
