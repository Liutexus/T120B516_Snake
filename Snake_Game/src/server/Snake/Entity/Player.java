package server.Snake.Entity;

import java.awt.*;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import server.Snake.Entity.Memento.Caretaker;
import server.Snake.Utility.Adapter;

public class Player implements Cloneable {
    private String id;
    private Snake snake;
    private int score;
    private boolean isGameOver;

    public Player(String id) {
        this.id = id;
        this.snake = new Snake(0, 0);
        this.score = 0;
        this.snake.setColor(Color.BLACK);
        this.isGameOver = false;
    }

    public Player(String id, float posX, float posY) {
        this.id = id;
        this.snake = new Snake(posX, posY);
        this.score = 0;
        this.snake.setColor(Color.BLACK);
        this.isGameOver = false;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public Snake getSnake() {
        return this.snake;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
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

    public Player clone(){
        try{
            return (Player) super.clone();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Couldn't clone '" + this.getClass() + "' class.");
        }
        return null;
    }

    public void mapToObject(Map<String, Object> map){
        Adapter.mapToPlayer(this, map);
    }

    @Override
    public String toString() {
        return Adapter.playerToJson(this);
    }
}
