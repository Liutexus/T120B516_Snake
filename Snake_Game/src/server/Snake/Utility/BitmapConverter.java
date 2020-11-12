package server.Snake.Utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class BitmapConverter {
    public enum Terrain {
        Sand,
        Road,
        Swamp,
        Sea,
        Forest,
        Snow,
        Wall
    }

    public static HashMap<Integer, Color> indexToColorMap = new HashMap<>() {{
        put(0, new Color(255, 209, 94));
        put(1, new Color(48, 48, 48));
        put(2, new Color(128, 128, 128));
        put(3, new Color(0, 148, 255));
        put(4, new Color(0, 127, 14));
        put(5, new Color(255, 255, 255));
        put(6, new Color(0, 0, 0));
    }};

    public static HashMap<Integer, Terrain> colorToTerrainMap = new HashMap<>() {{
        put(-11938, Terrain.Sand);
        put(-13619152, Terrain.Road);
        put(-8355712, Terrain.Swamp);
        put(-16739073, Terrain.Sea);
        put(-16744690, Terrain.Forest);
        put(-1, Terrain.Snow);
        put(-16777216, Terrain.Wall);
    }};

    public static HashMap<Integer, Integer> colorToIntMap = new HashMap<>() {{
        put(-11938, 0);
        put(-13619152, 1);
        put(-8355712, 2);
        put(-16739073, 3);
        put(-16744690, 4);
        put(-1, 5);
        put(-16777216, 6);
    }};

    public static Color getColorByIndex(int index) {
        if(Terrain.values().length < index) return null;
        return indexToColorMap.get(index);
    }

    public static Terrain[][] BMPToTerrain(String imagePath) {
        try {
            File bitmapFile = new File(imagePath);
            if(!bitmapFile.exists()) {
                System.out.println("File \"" + imagePath + "\" does not exist");
                return null;
            }
            BufferedImage image = ImageIO.read(bitmapFile);
            int width = image.getWidth();
            int height = image.getHeight();
            Terrain[][] terrainType = new Terrain[height][width];
            for(int i = 0; i < height; i++)
                for(int j = 0; j < width; j++)
                    terrainType[i][j] = colorToTerrainMap.get(image.getRGB(j, i));
            return terrainType;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Terrain[][] BMPToTerrain(String imagePath, int sizeX, int sizeY) {
        try {
            File bitmapFile = new File(imagePath);
            if(!bitmapFile.exists()) {
                System.out.println("File \"" + imagePath + "\" does not exist");
                return null;
            }
            BufferedImage imageBuff = ImageIO.read(bitmapFile);
            BufferedImage scaledImageBuff = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = scaledImageBuff.createGraphics();
            graphics2D.drawImage(imageBuff, 0, 0, sizeX, sizeY, null);
            graphics2D.dispose();
            Terrain[][] terrainType = new Terrain[sizeY][sizeX];
            for(int i = 0; i < sizeY; i++)
                for(int j = 0; j < sizeX; j++)
                    terrainType[i][j] = colorToTerrainMap.get(scaledImageBuff.getRGB(j, i));
            return terrainType;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int[][] BMPToIntArray(String imagePath, int sizeX, int sizeY) {
        try {
            File bitmapFile = new File(imagePath);
            if(!bitmapFile.exists()) {
                System.out.println("File \"" + imagePath + "\" does not exist");
                return null;
            }
            BufferedImage imageBuff = ImageIO.read(bitmapFile);
            BufferedImage scaledImageBuff = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = scaledImageBuff.createGraphics();
            graphics2D.drawImage(imageBuff, 0, 0, sizeX, sizeY, null);
            graphics2D.dispose();
            int[][] terrainType = new int[sizeY][sizeX];
            for(int i = 0; i < sizeY; i++)
                for(int j = 0; j < sizeX; j++)
                    terrainType[i][j] = colorToIntMap.get(scaledImageBuff.getRGB(j, i));
            return terrainType;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Color[][] BMPToColor(String imagePath) {
        try {
            File bitmapFile = new File(imagePath);
            if(!bitmapFile.exists()) {
                System.out.println("File \"" + imagePath + "\" does not exist");
                return null;
            }
            BufferedImage image = ImageIO.read(bitmapFile);
            int width = image.getWidth();
            int height = image.getHeight();
            Color[][] colorValues = new Color[height][width];
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    colorValues[i][j] = new Color(image.getRGB(j, i));
                }
            }
            return colorValues;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String intArrayToJSON(int[][] terrain, int index) {
        if(terrain.length < index) return null;
        ObjectWriter ow = new ObjectMapper().writer();
        HashMap<String, int[]> map = new HashMap<>();
        map.put(String.valueOf(index), terrain[index]);
        String json = "";
        try {
            json = ow.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}