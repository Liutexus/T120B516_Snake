package client.Snake.Entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import server.Snake.Entity.Player;
import server.Snake.Entity.Snake;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @ParameterizedTest
    @ValueSource(strings = {"1", "idHere", "", "player", "unit"})
    void getId(String id) {
        Player testPlayer = new Player(id);
        assertEquals(id, testPlayer.getId());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "\n", "\t", "test2", "t e s t 3"})
    void setId(String id) {
        Player testPlayer = new Player("test");
        assertEquals("test", testPlayer.getId());
        testPlayer.setId(id);
        assertEquals(id, testPlayer.getId());
    }

    @Test
    void getSnake() {
        Player testPlayer = new Player("");
        assertNotNull(testPlayer.getSnake());
        assertAll(
                () -> assertEquals(0f, testPlayer.getSnake().getPositionX()),
                () -> assertEquals(0f, testPlayer.getSnake().getPositionY())
        );
    }

    @Test
    void setSnake() {
        Player testPlayer = new Player("");
        assertNotNull(testPlayer.getSnake());
        assertAll(
                () -> assertEquals(0f, testPlayer.getSnake().getPositionX()),
                () -> assertEquals(0f, testPlayer.getSnake().getPositionY())
        );
        Snake testSnake = new Snake(1.2f, 3.4f);
        testPlayer.setSnake(testSnake);
        assertNotNull(testPlayer.getSnake());
        assertAll(
                () -> assertEquals(1.2f, testPlayer.getSnake().getPositionX()),
                () -> assertEquals(3.4f, testPlayer.getSnake().getPositionY())
        );
    }

    @Test
    void getColor() {
        Player testPlayer = new Player("");
        assertEquals(Color.BLACK, testPlayer.getSnake().getColor());
    }

    @Test
    void getColorRGB() {
        Player testPlayer = new Player("");
        assertEquals(Color.BLACK.getRGB(), testPlayer.getSnake().getColor().getRGB());
    }

    @Test
    void setColor() {
        Map myMap = new HashMap();
        Player testPlayer = new Player("");
        assertEquals(Color.BLACK, testPlayer.getSnake().getColor());
        testPlayer.getSnake().setColor(Color.GREEN);
        assertEquals(Color.GREEN, testPlayer.getSnake().getColor());
        testPlayer.getSnake().setColor(Color.CYAN);
        assertEquals(Color.CYAN, testPlayer.getSnake().getColor());
        testPlayer.getSnake().setColor(Color.ORANGE);
        assertEquals(Color.ORANGE, testPlayer.getSnake().getColor());
    }

    @Test
    void getScore() {
        Player testPlayer = new Player("");
        assertEquals(0, testPlayer.getScore());
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE,1,9001,500,762,556,366,45,9,Integer.MAX_VALUE})
    void setScore(int score) {
        Player testPlayer = new Player("");
        testPlayer.setScore(score);
        assertEquals(score, testPlayer.getScore());
    }

    @Test
    void deltaScore() {
        Player testPlayer = new Player("");
        assertEquals(0, testPlayer.getScore());
        int[] testValues = {0, 0, 0, 20, -10, -5, -5, 9, 45, 12, 20, 366, 545, 556, 762, 1270, 5000};
        int expectedValue = 0;
        for (int i : testValues) {
            testPlayer.deltaScore(i);
            expectedValue += i;
            assertEquals(expectedValue, testPlayer.getScore());
        }
    }

    @Test
    void getIsGameOver() {
        Player testPlayer = new Player("");
        assertFalse(testPlayer.getIsGameOver());
    }

    @Test
    void setGameOver() {
        Player testPlayer = new Player("");
        assertFalse(testPlayer.getIsGameOver());
        testPlayer.setGameOver(true);
        assertTrue(testPlayer.getIsGameOver());
    }

    @Test
    void testClone() {
        Player testPlayer = new Player("");
        testPlayer.deltaScore(50);
        testPlayer.setGameOver(true);
        testPlayer.setId("testClone");

        Player clonePlayer = testPlayer.clone();
        assertEquals(testPlayer.toString(), clonePlayer.toString());
    }

    @Test
    void mapToObject() {
        Map<String, Object> testSnakeMap = new HashMap<String, Object>() {
            {
                put("previousPositionsX", new ArrayList<Float>(){
                    {
                        add(0f);
                        add(1f);
                        add(2f);
                        add(3f);
                        add(4f);
                        add(5f);
                    }
                });
                put("previousPositionsY", new ArrayList<Float>(){
                    {
                        add(0.5f);
                        add(1.5f);
                        add(2.5f);
                        add(3.5f);
                        add(4.5f);
                        add(5.5f);
                    }
                });
                put("tailLength", 5);
                put("velocity", new ArrayList<Float>(){
                    {
                        add(1f);
                        add(2f);
                    }
                });
                put("size", new ArrayList<Float>(){
                    {
                        add(2f);
                        add(3f);
                    }
                });
                put("position", new ArrayList<Float>(){
                {
                    add(20.7f);
                    add(15.2f);
                }
                });
                put("colorRGB", Color.ORANGE.getRGB());
            }
        };

        Map<String, Object> testPlayerMap = new HashMap<String, Object>() {
            {
                put("id", "t e s t");
                put("snake", testSnakeMap);
                put("score", 5000);
                put("isGameOver", true);
            }

        };
        Player testPlayer = new Player("");
        testPlayer.mapToObject(testPlayerMap);

        assertAll(
                () -> assertEquals("t e s t", testPlayer.getId()),

                () -> assertEquals(2f, testPlayer.getSnake().getPreviousPositionsX().get(2)),
                () -> assertEquals(2.5f, testPlayer.getSnake().getPreviousPositionsY().get(2)),
                () -> assertEquals(5, testPlayer.getSnake().getTailLength()),
                () -> assertEquals(1f, testPlayer.getSnake().getVelocityX()),
                () -> assertEquals(2f, testPlayer.getSnake().getVelocityY()),
                () -> assertEquals(2f, testPlayer.getSnake().getSizeX()),
                () -> assertEquals(3f, testPlayer.getSnake().getSizeY()),
                () -> assertEquals(20.7f, testPlayer.getSnake().getPositionX()),
                () -> assertEquals(15.2f, testPlayer.getSnake().getPositionY()),

                () -> assertEquals(5000, testPlayer.getScore()),
                () -> assertEquals(Color.ORANGE.getRGB(), testPlayer.getSnake().getColorRGB()),
                () -> assertTrue(testPlayer.getIsGameOver())
        );
    }

    @Test
    void testToString() {
        Player testPlayer = new Player("toStringTest", 0.5f, 1.5f);
        testPlayer.setGameOver(true);
        testPlayer.setScore(556);

        Snake testSnake = new Snake(1.2f, 3.4f);
        testSnake.setSize(5.6f, 7.8f);
        testSnake.setVelocity(9.10f, 10.11f);
        testSnake.setTailLength(12);
        testSnake.setPreviousPositionsX(new ArrayList<Float>() {{
            add(1f);
            add(2f);
            add(3f);
            add(4f);
            add(5f);
        }});
        testSnake.setPreviousPositionsY(new ArrayList<Float>() {{
            add(1.5f);
            add(2.5f);
            add(3.5f);
            add(4.5f);
            add(5.5f);
        }});
        testSnake.setColor(Color.CYAN);

        testPlayer.setSnake(testSnake);
        assertEquals("{\"id\":\"toStringTest\",\"snake\":{\"previousPositionsX\":[1.0,2.0,3.0,4.0,5.0],\"previousPositionsY\":[1.5,2.5,3.5,4.5,5.5],\"tailLength\":12,\"velocity\":[9.1,10.11],\"size\":[5.6,7.8],\"position\":[1.2,3.4],\"colorRGB\":-16711681},\"score\":556,\"isGameOver\":true}",
                             testPlayer.toString());
    }
}