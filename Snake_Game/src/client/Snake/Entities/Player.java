package client.Snake.Entities;
import java.util.ArrayList;

public class Player {
    private String id;

    private float posX; // Current player horizontal position
    private float posY; // Current player vertical position
    private ArrayList prevPosX; // Previous player horizontal positions
    private ArrayList prevPosY; // Previous player vertical positions
    private float directionX = 0; // How fast is the player going horizontally?
    private float directionY = 0; // How fast is the player going vertically?

    private float sizeX; // How big is the player by X axis
    private float sizeY; // How big is the player by Y axis
    private int tailLength = 0; // Player tail's length
    private int score;
    private int health;
    private String color; // What color is the player?
    private String boost; // What boost player currently has?
    private String ground; // What ground is player standing?

    private boolean isGameOver = false;

    public Player(String id){
        this.id = id;

        this.prevPosX = new ArrayList(1);
        this.prevPosY = new ArrayList(1);
        this.posX = 0;
        this.posY = 0;
        this.directionX = 0;
        this.directionY = 0;
        this.sizeX = 1;
        this.sizeY = 1;
        this.score = 0;
        this.health = 100;
        this.color = "BLACK";
        this.boost = null;
        this.ground = null;
    }

    public Player(String id, float posX, float posY){
        this.id = id;
        this.posX = posX;
        this.posY = posY;

        this.prevPosX = new ArrayList(1);
        this.prevPosY = new ArrayList(1);
        this.sizeX = 1;
        this.sizeY = 1;
        this.score = 0;
        this.health = 100;
        this.color = "BLACK";
        this.boost = null;
        this.ground = null;
    }

    public String getId(){
        return this.id;
    }

    public boolean movePlayer(){
        prevPosX.add(0, posX);
        prevPosY.add(0, posY);

        posX += directionX;
        posY += directionY;

        // TODO:
        // Check if new position do not collide with previous positions or another player.

        return true;
    }

    public void setPosition(float x, float y){
        posX = x;
        posY = y;
    }

    public float[] getPosition(){
        return new float[] {this.posX, this.posY};
    }

    public ArrayList[] getPrevPositions(){
        return new ArrayList[] {this.prevPosX, this.prevPosY};
    }

    public void setMoveDirection(float x, float y){
        directionX = x;
        directionY = y;
    }

    public float[] getMoveDirection(){
        return new float[] {this.directionX, this.directionY};
    }

    public float[] getSize(){
        return new float[] {this.sizeX, sizeY};
    }

    public void setSize(float sizeX, float sizeY){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public void deltaSize(float sizeX, float sizeY){
        this.sizeX += sizeX;
        this.sizeY += sizeY;
    }

    public void increaseTailLength(int delta){
        this.tailLength += delta;
    }

    public int getTailLength(){
        return this.tailLength;
    }

    public String getPlayerColor(){
        return this.color;
    }

    private void addPositionToPrevious(){
        // TODO:
        // Push current coordinate to the start of the previous coordinates list.
    }

    public boolean checkCollisionWithTail(){
        for(int i = 0; i < this.tailLength; i++){
            if(directionX != 0 || directionY != 0) // is player moving?
                if(posX == (float)prevPosX.get(i) && posY == (float)prevPosY.get(i))
                    return true;
        }

        return false;
    }

    public void setGameOver(boolean state){
        this.isGameOver = state;
    }


    @Override
    public String toString() {
        String output;
        output = "ID: " + this.id + "\n";
        output += "Position: " + this.posX + " " + this.posY + "\n";
        output += "Direction: " + this.directionX + " " + this.directionY + "\n";
        output += "Size: " + this.sizeX + " " + this.sizeY + "\n";
        output += "Health: " + this.health + "\n";
        output += "Color: " + this.color + "\n";
        output += "Boost: " + this.boost + "\n";
        output += "Ground: " + this.ground + "\n";

        return  output;
    }
}
