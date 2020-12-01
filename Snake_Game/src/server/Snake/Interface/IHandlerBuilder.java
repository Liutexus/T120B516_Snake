package server.Snake.Interface;

import server.Snake.Enumerator.EClientStatus;
import server.Snake.GameLogic;
import server.Snake.Network.Handler;
import server.Snake.MatchInstance;

import java.net.Socket;
import java.util.Map;

public interface IHandlerBuilder {
    void reset();
    Handler setSocket(Socket serverSocket);
    Handler setStatus(EClientStatus status);
    Handler setMatchInstance(MatchInstance match);
    Handler setGameLogic(GameLogic gameLogic);
    Handler setBuilder(IHandlerBuilder builder);
    Handler setPlayers(Map players);
    Handler setTerrainEntities(Map terrainEntities);
    Handler getProduct();
}
