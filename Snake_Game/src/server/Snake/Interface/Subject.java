package server.Snake.Interface;

import server.Snake.Handler;

public interface Subject
{
    public boolean registerObserver(Observer o);
    public boolean unregisterObserver(Observer o);
    public boolean notifyObservers();
}
