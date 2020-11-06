package server.Snake.Entity;

import org.junit.jupiter.api.Test;
import server.Snake.Enumerator.EEffect;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void getPositionX() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        assertEquals(1.2f, testEntity.positionX);
    }

    @Test
    void setPositionX() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        testEntity.setPositionX(12);
        assertEquals(12, testEntity.positionX);
    }

    @Test
    void getPositionY() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        assertEquals(3.4f, testEntity.positionY);
    }

    @Test
    void setPositionY() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        testEntity.setPositionY(34);
        assertEquals(34, testEntity.positionY);
    }

    @Test
    void getPosition() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        float[] pos = testEntity.getPosition();
        assertEquals((float)1.2, pos[0]);
        assertEquals((float)3.4, pos[1]);
    }

    @Test
    void setPosition() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        testEntity.setPosition(12, 34);
        assertEquals(12, testEntity.positionX);
        assertEquals(34, testEntity.positionY);
    }

    @Test
    void nextPosition() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        testEntity.nextPosition(20, 40, 30, 50);
        assertTrue(20 <= testEntity.positionX && testEntity.positionX <= 30);
        assertTrue(40 <= testEntity.positionY && testEntity.positionY <= 50);
    }

    @Test
    void getSizeX() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        assertEquals(1, testEntity.getSizeX());
    }

    @Test
    void setSizeX() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        testEntity.setSizeX(5);
        assertEquals(5, testEntity.sizeX);
    }

    @Test
    void getSizeY() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        assertEquals(1, testEntity.getSizeY());
    }

    @Test
    void setSizeY() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        testEntity.setSizeY(5);
        assertEquals(5, testEntity.sizeY);
    }

    @Test
    void getSize() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        float[] sizes = testEntity.getSize();
        assertEquals(1, sizes[0]);
        assertEquals(1, sizes[1]);
    }

    @Test
    void setSize() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        testEntity.setSize(5.6f, 7.8f);
        float[] sizes = testEntity.getSize();
        assertEquals((float)5.6, sizes[0]);
        assertEquals((float)7.8, sizes[1]);
    }

    @Test
    void deltaSize() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        testEntity.deltaSize(3, 4);
        assertEquals(4, testEntity.sizeX);
        assertEquals(5, testEntity.sizeY);
    }

    @Test
    void setEffect() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        testEntity.setEffect(EEffect.INVISIBILITY, 20);
        assertEquals(20, testEntity.getEffects().get(EEffect.INVISIBILITY));
    }

    @Test
    void getEffects() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        assertEquals(0, testEntity.getEffects().entrySet().size());
        testEntity.setEffect(EEffect.HASTE, 50);
        testEntity.setEffect(EEffect.INVISIBILITY, 20);
        assertEquals(2, testEntity.getEffects().entrySet().size());
        assertEquals(20, testEntity.getEffects().get(EEffect.INVISIBILITY));
        assertEquals(50, testEntity.getEffects().get(EEffect.HASTE));
    }

    @Test
    void testToString() {
        Entity testEntity = new Entity(1.2f, 3.4f);
        assertEquals("{\"shape\":null,\"shapetype\":0,\"size\":[1.0,1.0],\"position\":[1.2,3.4]}", testEntity.toString());
    }
}