package server.Snake.Network;

import server.Snake.Interface.IHandler;
import server.Snake.Network.Packet.Packet;

public class LoginHandler implements IHandler {
    private Handler handler;
    private IHandler nextHandler;

    public LoginHandler(Handler handler){
        this.setNext(handler);
    }

    @Override
    public void setNext(IHandler handler) {
        this.nextHandler = handler;
    }

    @Override
    public void handle(Object request) {
        switch (((Packet)request).header){
            case CLIENT_LOGIN:
                // Placeholder
                break;
            case CLIENT_LOGOUT:
                // Placeholder
                break;
            default:
                this.nextHandler.handle(request);
                break;
        }
    }
}
