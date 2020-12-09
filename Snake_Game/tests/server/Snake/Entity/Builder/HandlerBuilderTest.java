package server.Snake.Entity.Builder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Player;
import server.Snake.Enumerator.EClientStatus;
import server.Snake.Enumerator.EMatchStatus;
import server.Snake.Logic.ConcreteGameLogic;
import server.Snake.MatchInstance;
import server.Snake.Server;
import server.Snake.Utility.Utils;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class HandlerBuilderTest {
    private ExecutorService pool;
    private HandlerBuilder testHandlerBuilder;

    @BeforeEach
    void setUp() {
        pool = Executors.newFixedThreadPool(3);
        this.testHandlerBuilder = new HandlerBuilder();
    }

    @Test
    void reset() {
    }

    @Test
    void setSocket() {
        pool.execute( () -> {
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
        this.testHandlerBuilder.setSocket(testSocket);
        assertEquals(testSocket, testHandlerBuilder.getProduct().getServerSocket());
        Server.close();
    }

    @Test
    void setStatus() {
        this.testHandlerBuilder.setStatus(EClientStatus.MENU);
        assertEquals(EClientStatus.MENU, this.testHandlerBuilder.getProduct().getStatus());
        this.testHandlerBuilder.setStatus(EClientStatus.UNDETERMINED);
        assertEquals(EClientStatus.UNDETERMINED, this.testHandlerBuilder.getProduct().getStatus());
        this.testHandlerBuilder.setStatus(EClientStatus.LOBBY);
        assertEquals(EClientStatus.LOBBY, this.testHandlerBuilder.getProduct().getStatus());
        this.testHandlerBuilder.setStatus(EClientStatus.IN_GAME);
        assertEquals(EClientStatus.IN_GAME, this.testHandlerBuilder.getProduct().getStatus());
        this.testHandlerBuilder.setStatus(EClientStatus.POST_GAME);
        assertEquals(EClientStatus.POST_GAME, this.testHandlerBuilder.getProduct().getStatus());
        this.testHandlerBuilder.setStatus(EClientStatus.DISCONNECTED);
        assertEquals(EClientStatus.DISCONNECTED, this.testHandlerBuilder.getProduct().getStatus());
    }

    @Test
    void setMatchInstance() {
        MatchInstance testMatchInstance = new MatchInstance("test");
        this.testHandlerBuilder.setMatchInstance(testMatchInstance);
        assertEquals(testMatchInstance, this.testHandlerBuilder.getProduct().getMatchInstance());
    }

    @Test
    void setGameLogic() {
        ConcreteGameLogic testGameLogic = new ConcreteGameLogic();
        this.testHandlerBuilder.setGameLogic(testGameLogic);
        assertEquals(testGameLogic, this.testHandlerBuilder.getProduct().getGameLogic());
    }

    @Test
    void setBuilder() {
        this.testHandlerBuilder.setBuilder(this.testHandlerBuilder);
        assertEquals(this.testHandlerBuilder, this.testHandlerBuilder.getProduct().getBuilder());
    }

    @Test
    void setPlayers() {
        Map testPlayers = new HashMap<String, Player>(){
            {
                put("test1", new Player("test1"));
                put("test2", new Player("test2"));
                put("test3", new Player("test3"));
            }
        };
        this.testHandlerBuilder.setPlayers(testPlayers);
        assertEquals(testPlayers, this.testHandlerBuilder.getProduct().getPlayers());
    }

    @Test
    void setTerrainEntities() {
        Map testTerrainEntities = new HashMap<String, Entity>(){
            {
                put("test1", new Entity(0, 0));
                put("test2", new Entity(0, 0));
                put("test3", new Entity(0, 0));
            }
        };
        this.testHandlerBuilder.setTerrainEntities(testTerrainEntities);
        assertEquals(testTerrainEntities, this.testHandlerBuilder.getProduct().getTerrainEntities());
    }

    @ParameterizedTest
    @EnumSource(EMatchStatus.class)
    void update(EMatchStatus status) {
        MatchInstance testMatchInstance = new MatchInstance("test");
        this.testHandlerBuilder.setMatchInstance(testMatchInstance);
        this.testHandlerBuilder.getProduct().getMatchInstance().setMatchStatus(status);
        this.testHandlerBuilder.update();
        assertEquals(status, this.testHandlerBuilder.getProduct().getMatchInstance().getMatchStatus());
    }
}