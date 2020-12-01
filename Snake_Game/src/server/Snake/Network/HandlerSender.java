package server.Snake.Network;

import server.Snake.Entity.Player;
import server.Snake.Enumerator.EClientStatus;
import server.Snake.Enumerator.EPacketHeader;
import server.Snake.Network.Packet.Packet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class HandlerSender implements Runnable {
    private Handler handler;
    private OutputStreamWriter out;

    public HandlerSender(Handler handler, OutputStreamWriter out) {
        this.handler = handler;
        this.out = out;
    }

    @Override
    public void run() {
        final boolean[] alive = {true};
        while(alive[0]){
            if(this.handler.getServerSocket().isClosed() && this.handler.getStatus() == EClientStatus.DISCONNECTED) break;
            if(this.handler.getStatus() == EClientStatus.IN_GAME){ // If the player is in game
                synchronized (this.handler.getPlayers()){
                    this.handler.getPlayers().forEach((key, value) -> { // Send all other match players
                        try {
                            sendPacket(EPacketHeader.PLAYER, value.toString());
                        } catch (IOException e) {
                            alive[0] = false;
                            this.handler.setStatus(EClientStatus.DISCONNECTED);
                            System.out.println("Client disconnected");
//                                e.printStackTrace();
                        }
                    });
                }
                if(!alive[0]) break;
                synchronized (this.handler.getTerrainEntities()){
                    this.handler.getTerrainEntities().forEach((key, value) -> { // Send all other match entities
                        try {
                            sendPacket(EPacketHeader.ENTITY, value.toString());
                        } catch (IOException e) {
                            alive[0] = false;
                            System.out.println("Error sending a packet to the client.");
//                                e.printStackTrace();
                        }
                    });
                }
            }
            try {Thread.sleep(100);} catch (Exception e) { };
        }
    }

    public void sendClientLogin(String id, Player player) {
        try{
            this.handler.setClientId(id);
            sendPacket(EPacketHeader.ID, id);

            this.handler.setClientPlayer(player);
            sendPacket(EPacketHeader.CLIENT_PLAYER, player.toString());

        } catch (Exception e) {
            System.out.println("Error sending login info to the client.");
//                e.printStackTrace();
        }
    }

    public void sendPacket(EPacketHeader header, String body) throws IOException {
        BufferedWriter bfw = new BufferedWriter(out);
        Packet packet = new Packet(header, body);
        bfw.write(packet.toString());
        bfw.flush();
    }

}
