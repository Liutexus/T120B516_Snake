package server.Snake.Interface;

public interface IHandler {
    void setNext(IHandler handler);
    void handle(Object request);
}
