// GameLogic.java is responsible of validating players' moves and determining game's state
package server.Snake;

import client.Snake.Entities.Player;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameLogic implements Runnable {
    private Map<Integer, Handler> handlers = new ConcurrentHashMap<>();
    private Map<String, Player> players = new ConcurrentHashMap<>(); // All current players

    public GameLogic(Map handlers, Map players){
        this.handlers = handlers;
        this.players = players;
    }

    // TODO: Add various functions which change players' state

    private void movePlayers() {
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            // --- JUST FOR FUN --- // For testing purposes
//            float[] directs = entry.getValue().getMoveDirection();
//            int randx = ThreadLocalRandom.current().nextInt(-1, 2);
//            if(randx == directs[0]*-1) randx = (int)directs[0];
//
//            int randy = 0;
//            if(randx == 0){
//                randy = ThreadLocalRandom.current().nextInt(-1, 2);
//            }
//            entry.getValue().setMoveDirection(randx, randy);
            // --- FUN ZONE OVER ---
            entry.getValue().movePlayer();
        }
    }

    public void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    private HashMap parseJSon(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> map;
        try {
            map = objectMapper.readValue(json, new TypeReference<>(){});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    public void updatePlayerField(String json) {
        HashMap<String, Object> fields = parseJSon(json);

        if(fields.containsKey("directionX") && fields.containsKey("directionY"))
            players.get(fields.get("id")).setMoveDirection(Float.parseFloat((String)fields.get("directionX")), Float.parseFloat((String)fields.get("directionY")));
    }

    @Override
    public void run() {
        while(true) { // Main game loop
            // Game logic goes here
            movePlayers();

            try {Thread.sleep(100);} catch (Exception e) { };
        }
    }
}
