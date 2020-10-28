package server.Snake.Utility;

import server.Snake.Entity.Entity;
import server.Snake.Entity.Snake;
import client.Snake.Entity.Player;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adapter {
    public static void mapToPlayer(Player player, Map<String, Object> map){
        map.forEach((field, obj) -> {
            switch (field){
                case "id":
                    player.setId((String)obj);
                    break;
                case "snake":
                    HashMap snakeMap = (HashMap) obj;
                    player.setSnake(mapToSnake(player.getSnake(), snakeMap));
                    break;
                case "score":
                    player.setScore((int)obj);
                    break;
                case "colorRGB":
                    player.setColor(new Color((int)obj));
                    break;
                case "isGameOver":
                    player.setGameOver((boolean) obj);
                    break;
                default:
                    System.out.println("Attribute: '" + field + "' is not recognised.");
                    break;
            }
        });
    }

    public static String playerToJson(Player player){
        ObjectWriter ow = new ObjectMapper().writer();
        String json = "";
        Player tempPlayer = player.clone();
        tempPlayer.getSnake().trimTailSizeAndPrevPos(tempPlayer.getSnake().getTailLength());
        try {
            json = ow.writeValueAsString(tempPlayer);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Snake mapToSnake(Snake snake, HashMap<String, Object> map) {
        map.forEach((field, value) -> {
            ArrayList<Object> temp;
            switch (field){
                case "previousPositionsX":
                    snake.setPreviousPositionsX((ArrayList<Float>)value);
                    break;
                case "previousPositionsY":
                    snake.setPreviousPositionsY((ArrayList<Float>)value);
                    break;
                case "tailLength":
                    snake.setTailLength((int)value);
                    break;
                case "velocity":
                    temp = (ArrayList<Object>) value;
                    snake.setVelocityX((float)(double)temp.get(0));
                    snake.setVelocityY((float)(double)temp.get(1));
                    break;
                case "size":
                    temp = (ArrayList<Object>) value;
                    snake.setSizeX((float)(double)temp.get(0));
                    snake.setSizeY((float)(double)temp.get(1));
                    break;
                case "position":
                    temp = (ArrayList<Object>) value;
                    snake.setPositionX((float)(double)temp.get(0));
                    snake.setPositionY((float)(double)temp.get(1));
                    break;
                default:
                    System.out.println("Attribute: '" + field + "' is not recognised.");
                    break;
            }
        });
        return snake;
    }

    public static String entityToString(Entity ent) {
        ObjectWriter ow = new ObjectMapper().writer();
        String json = "";
        try {
            json = ow.writeValueAsString(ent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Entity mapToEntity(Map<String, Object> map){
        Entity ent = new Entity(0,0);
        map.forEach((field, obj) -> {
            switch (field){
                case "position":
                    ArrayList<Double> a = (ArrayList) obj;
                    Float sizex = ((Double) a.get(0)).floatValue();
                    Float sizey = ((Double) a.get(1)).floatValue();
                    ent.setPosition(sizex, sizey);
                    break;
                case "size":
                    // set other params
                default:
                    System.out.println("Attribute: '" + field + "' is not recognised.");
                    break;
            }
        });
        return ent;
    }
}
