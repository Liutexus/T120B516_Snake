package server.Snake.Utility;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

}
