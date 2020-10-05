package server.Snake.Interface;

public interface Subject
{
    boolean registerObserver(Observer o);
    boolean unregisterObserver(Observer o);
    boolean notifyObservers();
}
