package server.Snake.Packet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import server.Snake.Entity.Player;
import server.Snake.Entity.Snake;
import server.Snake.Enumerator.EPacketHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PacketTest {

    @Test
    void constructors(){
        Packet testPacket = new Packet();
        Packet testPacket2 = new Packet("\"message");
        Packet testPacket3 = new Packet(EPacketHeader.CLIENT_RESPONSE);

        Map testPacketMap = new HashMap<Object, Object>();
        ObjectWriter objectMapper = new ObjectMapper().writer();
        try {
            testPacket3.setBody(objectMapper.writeValueAsString(testPacketMap));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Packet testPacket4 = new Packet(testPacket3.toString());

        assertAll(
                () -> assertNull(testPacket.getBody()),
                () -> assertNull(testPacket2.getBody()),
                () -> assertEquals("{}", testPacket3.getBody()),
                () -> assertEquals("{}", testPacket4.getBody())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "\n", "message1", "MESSAGE2", "m e s s a g e 3"})
    void body(String body) {
        Packet testPacket = new Packet(EPacketHeader.EMPTY);
        testPacket.setBody(body);
        if(body.compareTo("") == 0)
            assertEquals(null, testPacket.getBody());
        else
            assertEquals(body, testPacket.getBody());
    }

    @Test
    void parseBody() {
        Player testPlayer = new Player("test");
        Packet testPacket = new Packet(EPacketHeader.PLAYER, testPlayer.toString());
        Packet testPacket2 = new Packet(testPacket.toString());
        Map<String, Object> testMap = testPacket2.parseBody();

        Snake tempSnake = new Snake(0, 0);
        LinkedHashMap<String, Object> snakeTest = new LinkedHashMap<>(){{
            put("previousPositionsX", new ArrayList<Float>());
            put("previousPositionsY", new ArrayList<Float>());
            put("tailLength", 10);
            put("velocity", new ArrayList<Float>() {
                {
                    add(0f);
                    add(0f);
                }
            });
            put("size", new ArrayList<Float>() {
                {
                    add(1f);
                    add(1f);
                }
            });
            put("position", new ArrayList<Float>() {
                {
                    add(0f);
                    add(0f);
                }
            });
            put("colorRGB", -16777216);
        }};

        assertAll(
                () -> assertEquals(0, testMap.get("score")),
                () -> assertEquals(false, testMap.get("isGameOver")),
                () -> assertEquals(snakeTest.toString(), ((LinkedHashMap)testMap.get("snake")).toString()),
                () -> assertEquals("test", testMap.get("id"))
        );
    }
}