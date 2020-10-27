package server.Snake.Builder;

import server.Snake.GameLogic;
import server.Snake.Handler;
import server.Snake.Interface.IHandlerBuilder;
import server.Snake.Interface.IObserver;
import server.Snake.MatchInstance;

import java.net.Socket;
import java.util.Map;

public class HandlerBuilder implements IHandlerBuilder, IObserver {
    private Handler handler;

    public HandlerBuilder(){
        this.reset();
    }

    @Override
    public void reset() {
        this.handler = new Handler();
    }

    @Override
    public Handler setSocket(Socket serverSocket) {
        this.handler.setServerSocket(serverSocket);
        return this.handler;
    }

    @Override
    public Handler setMatchInstance(MatchInstance match) {
        this.handler.setMatchInstance(match);
        return this.handler;
    }

    @Override
    public Handler setGameLogic(GameLogic gameLogic) {
        this.handler.setGameLogic(gameLogic);
        return this.handler;
    }

    @Override
    public Handler setPlayers(Map players) {
        this.handler.setPlayers(players);
        return this.handler;
    }

    @Override
    public Handler setTerrainEntities(Map terrainEntities) {
        this.handler.setTerrainEntities(terrainEntities);
        return this.handler;
    }

    @Override
    public Handler getProduct() {
        return this.handler;
    }

    @Override
    public void update() {

    }
}
