package server.Snake.Utility;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class BitmapConverterTest {

    @Test
    void getColorByIndex() {
        assertAll(
                () -> assertEquals(new Color(255, 209, 94), BitmapConverter.getColorByIndex(0)),
                () -> assertEquals(new Color(48, 48, 48), BitmapConverter.getColorByIndex(1)),
                () -> assertEquals(new Color(128, 128, 128), BitmapConverter.getColorByIndex(2)),
                () -> assertEquals(new Color(0, 148, 255), BitmapConverter.getColorByIndex(3)),
                () -> assertEquals(new Color(0, 127, 14), BitmapConverter.getColorByIndex(4)),
                () -> assertEquals(new Color(255, 255, 255), BitmapConverter.getColorByIndex(5)),
                () -> assertEquals(new Color(0, 0, 0), BitmapConverter.getColorByIndex(6)));
    }

    @Test
    void testBMPToTerrain() {
        BitmapConverter.Terrain[][] testBMPSizeArray = BitmapConverter.BMPToTerrain("./resources/terrain_test.bmp", 2, 3);
        assertNotNull(testBMPSizeArray);
        assertAll(
                () -> assertEquals(3, testBMPSizeArray.length),
                () -> assertEquals(2, testBMPSizeArray[0].length),
                () -> assertEquals(2, testBMPSizeArray[1].length),
                () -> assertEquals(2, testBMPSizeArray[2].length),

                () -> assertEquals(BitmapConverter.Terrain.Sand, testBMPSizeArray[0][0]),
                () -> assertEquals(BitmapConverter.Terrain.Sea, testBMPSizeArray[0][1]),
                () -> assertEquals(BitmapConverter.Terrain.Road, testBMPSizeArray[1][0]),
                () -> assertEquals(BitmapConverter.Terrain.Forest, testBMPSizeArray[1][1]),
                () -> assertEquals(BitmapConverter.Terrain.Swamp, testBMPSizeArray[2][0]),
                () -> assertEquals(BitmapConverter.Terrain.Snow, testBMPSizeArray[2][1]));

        BitmapConverter.Terrain[][] testBMPSizelessArray = BitmapConverter.BMPToTerrain("./resources/terrain_test.bmp");
        assertNotNull(testBMPSizelessArray);
        assertAll(
                () -> assertEquals(3, testBMPSizelessArray.length),
                () -> assertEquals(2, testBMPSizelessArray[0].length),
                () -> assertEquals(2, testBMPSizelessArray[1].length),
                () -> assertEquals(2, testBMPSizelessArray[2].length),

                () -> assertEquals(BitmapConverter.Terrain.Sand, testBMPSizelessArray[0][0]),
                () -> assertEquals(BitmapConverter.Terrain.Sea, testBMPSizelessArray[0][1]),
                () -> assertEquals(BitmapConverter.Terrain.Road, testBMPSizelessArray[1][0]),
                () -> assertEquals(BitmapConverter.Terrain.Forest, testBMPSizelessArray[1][1]),
                () -> assertEquals(BitmapConverter.Terrain.Swamp, testBMPSizelessArray[2][0]),
                () -> assertEquals(BitmapConverter.Terrain.Snow, testBMPSizelessArray[2][1]));

        BitmapConverter.Terrain[][] testBMPNullArray = BitmapConverter.BMPToTerrain("!@#$%^&*()'[].,/.bmp");
        assertNull(testBMPNullArray);
    }

    @Test
    void BMPToIntArray() {
        int[][] testIntArray = BitmapConverter.BMPToIntArray("./resources/terrain_test.bmp", 2, 3);
        assertNotNull(testIntArray);
        assertAll(
                () -> assertEquals(3, testIntArray.length),
                () -> assertEquals(2, testIntArray[0].length),
                () -> assertEquals(2, testIntArray[1].length),
                () -> assertEquals(2, testIntArray[2].length),

                () -> assertEquals(0, testIntArray[0][0]),
                () -> assertEquals(3, testIntArray[0][1]),
                () -> assertEquals(1, testIntArray[1][0]),
                () -> assertEquals(4, testIntArray[1][1]),
                () -> assertEquals(2, testIntArray[2][0]),
                () -> assertEquals(5, testIntArray[2][1]));
        int[][] testNullArray = BitmapConverter.BMPToIntArray("!@#$%^&*()'[].,/.bmp", 500, 500);
        assertNull(testNullArray);
    }

    @Test
    void BMPToColor() {
        Color[][] testColorArray = BitmapConverter.BMPToColor("./resources/terrain_test.bmp");
        assertNotNull(testColorArray);
        assertAll(
                () -> assertEquals(3, testColorArray.length),
                () -> assertEquals(2, testColorArray[0].length),
                () -> assertEquals(2, testColorArray[1].length),
                () -> assertEquals(2, testColorArray[2].length),

                () -> assertEquals(new Color(255, 209, 94), testColorArray[0][0]),
                () -> assertEquals(new Color(0, 148, 255), testColorArray[0][1]),
                () -> assertEquals(new Color(48, 48, 48), testColorArray[1][0]),
                () -> assertEquals(new Color(0, 127, 14), testColorArray[1][1]),
                () -> assertEquals(new Color(128, 128, 128), testColorArray[2][0]),
                () -> assertEquals(new Color(255, 255, 255), testColorArray[2][1]));
        Color[][] testNullArray = BitmapConverter.BMPToColor("!@#$%^&*()'[].,/.bmp");
        assertNull(testNullArray);
    }
    @Test
    void intArrayToJSON() {
        int[][] testIntArray = new int[][] {{0, 3}, {1, 4}, {2, 5}};
        assertEquals("{\"0\":[0,3]}", BitmapConverter.intArrayToJSON(testIntArray, 0));
        assertEquals("{\"1\":[1,4]}", BitmapConverter.intArrayToJSON(testIntArray, 1));
        assertEquals("{\"2\":[2,5]}", BitmapConverter.intArrayToJSON(testIntArray, 2));
    }
}