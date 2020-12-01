package client.Snake.Renderer.Command;

import client.Snake.Renderer.Interface.ICommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import server.Snake.Enumerator.EPacketHeader;
import server.Snake.Network.Packet.Packet;

import java.io.OutputStreamWriter;
import java.util.HashMap;

public class PlayerMoveCommand extends TemplateCommand{
    private static ObjectWriter objectMapper = new ObjectMapper().writer();
    private static HashMap<Object, Object> packetMap = new HashMap<>();
    private static Packet packet = new Packet(EPacketHeader.CLIENT_RESPONSE);
    private static ICommand action;

    public static void moveUp(String id, OutputStreamWriter out){
        action = new MoveUp();
        action.execute(id, out);
    }

    public static void moveDown(String id, OutputStreamWriter out){
        action = new MoveDown();
        action.execute(id, out);
    }

    public static void moveRight(String id, OutputStreamWriter out){
        action = new MoveRight();
        action.execute(id, out);
    }

    public static void moveLeft(String id, OutputStreamWriter out){
        action = new MoveLeft();
        action.execute(id, out);
    }

    public static void moveStop(String id, OutputStreamWriter out){
        action = new MoveStop();
        action.execute(id, out);
    }

    @Override
    public void undo(String id, OutputStreamWriter out){
        action.undo(id, out);
        //System.out.println(action.toString());
    }
    @Override
    public String getString(){
        return "current command: " + action.toString();

    }

    private static class MoveUp implements ICommand{
        @Override
        public void execute(String id, OutputStreamWriter out) {
            packetMap.clear();
            packetMap.put("id", id);
            packetMap.put("directionX", "0");
            packetMap.put("directionY", "-1");
            try {
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
            action = new MoveLeft();
            action.execute(id, out);
        }
    }

    private static class MoveDown implements ICommand {
        @Override
        public void execute(String id, OutputStreamWriter out) {
            packetMap.clear();
            packetMap.put("id", id);
            packetMap.put("directionX", "0");
            packetMap.put("directionY", "1");
            try {
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
            action = new MoveRight();
            action.execute(id, out);
        }

    }

    private static class MoveRight implements ICommand{
        @Override
        public void execute(String id, OutputStreamWriter out) {
            packetMap.clear();
            packetMap.put("id", id);
            packetMap.put("directionX", "1");
            packetMap.put("directionY", "0");
            try {
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
            action = new MoveUp();
            action.execute(id, out);
        }

    }

    private static class MoveLeft implements ICommand{
        @Override
        public void execute(String id, OutputStreamWriter out) {
            packetMap.clear();
            packetMap.put("id", id);
            packetMap.put("directionX", "-1");
            packetMap.put("directionY", "0");
            try {
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
            action = new MoveDown();
            action.execute(id, out);
        }

    }

    private static class MoveStop implements ICommand{
        @Override
        public void execute(String id, OutputStreamWriter out) {
            packetMap.clear();
            packetMap.put("id", id);
            packetMap.put("directionX", "0");
            packetMap.put("directionY", "0");
            try {
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
            action = new MoveUp();
            action.execute(id, out);
        }

    }

}
