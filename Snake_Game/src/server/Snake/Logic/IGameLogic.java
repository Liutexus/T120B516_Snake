package server.Snake.Logic;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Player;
import server.Snake.Interface.IHandler;

import java.util.Map;

public interface IGameLogic extends IHandler, Runnable{
    public void addPlayer(Player player);
    public void updatePlayerField(Map map);
    public void run();
    public void setNext(IHandler handler);
    public void handle(Object request);
}
