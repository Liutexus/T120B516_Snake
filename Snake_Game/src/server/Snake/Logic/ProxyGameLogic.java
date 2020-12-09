package server.Snake.Logic;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Player;
import server.Snake.Interface.IHandler;
import server.Snake.MatchInstance;

import java.util.Map;

public class ProxyGameLogic implements Runnable, IHandler, IGameLogic {

    ConcreteGameLogic concreteGameLogic;

    Map handlers;
    Map players;
    MatchInstance matchInstance;
    Map terrainEntities;
    int[][] terrain;
    boolean instanceDataStored;

    public ProxyGameLogic() {}

    public ProxyGameLogic(Map handlers, Map players, MatchInstance matchInstance, Map terrainEntities, int[][] terrain){
        this.handlers = handlers;
        this.players = players;
        this.matchInstance = matchInstance;
        this.terrainEntities = terrainEntities;
        this.terrain = terrain;
        this.instanceDataStored = true;
    }

    @Override
    public void addPlayer(Player player) {
        if(concreteGameLogic == null)
            instantiateLogic();
        concreteGameLogic.addPlayer(player);
    }

    @Override
    public void updatePlayerField(Map map) {
        if(concreteGameLogic == null)
            instantiateLogic();
        concreteGameLogic.updatePlayerField(map);
    }

    @Override
    public void run() {
        if(concreteGameLogic == null)
            instantiateLogic();
        concreteGameLogic.run();
    }

    @Override
    public void setNext(IHandler handler) {
        if(concreteGameLogic == null)
            instantiateLogic();
        concreteGameLogic.setNext(handler);
    }

    @Override
    public void handle(Object request) {
        if(concreteGameLogic == null)
            instantiateLogic();
        concreteGameLogic.handle(request);
    }

    private void instantiateLogic() {
        this.concreteGameLogic = instanceDataStored ? new ConcreteGameLogic(handlers, players, matchInstance, terrainEntities, terrain) : new ConcreteGameLogic();
    }
}
