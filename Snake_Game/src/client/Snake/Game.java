package client.Snake;
import java.awt.Color;
import static java.awt.Color.*;

import client.Snake.Entities.Food;
import client.Snake.Entities.Player;
import client.Snake.Renderer.SwingRender;
import studijosKTU.ScreenKTU;

public class Game {
    private SwingRender render;

    private int screenResolution = 20; // 3
    private int screenSize = 50; // 335

    private boolean isGameOver = false; // Is the game over?
    private boolean safeTailTouch = false; // Is touching tail gives game over? ;for debug;

    private Player[] players;
    private Food[] foods;

    void Start() {
        SwingRender render = new SwingRender();



//        ScreenKTU scr = new ScreenKTU(screenResolution, screenSize);
//        scr.forEachCell((r, c) -> scr.print(r, c, white));
//
//        players = new Player[] {new Player("0")};
//        foods = new Food[]{new Food(0, 0)};
//
//        players[0].setPosition((screenSize / 2) + 1, (screenSize / 2) + 1);
//
//        Running(scr);
    }
    void Running(ScreenKTU scr)
    {
        foods[0].nextPosition(screenSize - 2, screenSize - 2);

        while(!isGameOver){ // Main game loop
            scr.forEachCell((r, c) -> scr.print(r, c, white)); // Painting the screen white
            players[0].setMoveDirection(scr.xDirection, scr.yDirection);
            players[0].movePlayer();

            for(int i = 0; i < players.length; i++){
                if(players[i].getPosition()[0] == foods[0].getPosition()[0] && players[i].getPosition()[1] == foods[0].getPosition()[1]){ // Catchin' the Bait
                    players[i].increaseTailLength(foods[0].getScoreValue());
                    foods[0].nextPosition(screenSize - 2, screenSize - 2);
                }
            }

            for(int i = 0; i < players[0].getTailLength(); i++){
                float xTailCoord = (float)players[0].getPrevPositions()[0].get(i);
                float yTailCoord = (float)players[0].getPrevPositions()[1].get(i);

                int colorStep = 255 / (players[0].getTailLength() + 1);
                Color tailColor = new Color(colorStep * i, colorStep, colorStep);
                scr.print((int)yTailCoord, (int)xTailCoord, tailColor);
            }

            // Checking for game over conditions
            if(players[0].getPosition()[0] > screenSize-1 || players[0].getPosition()[0] < 0 || players[0].getPosition()[1] > screenSize-1 || players[0].getPosition()[1] < 0){ // is the player out of screen bounds?
                isGameOver = true;
                players[0].setGameOver(true);
            }

            if(safeTailTouch == false) // Is safe tail mode on?
                for(int i = 1; i <= players[0].getTailLength(); i++)
                    if(players[0].checkCollisionWithTail())
                            isGameOver = true;

            scr.print((int)players[0].getPosition()[1], (int)players[0].getPosition()[0], Color.black); // client.Snake's head
            scr.print((int)foods[0].getPosition()[1], (int)foods[0].getPosition()[0], Color.yellow); // Food

            scr.refresh();
        }
    }

    public static void main(String[] args) {
        Game game = new Game();

        game.Start();
    }
}
