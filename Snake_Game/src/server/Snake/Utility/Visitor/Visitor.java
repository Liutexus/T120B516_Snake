package server.Snake.Utility.Visitor;

import server.Snake.Entity.Entity;
import server.Snake.Entity.Generic.GenericMovingEntity;
import server.Snake.Entity.Generic.GenericStaticEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class Visitor implements IVisitor {

    public static GenericStaticEntity mapToStaticEntity(Map<String, Object> map){
        GenericStaticEntity tempEntity = new GenericStaticEntity(0,0);
        map.forEach((field, obj) -> {
            switch (field){
                case "position":
                    tempEntity.setPosition(
                            (parseToFloat(((ArrayList) obj).get(0))),
                            (parseToFloat(((ArrayList) obj).get(1)))
                    );
                    break;
                case "size":
                    tempEntity.setSizeX(parseToFloat(((ArrayList) obj).get(0)));
                    tempEntity.setSizeY(parseToFloat(((ArrayList) obj).get(1)));
                    break;
                case "colorRGB":
                    tempEntity.setColor(new Color((int)obj));
                    break;
                default:
                    System.out.println("Attribute: '" + field + "' is not recognised in " + Entity.class);
                    break;
            }
        });
        return tempEntity;
    }

    public static GenericMovingEntity mapToMovingEntity(Map<String, Object> map){
        GenericMovingEntity tempEntity = new GenericMovingEntity(0,0);
        map.forEach((field, value) -> {
            ArrayList<Object> temp;
            switch (field){
                case "position":
                    tempEntity.setPosition(
                            (parseToFloat(((ArrayList) value).get(0))),
                            (parseToFloat(((ArrayList) value).get(1)))
                    );
                    break;
                case "size":
                    tempEntity.setSizeX(parseToFloat(((ArrayList) value).get(0)));
                    tempEntity.setSizeY(parseToFloat(((ArrayList) value).get(1)));
                    break;
                case "colorRGB":
                    tempEntity.setColor(new Color((int)value));
                    break;
                case "velocity":
                    temp = (ArrayList<Object>) value;
                    tempEntity.setVelocityX(parseToFloat(temp.get(0)));
                    tempEntity.setVelocityY(parseToFloat(temp.get(1)));
                    break;
                case "previousPositionsX":
                    tempEntity.setPreviousPositionsX((ArrayList<Float>)value);
                    break;
                case "previousPositionsY":
                    tempEntity.setPreviousPositionsY((ArrayList<Float>)value);
                    break;
                default:
                    System.out.println("Attribute: '" + field + "' is not recognised in " + Entity.class);
                    break;
            }
        });
        return tempEntity;
    }

    public static Entity mapToEntity(Map<String, Object> map){
        Entity tempEntity = new Entity(0,0);
        map.forEach((field, obj) -> {
            switch (field){
                case "position":
                    tempEntity.setPosition(
                            (parseToFloat(((ArrayList) obj).get(0))),
                            (parseToFloat(((ArrayList) obj).get(1)))
                    );
                    break;
                case "size":
                    tempEntity.setSizeX(parseToFloat(((ArrayList) obj).get(0)));
                    tempEntity.setSizeY(parseToFloat(((ArrayList) obj).get(1)));
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
                case "velocity":
                case "previousPositionsX":
                case "previousPositionsY":
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
        } else
        if(obj.getClass() == Integer.class){
            return ((Integer) obj).floatValue();
        }

        return 0;
    }
}
