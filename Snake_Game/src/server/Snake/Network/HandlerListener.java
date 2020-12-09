package server.Snake.Network;

import server.Snake.Interpreter.Interpreter;
import server.Snake.Enumerator.EClientStatus;
import server.Snake.Enumerator.EPacketHeader;
import server.Snake.Interface.IHandler;
import server.Snake.Network.Packet.Packet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketException;

public class HandlerListener implements Runnable, IHandler {
    private Handler handler;
    private InputStreamReader in;
    private IHandler nextHandler;

    public HandlerListener(Handler handler, InputStreamReader in) {
        this.handler = handler;
        this.in = in;
        this.setNext(new LoginHandler(this.handler));
    }

    @Override
    public void run() {
        BufferedReader inb = new BufferedReader(in);
        while(true) {
            if(this.handler.getServerSocket().isClosed() && this.handler.getStatus() == EClientStatus.DISCONNECTED) break;
            try {
                parsePacket(inb.readLine());
            } catch (Exception e) {
                if(this.handler.getServerSocket().isClosed()) break;
                if(e instanceof SocketException) {
                    break;
                }
                e.printStackTrace();
                continue;
            }
        }
    }

    private void parsePacket(String packetJson){
        Packet packet = new Packet(packetJson);
        switch (packet.header){
            case CLIENT_RESPONSE:
                this.handler.getGameLogic().updatePlayerField(packet.parseBody());
                break;
            case COMMAND:
                Interpreter.execute(packet.getBody(), handler);
                break;
            default:
                this.handle(packet);
//                System.out.println("Error. Not recognised packet header '" + packet.header.toString() + "'. ");
                break;
        }
    }

    @Override
    public void setNext(IHandler handler) {
        this.nextHandler = handler;
    }

    @Override
    public void handle(Object request) {
        this.nextHandler.handle(request);
    }
}
