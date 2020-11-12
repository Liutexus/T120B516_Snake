package client.Snake.Renderer.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import server.Snake.Enumerator.EPacketHeader;
import server.Snake.Packet.Packet;

import java.io.OutputStreamWriter;
import java.util.HashMap;

public class NetworkCommand {
    private static ObjectWriter objectMapper = new ObjectMapper().writer();
    private static HashMap<Object, Object> packetMap = new HashMap<>();
    private static Packet packet = new Packet(EPacketHeader.CLIENT_RESPONSE);
    private static ICommand action;

    public static void requestLogin(String id, OutputStreamWriter out){
        action = new RequestLogin();
        action.execute(id, out);
    }

    public static void requestLogout(String id, OutputStreamWriter out){
        action = new RequestLogout();
        action.execute(id, out);
    }

    public static void requestMatchJoin(String id, OutputStreamWriter out){
        action = new RequestMatchJoin();
        action.execute(id, out);
    }

    public static void requestMatchLeave(String id, OutputStreamWriter out){
        action = new RequestMatchLeave();
        action.execute(id, out);
    }

    public static void undo(String id, OutputStreamWriter out){
        action.undo(id, out);
    }

    private static class RequestLogin implements ICommand{
        @Override
        public void execute(String id, OutputStreamWriter out) {
            packetMap.clear();

            try {
                packet = new Packet(EPacketHeader.CLIENT_LOGIN);
                packet.setBody(objectMapper.writeValueAsString(packetMap));
                out.write(packet.toString());
                out.flush();
            } catch (Exception e) {
                System.out.println("Error sending an input to the server.");
//                e.printStackTrace();
            }
        }

        @Override
        public void undo(String id, OutputStreamWriter out) {
            action = new RequestLogout();
            action.execute(id, out);
        }
    }

    private static class RequestLogout implements ICommand{
        @Override
        public void execute(String id, OutputStreamWriter out) {
            packetMap.clear();
            packetMap.put("id", id);

            try {
                packet = new Packet(EPacketHeader.CLIENT_LOGOUT);
                packet.setBody(objectMapper.writeValueAsString(packetMap));
                out.write(packet.toString());
                out.flush();
            } catch (Exception e) {
                System.out.println("Error sending an input to the server.");
//                e.printStackTrace();
            }
        }

        @Override
        public void undo(String id, OutputStreamWriter out) {
            action = new RequestLogin();
            action.execute(id, out);
        }
    }

    private static class RequestMatchJoin implements ICommand{
        @Override
        public void execute(String id, OutputStreamWriter out) {
            packetMap.clear();

            try {
                packet = new Packet(EPacketHeader.CLIENT_REQUEST_MATCH_JOIN);
                packet.setBody(objectMapper.writeValueAsString(packetMap));
                out.write(packet.toString());
                out.flush();
            } catch (Exception e) {
                System.out.println("Error sending an input to the server.");
//                e.printStackTrace();
            }
        }

        @Override
        public void undo(String id, OutputStreamWriter out) {
            action = new RequestMatchLeave();
            action.execute(id, out);
        }
    }

    private static class RequestMatchLeave implements ICommand{
        @Override
        public void execute(String id, OutputStreamWriter out) {
            packetMap.clear();
            packetMap.put("id", id);

            try {
                packet = new Packet(EPacketHeader.CLIENT_REQUEST_MATCH_LEAVE);
                packet.setBody(objectMapper.writeValueAsString(packetMap));
                out.write(packet.toString());
                out.flush();
            } catch (Exception e) {
                System.out.println("Error sending an input to the server.");
//                e.printStackTrace();
            }
        }

        @Override
        public void undo(String id, OutputStreamWriter out) {
            action = new RequestMatchJoin();
            action.execute(id, out);
        }
    }
}
