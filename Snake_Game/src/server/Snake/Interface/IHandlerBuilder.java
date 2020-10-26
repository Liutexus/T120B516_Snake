package server.Snake.Interface;

import server.Snake.GameLogic;
import server.Snake.Handler;
import server.Snake.MatchInstance;

import java.net.Socket;
import java.util.Map;

public interface IHandlerBuilder {
    void reset();
    Handler setSocket(Socket serverSocket);
    Handler setMatchInstance(MatchInstance match);
    Handler setGameLogic(GameLogic gameLogic);
    Handler setPlayers(Map players);
    Handler setTerrainEntities(Map terrainEntities);
    Handler getProduct();
}
