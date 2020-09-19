// ServerGame.java is responsible of keeping all game's logic
package server.Snake;

import client.Snake.Entities.Player;

import java.util.ArrayList;

public class ServerGame implements Runnable {
    private ArrayList<Player> players; // All current players

    public ServerGame(ArrayList<Player> players){
        this.players = players;

    }


    private void movePlayers() {
        for(int i = 0; i < players.size(); i++) {
            Player temp = players.get(i);
//            temp.setMoveDirection(1, 1);
            temp.movePlayer();
//            System.out.println(temp.toString());
            players.set(i, temp);
        }
    }

    @Override
    public void run() {
        while(true){
            // Game logic goes here

            synchronized (players){
                movePlayers();
            }

            try {Thread.sleep(100);} catch (Exception e) { };
        }
    }
}
