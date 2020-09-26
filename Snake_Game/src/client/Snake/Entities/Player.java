package client.Snake.Entities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

public class Player implements java.io.Serializable {
    private String id;

    private float posX; // Current player horizontal position
    private float posY; // Current player vertical position
    private ArrayList prevPosX; // Previous player horizontal positions
    private ArrayList prevPosY; // Previous player vertical positions
    private float directionX = 0; // How fast is the player going horizontally?
    private float directionY = 0; // How fast is the player going vertically?

    private float sizeX; // How big is the player by X axis
    private float sizeY; // How big is the player by Y axis
    private int tailLength = 10; // Player tail's length
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

    public ArrayList getPrevPositionsX(){
        return this.prevPosX;
    }

    public ArrayList getPrevPositionsY(){
        return this.prevPosY;
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

    public void changeTailSize(int delta){
        if(this.tailLength + delta >= 0){
            this.tailLength += delta;
        }
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

    public void setScore () {
        // TODO
    }

    public int getScore() {
        return this.score;
    }

    public void setHealth() {
        // TODO
    }

    public int getHealth() {
        return this.health;
    }

    public void setBoost() {
        // TODO
    }

    public String getBoost() {
        return this.boost;
    }

    public void setGround() {
        // TODO
    }

    public String getGround() {
        return this.ground;
    }

    public void setGameOver(boolean state){
        this.isGameOver = state;
    }

    public void jsonToObject(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> map = new HashMap<>();
        try {
            map = objectMapper.readValue(json, new TypeReference<HashMap<String,Object>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.forEach((field, obj) -> {
            ArrayList<Object> objects;
            switch (field){
                case "id":
                    this.id = (String)obj;
                    break;
                case "position":
                    objects = (ArrayList<Object>) obj;
                    posX = (float)(double)objects.get(0);
                    posY = (float)(double)objects.get(1);
                    break;
                case "prevPositionsX":
                    this.prevPosX = (ArrayList) obj;
                    break;
                case "prevPositionsY":
                    this.prevPosY = (ArrayList) obj;
                    break;
                case "moveDirection":
                    objects = (ArrayList<Object>) obj;
                    this.directionX = (float)(double)objects.get(0);
                    this.directionY = (float)(double)objects.get(1);
                    break;
                case "size":
                    objects = (ArrayList<Object>) obj;
                    this.sizeX = (float)(double)objects.get(0);
                    this.sizeY = (float)(double)objects.get(1);
                    break;
                case "tailLength":
                    this.tailLength = (int)obj;
                    break;
                case "score":
                    this.score = (int)obj;
                    break;
                case "health":
                    this.health = (int)obj;
                    break;
                case "playerColor":
                    this.color = (String)obj;
                    break;
                case "boost":
                    this.boost = (String)obj;
                    break;
                case "ground":
                    this.ground = (String)obj;
                    break;
                default:
                    System.out.println("Attribute: '" + field + "' is not recognised.");
                    break;
            }
        });
    }

    @Override
    public String toString() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
