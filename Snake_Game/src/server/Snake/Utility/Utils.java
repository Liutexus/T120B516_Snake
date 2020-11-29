package server.Snake.Utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.Snake.Entity.Player;
import server.Snake.Entity.Snake;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class Utils {
    public static String randomId() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }

    public static float vectorLengthToPoint(float x1, float y1, float x2, float y2) {
        float a = (float)Math.pow(x1 - x2, 2);
        float b = (float)Math.pow(y1 - y2, 2);
        return (float)Math.sqrt(a+b);
    }

    public static float[] vectorToPoint(float x1, float y1, float x2, float y2){
        float a = x1 - x2;
        float b = y1 - y2;

        if(Math.abs(a) > Math.abs(b)){
            if(a > 0)
                return new float[]{1, 0};
            return new float[]{-1, 0};
        } else {
            if(b > 0)
                return new float[]{0, 1};
            return new float[]{0, -1};
        }
    }

    public static String parseConfig(String root, String element){
        Path fileName = Path.of("config.json");

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> map = new HashMap<>();
        try {
            map = objectMapper.readValue(Files.readString(fileName), new TypeReference<>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(((HashMap)map.get(root)).get(element));
    }

    public static Map getPlayersTailsArray(Map<String, Player> players){
        Map<String, ArrayList<Float>> tempMap = new HashMap<>();
        ArrayList<Float> tempX = new ArrayList<>();
        ArrayList<Float> tempY = new ArrayList<>();

        players.forEach((id, player) -> {
            Snake tempSnake = player.getSnake();
            int tailLength = tempSnake.getTailLength();
            if(tailLength > tempSnake.getPreviousPositionsX().size()) tailLength = tempSnake.getPreviousPositionsX().size();

            for(int i = 0; i < tailLength; i++){
                tempX.add((Float) tempSnake.getPreviousPositionsX().get(i));
                tempY.add((Float) tempSnake.getPreviousPositionsY().get(i));
            }
        });

        tempMap.put("x", tempX);
        tempMap.put("y", tempY);

        return tempMap;
    }

    public static int[] findFreeCell(int[][] array, int min, int max){
        int randX = ThreadLocalRandom.current().nextInt(min, max);
        int randY = ThreadLocalRandom.current().nextInt(min, max);

        while(array[randY][randX] == 6) { // To make sure that player doesn't spawn in a Wall already
            randX = ThreadLocalRandom.current().nextInt(5, 45);
            randY = ThreadLocalRandom.current().nextInt(5, 45);
        }

        return new int[]{randX, randY};
    }

    public static int[] findFreeCell(int[][] array1, Map<String, Player> players, int min, int max){
        int[] pos = findFreeCell(array1, min, max);

        players.forEach((id, player) -> {
            Snake tempSnake = player.getSnake();
            int length = tempSnake.getTailLength();
            if(tempSnake.getPreviousPositionsX().size() < length)
                length = tempSnake.getPreviousPositionsX().size();

            while(tempSnake.getPreviousPositionsX().subList(0, length).contains(pos[0]) &&
                    tempSnake.getPreviousPositionsY().subList(0, length).contains(pos[1])){
                pos[0] = ThreadLocalRandom.current().nextInt(min, max);
                pos[1] = ThreadLocalRandom.current().nextInt(min, max);
            }
        });

        return new int[]{pos[0], pos[1]};
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
