package server.Snake.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class BitmapConverter {
    public enum Terrain {
        Sand,
        Sea,
        Road,
        Forest,
        Swamp,
        Snow
    }

    public static HashMap<Integer, Terrain> colorToTerrainMap = new HashMap<>() {{
        put(-11938, Terrain.Sand);
        put(-13619152, Terrain.Road);
        put(-8355712, Terrain.Swamp);
        put(-16739073, Terrain.Sea);
        put(-16744690, Terrain.Forest);
        put(-1, Terrain.Snow);
    }};

    public static Terrain[][] BMPToTerrain(String imagePath) {
        try {
            File bitmapFile = new File(imagePath);
            BufferedImage image = ImageIO.read(bitmapFile);

            int width = image.getWidth();
            int height = image.getHeight();
            Terrain[][] terrainType = new Terrain[height][width];

            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    terrainType[i][j] = colorToTerrainMap.get(image.getRGB(j, i));
                }
            }
            return terrainType;
        }
        catch (IOException e) {
            System.out.println(e.toString());
            return new Terrain[0][0];
        }
    }

    public static Terrain[][] BMPToTerrain(String imagePath, int sizeX, int sizeY) {
        try {
            File bitmapFile = new File(imagePath);
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
        }
        catch (IOException e) {
            e.printStackTrace();
            return new Terrain[0][0];
        }
    }

    public static Color[][] BMPToColor(String imagePath) {
        try {
            File bitmapFile = new File(imagePath);
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
        }
        catch (IOException e) {
            System.out.println(e.toString());
            return new Color[0][0];
        }
    }
}