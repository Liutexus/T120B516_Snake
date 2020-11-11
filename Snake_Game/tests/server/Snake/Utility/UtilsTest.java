package server.Snake.Utility;

import org.junit.jupiter.api.Test;
import server.Snake.Entity.Player;
import server.Snake.MatchInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void randomId() {
        String testRandId = Utils.randomId();
        assertAll(
                () -> assertNotEquals(testRandId, Utils.randomId()),
                () -> assertNotEquals(testRandId, Utils.randomId()),
                () -> assertNotEquals(testRandId, Utils.randomId())

        );
    }

    @Test
    void vectorLengthToPoint() {
        float[] testPoint1 = new float[]{1, 5};
        float[] testPoint2 = new float[]{4, 1};

        float testProduct = Utils.vectorLengthToPoint(testPoint1[0], testPoint1[1], testPoint2[0], testPoint2[1]);
        assertEquals(5, testProduct);
    }

    @Test
    void vectorToPoint() {
        float[] testPoint1 = new float[]{1, 5};
        float[] testPoint2 = new float[]{4, 1};

        float[] testVector = Utils.vectorToPoint(testPoint1[0], testPoint1[1], testPoint2[0], testPoint2[1]);

        assertAll(
                () -> assertEquals(0, testVector[0]),
                () -> assertEquals(1, testVector[1])
        );
    }

    @Test
    void getPlayersTailsArray() {
        Player testPlayer1 = new Player("test1");
        Player testPlayer2 = new Player("test2");

        Map testPlayers = new HashMap<String, Player>(){
            {
                put("test1", testPlayer1);
                put("test2", testPlayer2);
            }
        };

        for(int i = 0; i < 5; i++){
            if(i % 2 == 0) {
                testPlayer1.getSnake().setVelocity(1, 0);
                testPlayer2.getSnake().setVelocity(1, 0);
            }
            else {
                testPlayer1.getSnake().setVelocity(0, 1);
                testPlayer2.getSnake().setVelocity(0, 1);
            }
            testPlayer1.getSnake().move();
            testPlayer2.getSnake().move();
        }

        Map testTailPos = Utils.getPlayersTailsArray(testPlayers);
        ArrayList testXPos = (ArrayList) testTailPos.get("x");
        ArrayList testYPos = (ArrayList) testTailPos.get("y");

        ArrayList testArrayX = new ArrayList(){
            {
                add(2f); add(2f); add(1f); add(1f); add(0f); add(2f); add(2f); add(1f); add(1f); add(0f);
            }
        };
        ArrayList testArrayY = new ArrayList(){
            {
                add(2f); add(1f); add(1f); add(0f); add(0f); add(2f); add(1f); add(1f); add(0f); add(0f);
            }
        };

        assertAll(
                () -> assertEquals(testArrayX, testXPos),
                () -> assertEquals(testArrayY, testYPos)
        );
    }

}
