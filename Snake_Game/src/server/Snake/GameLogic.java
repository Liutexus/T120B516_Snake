// GameLogic.java is responsible of validating players' moves and determining game's state
package server.Snake;

import client.Snake.Entities.Player;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GameLogic implements Runnable {
    private Map<Integer, Handler> handlers;
    private Map<String, Player> players; // All current players

    public GameLogic(Map handlers, Map players){
        this.handlers = handlers;
        this.players = players;
    }

    // TODO: Add various functions which change players' state

    private void movePlayers() {
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            // --- JUST FOR FUN ---
            // For testing purposes
            float[] directs = entry.getValue().getMoveDirection();
            int randx = ThreadLocalRandom.current().nextInt(-1, 2);
            if(randx == directs[0]*-1) randx = (int)directs[0];

            int randy = 0;
            if(randx == 0){
                randy = ThreadLocalRandom.current().nextInt(-1, 2);
            }
            entry.getValue().setMoveDirection(randx, randy);
            // --- FUN ZONE OVER ---
            entry.getValue().movePlayer();
        }
    }

    @Override
    public void run() {
        while(true) { // Main game loop
            // Game logic goes here
            synchronized (players){
                movePlayers();
            }

            try {Thread.sleep(100);} catch (Exception e) { };
        }
    }
}
