package client.Snake.Renderer;

import client.Snake.Renderer.Panels.SnakePanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Snake.Server;
import server.Snake.Utility.Utils;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class SnakePanelTest {
    private ExecutorService pool;

    @BeforeEach
    void setUp() {
        pool = Executors.newFixedThreadPool(3);
    }

    @Test
    void getInstance() {
        pool.execute(() -> {
            Server.run();
        });
        Socket testSocket = null;
        try {
            testSocket = new Socket(
                    Utils.parseConfig("network", "address"),
                    Integer.parseInt(Utils.parseConfig("network", "port")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.Snake.Renderer.Panels.SnakePanel testSnakePanel = SnakePanel.getInstance(testSocket);

        assertNotNull(testSnakePanel);

        testSnakePanel.run();

        Server.close();
    }

}