package server.Snake.Entity.Builder;

import server.Snake.Enumerator.EClientStatus;
import server.Snake.Logic.ConcreteGameLogic;
import server.Snake.Logic.IGameLogic;
import server.Snake.Network.Handler;
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
    public Handler setStatus(EClientStatus status) {
        this.handler.setStatus(status);
        return this.handler;
    }

    @Override
    public Handler setMatchInstance(MatchInstance match) {
        this.handler.setMatchInstance(match);
        return this.handler;
    }

    @Override
    public Handler setGameLogic(IGameLogic gameLogic) {
        this.handler.setGameLogic(gameLogic);
        return this.handler;
    }

    @Override
    public Handler setBuilder(IHandlerBuilder builder){
        this.handler.setBuilder(this);
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
        if(this.handler.getMatchInstance() != null){
            switch (this.handler.getMatchInstance().getMatchStatus()){
                case UNDETERMINED:
                    this.handler.setStatus(EClientStatus.MENU);
                    break;
                case ONGOING:
                    this.handler.setStatus(EClientStatus.IN_GAME);
                    break;
                case WAITING:
                    this.handler.setStatus(EClientStatus.LOBBY);
                    break;
                case CONCLUDING:
                    this.handler.setStatus(EClientStatus.POST_GAME);
                    break;
                case FINISHED:
                    this.handler.setStatus(EClientStatus.DISCONNECTED);
                    break;
                default:
                    System.out.println("Unknown match state in class " + this.getClass());
                    break;
            }

        }
    }
}
