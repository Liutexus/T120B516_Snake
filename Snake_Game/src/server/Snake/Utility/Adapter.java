package server.Snake.Utility;

import server.Snake.Entity.Entity;
import server.Snake.Entity.Generic.GenericMovingEntity;
import server.Snake.Entity.Generic.GenericStaticEntity;
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
