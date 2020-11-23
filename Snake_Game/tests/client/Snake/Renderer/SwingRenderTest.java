package client.Snake.Renderer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import client.Snake.Renderer.Enumerator.ERendererState;

import static org.junit.jupiter.api.Assertions.*;

class SwingRenderTest {
    private ExecutorService pool;

    @BeforeEach
    void setUp() {
        pool = Executors.newFixedThreadPool(3);
    }

    @Test
    void run() {
        SwingRender testRender = new SwingRender();

        pool.execute(() -> {
            testRender.run();
            synchronized (this){
                this.notify();
            }
        });

        pool.execute(() -> {
            synchronized (this){
                try {
                    this.wait(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            testRender.close();
        });

        try {
            synchronized (this){
                this.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(ERendererState.CLOSED, testRender.getCurrentState());
    }
}