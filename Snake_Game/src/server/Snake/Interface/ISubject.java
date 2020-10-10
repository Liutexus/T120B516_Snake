package server.Snake.Interface;

public interface ISubject
{
    boolean registerObserver(IObserver o);
    boolean unregisterObserver(IObserver o);
    boolean notifyObservers();
}
