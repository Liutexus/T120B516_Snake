package server.Snake;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import server.Snake.Entity.Builder.HandlerBuilder;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Obstacle.Moving.Hawk;
import server.Snake.Entity.Player;
import server.Snake.Enumerator.EPacketHeader;
import server.Snake.Utility.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class HandlerTest {
    private ExecutorService pool;
    private Handler testHandler;
    private HandlerBuilder testBuilder;
    private MatchInstance testMatch;

    @BeforeEach
    void setUp() {
        pool = Executors.newFixedThreadPool(3);
        testHandler = new Handler();
        testBuilder = new HandlerBuilder();
        testMatch = new MatchInstance("test");
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @ValueSource(strings = {"msg0", "message_1", "MESSAGE2", "m e s s a g e 3"})
    void run(String message) {
        final Socket[] clientSocket = {new Socket()};

        testHandler.setMatchInstance(testMatch);
        this.testHandler.setBuilder(testBuilder);

        pool.execute(() -> {
            try (var listener = new ServerSocket(Integer.parseInt(Utils.parseConfig("network", "port")))) {
                testHandler.setServerSocket(listener.accept());
                synchronized (this){
                    this.notify();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pool.execute(() -> {
            try { // Connecting to the server
                clientSocket[0] = new Socket(
                        Utils.parseConfig("network", "address"),
                        Integer.parseInt(Utils.parseConfig("network", "port")));
            } catch (Exception e) {
                System.out.println("No server to connect to.");
            }
        });

        try {
            synchronized (this){
                this.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        testHandler.sendLoginInfo(message, new Player(message));
        try {
            InputStreamReader in = new InputStreamReader(clientSocket[0].getInputStream());
            BufferedReader inb = new BufferedReader(in);
            assertEquals("{\"ID\":\"" + message + "\"}", inb.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        testHandler.sendPacket(EPacketHeader.EMPTY, message);
        try {
            InputStreamReader in = new InputStreamReader(clientSocket[0].getInputStream());
            BufferedReader inb = new BufferedReader(in);
            assertEquals("{\"EMPTY\":\"" + message + "\"}", inb.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        pool.execute(() -> {
            try {
                synchronized (this){
                    this.wait(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            testHandler.shutdown();
        });
        testHandler.run();
    }

    @Test
    void setGameLogic() {
        GameLogic testGameLogic = new GameLogic();
        testHandler.setGameLogic(testGameLogic);
        assertEquals(testGameLogic, testHandler.getGameLogic());
    }

    @Test
    void setPlayers() {
        Map testPlayers = new HashMap<String, Player>();
        testPlayers.put("test", new Player("test"));
        testHandler.setPlayers(testPlayers);
        assertEquals(testPlayers, testHandler.getPlayers());
    }

    @Test
    void setTerrainEntities() {
        Map testTerrain = new HashMap<String, Entity>();
        testTerrain.put("test", new Hawk(new Entity(0, 0)));
        testHandler.setTerrainEntities(testTerrain);
        assertEquals(testTerrain, testHandler.getTerrainEntities());
    }

}
