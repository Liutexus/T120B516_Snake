package server.Snake.Network;

import server.Snake.Enumerator.EClientStatus;
import server.Snake.MatchInstance;
import server.Snake.Network.Packet.Packet;
import server.Snake.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketException;

public class HandlerListener implements Runnable{
    Handler handler;
    InputStreamReader in;

    public HandlerListener(Handler handler, InputStreamReader in) {
        this.handler = handler;
        this.in = in;
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
            case EMPTY:
                break;
            case CLIENT_RESPONSE:
                this.handler.getGameLogic().updatePlayerField(packet.parseBody());
                break;
            case CLIENT_REQUEST_MATCH_JOIN:
                MatchInstance matchInstance = Server.returnAvailableMatch();
                matchInstance.registerObserver(this.handler.getBuilder());
                break;
            default:
                System.out.println("Error. Not recognised packet header '" + packet.header.toString() + "'. ");
                break;
        }
    }

}
