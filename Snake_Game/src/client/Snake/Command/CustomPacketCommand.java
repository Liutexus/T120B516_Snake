package client.Snake.Command;

import client.Snake.Interface.ICommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import server.Snake.Enumerator.EPacketHeader;
import server.Snake.Network.Packet.Packet;

import java.io.OutputStreamWriter;
import java.util.HashMap;

public class CustomPacketCommand extends TemplateCommand{
    private static ObjectWriter objectMapper = new ObjectMapper().writer();
    private static HashMap<Object, Object> packetMap = new HashMap<>();
    private static Packet packet;
    private static ICommand action;

    public static void customPacket(EPacketHeader header, String body, OutputStreamWriter out){
        packet = new Packet(header);
        action = new CustomPacket();
        action.execute(body, out);
    }

    @Override
    void undo(String id, OutputStreamWriter out) {
        action.undo(id, out);
    }

    @Override
    String getString() {
        return "current command: " + action.toString();
    }

    private static class CustomPacket implements ICommand{
        @Override
        public void execute(String body, OutputStreamWriter out) {
            try {
                packet.setBody(body);
                out.write(packet.toString());
                out.flush();
            } catch (Exception e) {
                System.out.println("Error sending an input to the server.");
//                e.printStackTrace();
            }
        }

        @Override
        public void undo(String body, OutputStreamWriter out) {
            action = new CustomPacket();
            action.execute(null, out);
        }
    }

}
