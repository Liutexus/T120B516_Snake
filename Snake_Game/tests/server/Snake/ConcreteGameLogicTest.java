package server.Snake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Snake.Entity.Player;
import server.Snake.Logic.ConcreteGameLogic;
import server.Snake.Utility.Utils;

class ConcreteGameLogicTest {
    private ConcreteGameLogic testGameLogic;

    @BeforeEach
    void setUp() {
        this.testGameLogic = new ConcreteGameLogic();

        for(int i = 0; i < Integer.parseInt(Utils.parseConfig("server", "maxPlayersPerMatch")); i++)
            this.testGameLogic.addPlayer(new Player("test" + i));
    }

    @Test
    void addPlayer() {
        Player testPlayer = new Player("test");




    }

    @Test
    void updatePlayerField() {

    }
}