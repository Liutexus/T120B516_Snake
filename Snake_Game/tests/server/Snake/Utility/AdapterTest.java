package server.Snake.Utility;

import org.junit.jupiter.api.Test;
import server.Snake.Entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AdapterTest {

    @Test
    void mapToEntity() {
        Map testMap = new HashMap<String, Object>(){
            {
                put("position", new ArrayList(){
                    {
                        add(1f);
                        add(1f);
                    }
                });
                put("size", new ArrayList(){
                    {
                        add(1f);
                        add(1f);
                    }
                });
                put("colorRGB", -10312600);
            }
        };
        Entity testEntity = Adapter.mapToEntity(testMap);
        assertAll(
                () -> assertEquals(((ArrayList)testMap.get("position")).get(0), testEntity.getPosition()[0]),
                () -> assertEquals(((ArrayList)testMap.get("position")).get(1), testEntity.getPosition()[1]),

                () -> assertEquals(((ArrayList)testMap.get("size")).get(0), testEntity.getSize()[0]),
                () -> assertEquals(((ArrayList)testMap.get("size")).get(1), testEntity.getSize()[1]),

                () -> assertEquals(testMap.get("colorRGB"), testEntity.getColorRGB())
        );
    }
}