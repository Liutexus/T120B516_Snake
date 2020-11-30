package server.Snake.Utility;

import server.Snake.Entity.Entity;
import server.Snake.Entity.Generic.GenericMovingEntity;
import server.Snake.Entity.Generic.GenericStaticEntity;
import server.Snake.Entity.Player;
import server.Snake.Entity.Snake;
import server.Snake.Interface.IVisitor;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapToObjectVisitor implements IVisitor {
    private HashMap<String, Object> map = new HashMap();

    public MapToObjectVisitor(){}

    public MapToObjectVisitor(HashMap map){
        this.map = map;
    }

    public void setMap(HashMap map){
        this.map = map;
    }

    @Override
    public GenericStaticEntity visit(GenericStaticEntity entity){
        map.forEach((field, obj) -> {
            switch (field){
                case "position":
                    entity.setPosition(
                            (Utils.parseToFloat(((ArrayList) obj).get(0))),
                            (Utils.parseToFloat(((ArrayList) obj).get(1)))
                    );
                    break;
                case "size":
                    entity.setSizeX(Utils.parseToFloat(((ArrayList) obj).get(0)));
                    entity.setSizeY(Utils.parseToFloat(((ArrayList) obj).get(1)));
                    break;
                case "colorRGB":
                    entity.setColor(new Color((int)obj));
                    break;
                default:
                    System.out.println("Attribute: '" + field + "' is not recognised in " + Entity.class);
                    break;
            }
        });
        return entity;
    }

    @Override
    public GenericMovingEntity visit(GenericMovingEntity entity){
        map.forEach((field, value) -> {
            ArrayList<Object> temp;
            switch (field){
                case "position":
                    entity.setPosition(
                            (Utils.parseToFloat(((ArrayList) value).get(0))),
                            (Utils.parseToFloat(((ArrayList) value).get(1)))
                    );
                    break;
                case "size":
                    entity.setSizeX(Utils.parseToFloat(((ArrayList) value).get(0)));
                    entity.setSizeY(Utils.parseToFloat(((ArrayList) value).get(1)));
                    break;
                case "colorRGB":
                    entity.setColor(new Color((int)value));
                    break;
                case "velocity":
                    temp = (ArrayList<Object>) value;
                    entity.setVelocityX(Utils.parseToFloat(temp.get(0)));
                    entity.setVelocityY(Utils.parseToFloat(temp.get(1)));
                    break;
                case "previousPositionsX":
                    entity.setPreviousPositionsX((ArrayList<Float>)value);
                    break;
                case "previousPositionsY":
                    entity.setPreviousPositionsY((ArrayList<Float>)value);
                    break;
                default:
                    System.out.println("Attribute: '" + field + "' is not recognised in " + Entity.class);
                    break;
            }
        });
        return entity;
    }

    @Override
    public Entity visit(Entity entity){
        map.forEach((field, obj) -> {
            switch (field){
                case "id":
                    entity.setId((String)obj);
                    break;
                case "position":
                    entity.setPosition(
                            (Utils.parseToFloat(((ArrayList) obj).get(0))),
                            (Utils.parseToFloat(((ArrayList) obj).get(1)))
                    );
                    break;
                case "size":
                    entity.setSizeX(Utils.parseToFloat(((ArrayList) obj).get(0)));
                    entity.setSizeY(Utils.parseToFloat(((ArrayList) obj).get(1)));
                    break;
                case "colorRGB":
                    entity.setColor(new Color((int)obj));
                    break;
                case "velocity":
                case "previousPositionsX":
                case "previousPositionsY":
                    break;
                default:
                    System.out.println("Attribute: '" + field + "' is not recognised in " + Entity.class);
                    break;
            }
        });
        return entity;
    }

    @Override
    public Snake visit(Snake snake) {
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
                    snake.setVelocityX(Utils.parseToFloat(temp.get(0)));
                    snake.setVelocityY(Utils.parseToFloat(temp.get(1)));
                    break;
                case "size":
                    temp = (ArrayList<Object>) value;
                    snake.setSizeX(Utils.parseToFloat(temp.get(0)));
                    snake.setSizeY(Utils.parseToFloat(temp.get(1)));
                    break;
                case "position":
                    temp = (ArrayList<Object>) value;
                    snake.setPositionX(Utils.parseToFloat(temp.get(0)));
                    snake.setPositionY(Utils.parseToFloat(temp.get(1)));
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

    @Override
    public Player visit(Player player){
        map.forEach((field, obj) -> {
            switch (field){
                case "id":
                    player.setId((String)obj);
                    break;
                case "snake":
                    HashMap snakeMap = (HashMap) obj;

                    MapToObjectVisitor visitor = new MapToObjectVisitor(snakeMap);
                    Snake tempSnake = new Snake(0 , 0);
                    tempSnake.accept(visitor);

                    player.setSnake(tempSnake);
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
        return player;
    }

}
