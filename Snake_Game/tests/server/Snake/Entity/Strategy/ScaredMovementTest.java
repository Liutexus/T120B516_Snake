package server.Snake.Entity.Strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Obstacle.Moving.Hawk;
import server.Snake.Entity.Player;
import server.Snake.Interface.IMovingEntityBehaviour;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ScaredMovementTest {
    private IMovingEntityBehaviour testMovingStrategy;
    private AbstractMovingEntity testEntity;
    private Map<String, Player> testPlayers;
    private int[][] testTerrain;

    @BeforeEach
    void setUp() {
        this.testMovingStrategy = new ScaredMovement();
        testEntity = new Hawk(new Entity(15, 15));
        testPlayers = new HashMap<String, Player>(){
            {
                put("test1", new Player("test1", 8 ,17));
                put("test2", new Player("test2", 25 ,20));
                put("test3", new Player("test3", 17 ,12));
            }
        };

        Random rand = new Random();
        testTerrain = new int[50][50];
        for(int i = 0; i < 50; i++)
            for(int j = 0; j < 50; j++)
                testTerrain[i][j] = rand.nextInt(7);
    }

    @Test
    void move() {
        float initPosX = this.testEntity.getPositionX();
        float initPosY = this.testEntity.getPositionY();

        while(initPosX == this.testEntity.getPositionX() || initPosY == this.testEntity.getPositionY())
            for(int i = 0; i < 10; i++)
                this.testMovingStrategy.move(this.testEntity);

        assertNotEquals(initPosX, this.testEntity.getPositionX());
        assertNotEquals(initPosY, this.testEntity.getPositionY());
    }

    @Test
    void testMove() {
        float initPosX = this.testEntity.getPositionX();
        float initPosY = this.testEntity.getPositionY();

        boolean loop = true;
        while(loop){
            for (Player player : this.testPlayers.values()) {

                if(this.testEntity.getPositionX() != player.getSnake().getPositionX() &&
                        this.testEntity.getVelocityX() != player.getSnake().getPositionY())
                    this.testMovingStrategy.move(this.testEntity, this.testPlayers);
                else
                    loop = false;
            }
        }

        assertNotEquals(initPosX, this.testEntity.getPositionX());
        assertNotEquals(initPosY, this.testEntity.getPositionY());
    }

    @Test
    void testMove1() {
        float initPosX = this.testEntity.getPositionX();
        float initPosY = this.testEntity.getPositionY();

        boolean loop = true;
        while(loop){
            for (Player player : this.testPlayers.values()) {

                if(this.testEntity.getPositionX() != player.getSnake().getPositionX() &&
                        this.testEntity.getVelocityX() != player.getSnake().getPositionY())
                    this.testMovingStrategy.move(this.testEntity, this.testPlayers, this.testTerrain);
                else
                    loop = false;
            }
        }

        assertNotEquals(initPosX, this.testEntity.getPositionX());
        assertNotEquals(initPosY, this.testEntity.getPositionY());
    }
}