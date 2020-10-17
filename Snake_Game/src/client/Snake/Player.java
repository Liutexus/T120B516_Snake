package client.Snake;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import client.Snake.Entity.Snake;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

public class Player implements java.io.Serializable {

    private String id;
    private Snake snake;
    private int score;
    private Color color; // What color is the player?
    private boolean isGameOver;

    public Player(String id) {
        this.id = id;
        this.snake = new Snake(0, 0);
        this.score = 0;
        this.color = Color.BLACK;
        this.isGameOver = false;
    }

    public Player(String id, float posX, float posY) {
        this.id = id;
        this.snake = new Snake(posX, posY);
        this.score = 0;
        this.color = Color.BLACK;
        this.isGameOver = false;
    }

    public String getId() {
        return this.id;
    }

    public Snake getSnake() {
        return this.snake;
    }

    @JsonIgnore
    public Color getColor() {
        return this.color;
    }

    public int getColorRGB() { return this.color.getRGB();}

    public void setColor(Color color) {
        this.color = color;
    }

    public int getScore() {
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
            switch (field){
                case "id":
                    this.id = (String)obj;
                    break;
                case "snake":
                    HashMap snakeMap = (HashMap) obj;
                    this.snake.mapToObject(snakeMap);
                    break;
                case "score":
                    this.score = (int)obj;
                    break;
                case "color":
                    this.color = (Color) obj;
                    break;
                case "isGameOver":
                    this.isGameOver = (boolean) obj;
                    break;
                default:
                    System.out.println("Attribute: '" + field + "' is not recognised.");
                    break;
            }
        });
    }

    public void mapToObject(Map<String, Object> map){
        map.forEach((field, obj) -> {
            switch (field){
                case "id":
                    this.id = (String)obj;
                    break;
                case "snake":
                    HashMap snakeMap = (HashMap) obj;
                    this.snake.mapToObject(snakeMap);
                    break;
                case "score":
                    this.score = (int)obj;
                    break;
                case "colorRGB":
                    this.color = new Color((int)obj);
                    break;
                case "isGameOver":
                    this.isGameOver = (boolean) obj;
                    break;
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
        Player tempPlayer = this;
        tempPlayer.getSnake().trimTailSizeAndPrevPos(tempPlayer.getSnake().getTailLength());
        try {
            json = ow.writeValueAsString(tempPlayer);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
