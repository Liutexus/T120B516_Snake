package server.Snake.Utility;

import server.Snake.Entity.Entity;
import server.Snake.Entity.Snake;
import server.Snake.Entity.Player;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Adapter {
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
                case "isGameOver":
                    player.setGameOver((boolean) obj);
                    break;
                default:
                    System.out.println("Attribute: '" + field + "' is not recognised in " + Player.class);
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
                    snake.setVelocityX(parseToFloat(temp.get(0)));
                    snake.setVelocityY(parseToFloat(temp.get(1)));
                    break;
                case "size":
                    temp = (ArrayList<Object>) value;
                    snake.setSizeX(parseToFloat(temp.get(0)));
                    snake.setSizeY(parseToFloat(temp.get(1)));
                    break;
                case "position":
                    temp = (ArrayList<Object>) value;
                    snake.setPositionX(parseToFloat(temp.get(0)));
                    snake.setPositionY(parseToFloat(temp.get(1)));
                    break;
                case "colorRGB":
                    snake.setColor(new Color((int)value));
                    break;
                default:
                    System.out.println("Attribute: '" + field + "' is not recognised in " + Snake.class);
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
        Entity tempEntity = new Entity(0,0);
        map.forEach((field, obj) -> {
            switch (field){
                case "position":
                    tempEntity.setPosition(
                            ((Double) ((ArrayList) obj).get(0)).floatValue(),
                            ((Double) ((ArrayList) obj).get(1)).floatValue()
                    );
                    break;
                case "size":
                    tempEntity.setSizeX(((Double) ((ArrayList) obj).get(0)).floatValue());
                    tempEntity.setSizeY(((Double) ((ArrayList) obj).get(1)).floatValue());
                    break;
                case "colorRGB":
                    tempEntity.setColor(new Color((int)obj));
                    break;
                case "shapeType":
                    String s = "" + obj;
                    switch(s)
                    {
                        case "1":
//                            ent.setShape(new Triangle(new RedColor()));
                            break;
                        case "2":
//                            ent.setShape(new Polygon(new RedColor()));
                            break;
                        case "3":
//                            ent.setShape(new Polygon(new BlueColor()));
                            break;
                        case "4":
//                            ent.setShape(new Triangle(new BlueColor()));
                            break;
                    }
                    break;
                default:
                    System.out.println("Attribute: '" + field + "' is not recognised in " + Entity.class);
                    break;
            }
        });
        return tempEntity;
    }

    public static float parseToFloat(Object obj){
        if(obj.getClass() == String.class){
            return Float.parseFloat((String) obj);
        } else
        if(obj.getClass() == Double.class){
            return ((Double)obj).floatValue();
        } else
        if(obj.getClass() == Float.class){
            return (Float) obj;
        }
        return 0;
    }
}
