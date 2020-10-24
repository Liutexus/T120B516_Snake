package client.Snake.Renderer.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import server.Snake.Enums.EPacketHeader;
import server.Snake.Packet.Packet;

import java.io.OutputStreamWriter;
import java.util.HashMap;

public class PlayerMoveCommand {
    private static ObjectWriter objectMapper = new ObjectMapper().writer();
    private static HashMap<Object, Object> packetMap = new HashMap<>();
    private static Packet packet = new Packet(EPacketHeader.CLIENT_RESPONSE);
    private static ICommand moveAction;

    public static void moveUp(String id, OutputStreamWriter out){
        moveAction = new MoveUp();
        moveAction.execute(id, out);
    }

    public static void moveDown(String id, OutputStreamWriter out){
        moveAction = new MoveDown();
        moveAction.execute(id, out);
    }

    public static void moveRight(String id, OutputStreamWriter out){
        moveAction = new MoveRight();
        moveAction.execute(id, out);
    }

    public static void moveLeft(String id, OutputStreamWriter out){
        moveAction = new MoveLeft();
        moveAction.execute(id, out);
    }

    public static void moveStop(String id, OutputStreamWriter out){
        moveAction = new MoveStop();
        moveAction.execute(id, out);
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
            moveAction = new MoveDown();
            moveAction.execute(id, out);
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
            moveAction = new MoveUp();
            moveAction.execute(id, out);
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
            moveAction = new MoveLeft();
            moveAction.execute(id, out);
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
            moveAction = new MoveRight();
            moveAction.execute(id, out);
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
            moveAction = new MoveUp();
            moveAction.execute(id, out);
        }
    }
}
