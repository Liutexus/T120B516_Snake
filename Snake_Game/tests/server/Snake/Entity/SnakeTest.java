package server.Snake.Entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import server.Snake.Enumerator.EEffect;

import java.awt.event.ItemEvent;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SnakeTest {

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, 0, 1, 1, 2, 3, 4, 8, 13, 21, 34, Integer.MAX_VALUE})
    void tailLength(int length) {
        Snake testSnake = new Snake(0,0);
        testSnake.setTailLength(length);
        assertEquals(length, testSnake.getTailLength());
        testSnake.setTailLength(length - 5);
        assertEquals(length - 5, testSnake.getTailLength());
    }

    @Test
    void deltaTailLength() {
        Snake testSnake = new Snake(0,0);
        testSnake.setTailLength(5);
        testSnake.deltaTailLength(10);
        assertEquals(15, testSnake.getTailLength());
        testSnake.deltaTailLength(-20);
        assertEquals(15, testSnake.getTailLength());
    }

    @Test
    void trimTailSizeAndPrevPos() {
        Snake testSnake = new Snake(0,0);
        testSnake.setVelocity(1f, 0.5f);
        testSnake.setTailLength(30);
        for (int i = 0; i < 50; i++) {
            testSnake.move();
        }

        testSnake.trimTailSizeAndPrevPos(60);
        assertEquals(50, testSnake.previousPositionsX.size());
        assertEquals(50, testSnake.previousPositionsY.size());
        assertEquals(30, testSnake.getTailLength());

        testSnake.trimTailSizeAndPrevPos(25);
        assertEquals(25, testSnake.previousPositionsX.size());
        assertEquals(25, testSnake.previousPositionsY.size());
        assertEquals(25, testSnake.getTailLength());

        testSnake.trimTailSizeAndPrevPos(5);
        assertEquals(5, testSnake.previousPositionsX.size());
        assertEquals(5, testSnake.previousPositionsY.size());
        assertEquals(5, testSnake.getTailLength());
        assertAll(
                () -> assertEquals(49f, testSnake.getPreviousPositionsX().get(0)),
                () -> assertEquals(48f, testSnake.getPreviousPositionsX().get(1)),
                () -> assertEquals(47f, testSnake.getPreviousPositionsX().get(2)),
                () -> assertEquals(46f, testSnake.getPreviousPositionsX().get(3)),
                () -> assertEquals(45f, testSnake.getPreviousPositionsX().get(4)),

                () -> assertEquals(24.5f, testSnake.getPreviousPositionsY().get(0)),
                () -> assertEquals(24f, testSnake.getPreviousPositionsY().get(1)),
                () -> assertEquals(23.5f, testSnake.getPreviousPositionsY().get(2)),
                () -> assertEquals(23f, testSnake.getPreviousPositionsY().get(3)),
                () -> assertEquals(22.5f, testSnake.getPreviousPositionsY().get(4))
        );
    }

    @Test
    void checkCollisionWithTail() {
        Snake testSnake = new Snake(32, 36);
        testSnake.setVelocity(0, 1.0f);
        testSnake.setPreviousPositionsX(new ArrayList<>(){
            {
                add(32f);
                add(33f);
                add(34f);
                add(35f);
                add(35f);
                add(34f);
                add(33f);
                add(32f);
                add(31f);
                add(30f);
                add(29f);
            }
        });
        testSnake.setPreviousPositionsY(new ArrayList<>(){
            {
                add(35f);
                add(35f);
                add(35f);
                add(35f);
                add(36f);
                add(36f);
                add(36f);
                add(36f);
                add(36f);
                add(36f);
                add(36f);
            }
        });

//        assertTrue(testSnake.checkCollisionWithTail());
        assertEquals(7, testSnake.getTailLength());
    }

    @Test
    void move() {
        Snake testSnake = new Snake(0,0);
        testSnake.setVelocity(1f, 0.5f);
        testSnake.move();
        testSnake.move();
        testSnake.move();
        testSnake.setEffect(EEffect.STUN, 10);
        testSnake.move();
        testSnake.move();
        testSnake.move();

        assertAll(
                () -> assertEquals(3f, testSnake.getPositionX()),
                () -> assertEquals(1.5f, testSnake.getPositionY()),
                () -> assertEquals(3, testSnake.getPreviousPositionsX().size()),
                () -> assertEquals(3, testSnake.getPreviousPositionsY().size())
        );
    }

    @Test
    void onCollide() {
        // TODO: Finish this test.
    }
}