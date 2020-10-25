package server.Snake.Utility;

import java.util.Random;

public class Utils {

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
        // TODO: for NPC walking
        return null;
    }

}
