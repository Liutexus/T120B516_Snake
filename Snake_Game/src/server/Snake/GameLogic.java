// GameLogic.java is responsible of validating players' moves and determining game's state
package server.Snake;

import client.Snake.Entities.Player;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GameLogic implements Runnable {
    private ArrayList<Player> players; // All current players

    public GameLogic(ArrayList<Player> players){
        this.players = players;

    }


    // TODO: Add various functions which change players' state

    private void movePlayers() {
        for(int i = 0; i < players.size(); i++) {
            Player temp = players.get(i);

            // --- JUST FOR FUN ---
//            float[] directs = temp.getMoveDirection();
//            int randx = ThreadLocalRandom.current().nextInt(-1, 2);
//            if(randx == directs[0]*-1) randx = (int)directs[0];
//
//            int randy = 0;
//            if(randx == 0){
//                randy = ThreadLocalRandom.current().nextInt(-1, 2);
//            }
//            temp.setMoveDirection(randx, randy);
            // --- FUN ZONE OVER ---

            temp.movePlayer();
            players.set(i, temp);
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
