package Snake;
import java.awt.Color;
import static java.awt.Color.*;
import studijosKTU.ScreenKTU;
import java.util.concurrent.ThreadLocalRandom;
import java.util.LinkedList;

public class Snake {
    void Start()
    {
        int screenResolution = 50;
        int screenSize = 20;
        ScreenKTU scr = new ScreenKTU(screenResolution, screenSize);
        scr.forEachCell((r, c) -> scr.print(r, c, white));

        int playerCoordsX = screenSize / 2; // Starting X coordinates of the player
        int playerCoordsY = screenSize / 2; // Starting Y coordinates of the player
        int targetCoordsX = 0;
        int targetCoordsY = 0;

        int xDirection = 0;
        int yDirection = 0;

        boolean safeTailTouch = false; // Is touching tail gives game over? ;for debug;
        boolean isGameOver = false; // Is the game over?

        Running(scr, playerCoordsX, playerCoordsY, targetCoordsX, targetCoordsY, xDirection, yDirection, isGameOver, safeTailTouch, screenSize);
    }
    void Running(ScreenKTU scr,int playerCoordsX, int playerCoordsY, int targetCoordsX, int targetCoordsY, int xDirection, int yDirection, boolean isGameOver, boolean safeTailTouch, int screenSize)
    {
        targetCoordsX = ThreadLocalRandom.current().nextInt(0, screenSize + 1);
        targetCoordsY = ThreadLocalRandom.current().nextInt(0, screenSize + 1);

        int tailDistance = 1; // Initial tail distance

        LinkedList<Integer> lastPlayerXCoords = new LinkedList<>();
        LinkedList<Integer> lastPlayerYCoords = new LinkedList<>();
        for (int i = 0; i <= tailDistance; i++) { // Creating tail prior any movement
            lastPlayerXCoords.add(playerCoordsX);
            lastPlayerYCoords.add(playerCoordsY);
        }

        while(!isGameOver){
            scr.forEachCell((r, c) -> scr.print(r, c, white)); // Painting the screen white

            xDirection = scr.xDirection;
            yDirection = scr.yDirection;

            // Debug
            //System.out.println("Player X coord: " + playerCoordsX);
            //System.out.println("Player Y coord: " + playerCoordsY);
            //System.out.println("X Direction: " + xDirection);
            //System.out.println("Y Direction: " + yDirection);
            //System.out.println("Bait X coordinates: " + targetCoordsX);
            //System.out.println("Bait Y coordinates: " + targetCoordsY);
            //System.out.println();
            //System.out.println(tailDistance);
            // End of Debug

            playerCoordsX += xDirection; // Changing X coordinates
            playerCoordsY += yDirection; // Changing Y coordinates
            lastPlayerXCoords.addFirst(playerCoordsX); // Saving previous player X coordinates
            lastPlayerYCoords.addFirst(playerCoordsY); // Saving previous player Y coordinates

            // Catchin the Bait
            if(targetCoordsX == playerCoordsX && targetCoordsY == playerCoordsY)
            {
                tailDistance++;
                targetCoordsX = ThreadLocalRandom.current().nextInt(1, screenSize - 2);
                targetCoordsY = ThreadLocalRandom.current().nextInt(1, screenSize - 2);
            }

            // Drawing snake's tail
            Color tailColor;
            for(int i = 0; i <= tailDistance; i++)
            {
                int xTailCoord = lastPlayerXCoords.get(i);
                int yTailCoord = lastPlayerYCoords.get(i);
                int colorStep = 255 / (tailDistance + 1);
                tailColor = new Color(colorStep * i, colorStep, colorStep);
                scr.print(yTailCoord, xTailCoord, tailColor);
            }

            // Checking for game over conditions
            if(playerCoordsX > screenSize-1 || playerCoordsX == 0 || playerCoordsY > screenSize-1 || playerCoordsY == 0) // is the player out of screen bounds?
            {
                isGameOver = true;
            }
            if(safeTailTouch == false) // Is safe tail mode on?
                for(int i = 1; i <= tailDistance; i++)
                    if(xDirection != 0 || yDirection != 0) // is player moving?
                        if(playerCoordsX == lastPlayerXCoords.get(i) && playerCoordsY == lastPlayerYCoords.get(i)) // is snek touching it's tail?
                            isGameOver = true;

            scr.print(playerCoordsY, playerCoordsX, Color.black); // Snake's head
            scr.print(targetCoordsY, targetCoordsX, Color.yellow); // Food

            scr.refresh();
        }
    }

    public static void main(String[] args) {
        Snake a = new Snake();

        a.Start();
    }
}
