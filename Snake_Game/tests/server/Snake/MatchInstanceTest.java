package server.Snake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Snake.Entity.Builder.HandlerBuilder;
import server.Snake.Enumerator.EClientStatus;
import server.Snake.Enumerator.EMatchStatus;
import server.Snake.Utility.Utils;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class MatchInstanceTest {
    private ExecutorService pool;
    private MatchInstance testMatch;
    private HandlerBuilder testHandlerBuilder;

    @BeforeEach
    void setUp() {
        this.pool = Executors.newFixedThreadPool(3);
        this.testMatch = new MatchInstance("test");
        this.testHandlerBuilder = new HandlerBuilder();

        this.testMatch.registerObserver(this.testHandlerBuilder);
        this.testHandlerBuilder.setMatchInstance(this.testMatch);
        for(int i = 0; i < Integer.parseInt(Utils.parseConfig("server", "maxPlayersPerMatch")); i++){
            HandlerBuilder testHandlerBuilder = new HandlerBuilder();
            testHandlerBuilder.setMatchInstance(this.testMatch);
            this.testMatch.registerObserver(testHandlerBuilder);
        }

    }

    @Test
    void getCurrentPlayerCount() {
        assertEquals(1, this.testMatch.getCurrentPlayerCount());
        this.testMatch.unregisterObserver(this.testHandlerBuilder);
        assertEquals(0, this.testMatch.getCurrentPlayerCount());
    }

    @Test
    void getMaxPlayerCount() {
        assertEquals(Integer.parseInt(Utils.parseConfig("server", "maxPlayersPerMatch")), this.testMatch.getMaxPlayerCount());
    }

    @Test
    void matchStatus() {
        pool.execute(() -> {
            this.testMatch.run();
        });

        try {
            synchronized (this){
                this.wait(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.testMatch.setMatchStatus(EMatchStatus.FINISHED);

        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(EMatchStatus.FINISHED, this.testMatch.getMatchStatus());
    }

    @Test
    void notifyObservers() {
        this.testMatch.notifyObservers();
        ((Map<Integer, HandlerBuilder>)this.testMatch.getHandlers()).forEach((key, value) -> {
            assertEquals(EClientStatus.LOBBY, value.getProduct().getStatus());
        });

    }
}