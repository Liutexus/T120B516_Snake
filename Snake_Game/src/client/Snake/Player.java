package client.Snake;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import client.Snake.Entity.Snake;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

public class Player implements java.io.Serializable {

    private String id;
    private Snake snake;
    private int score;
    private String color; // What color is the player?
    private boolean isGameOver;

    public Player(String id) {
        this.id = id;
        this.snake = new Snake();
        this.score = 0;
        this.color = "BLACK";
        this.isGameOver = false;
    }

    public Player(String id, float posX, float posY) {
        this.id = id;
        this.snake = new Snake(posX, posY);
        this.score = 0;
        this.color = "BLACK";
        this.isGameOver = false;
    }

    public String getId() {
        return this.id;
    }

    public Snake getSnake() {
        return this.snake;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void deltaScore(int score) {
        if (this.score + score >= 0) {
            this.score += score;
        }
    }

    public boolean getIsGameOver() {
        return this.isGameOver;
    }
    public void setGameOver(boolean isGameOver){
        this.isGameOver = isGameOver;
    }

    // TODO: Fix JSON-to-object parsing to accommodate the new Snake/Player split.
    public void jsonToObject(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> map = new HashMap<>();
        try {
            map = objectMapper.readValue(json, new TypeReference<HashMap<String,Object>>(){});
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        map.forEach((field, obj) -> {
            ArrayList<Object> objects;
            switch (field){
                case "id":
                    this.id = (String)obj;
                    break;
                case "position":
                    objects = (ArrayList<Object>) obj;
                    this.snake.setPositionX((float)(double)objects.get(0));
                    this.snake.setPositionY((float)(double)objects.get(1));
                    break;
                case "prevPositionsX":
                    this.snake.setPreviousPositionsX((ArrayList) obj);
                    break;
                case "prevPositionsY":
                    this.snake.setPreviousPositionsY((ArrayList) obj);
                    break;
                case "velocity":
                    objects = (ArrayList<Object>) obj;
                    this.snake.setVelocityX((float)(double)objects.get(0));
                    this.snake.setVelocityY((float)(double)objects.get(1));
                    break;
                case "size":
                    objects = (ArrayList<Object>) obj;
                    this.snake.setSizeX((float)(double)objects.get(0));
                    this.snake.setSizeY((float)(double)objects.get(1));
                    break;
                case "tailLength":
                    this.snake.setTailLength((int)obj);
                    break;
                case "score":
                    this.score = (int)obj;
                    break;
                case "color":
                    this.color = (String) obj;
                    break;
                case "isGameOver":
                    this.isGameOver = (boolean) obj;
                default:
                    System.out.println("Attribute: '" + field + "' is not recognised.");
                    break;
            }
        });
    }

    @Override
    public String toString() {
        ObjectWriter ow = new ObjectMapper().writer();
        String json = "";
        this.snake.trimTailSizeAndPrevPos(snake.getTailLength());
        Player convObj = this;
        try {
            json = ow.writeValueAsString(convObj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
