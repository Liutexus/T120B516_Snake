package server.Snake.Packet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.ObjectBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Packet {
    public EPacketHeader header;
    private String body;
    private HashMap<String, Object> map;

    public Packet(){
        this.header = EPacketHeader.EMPTY;
        this.body = "";
    }

    public Packet(EPacketHeader header){
        this.header = header;
        this.body = "";
    }

    public Packet(EPacketHeader header, String body){
        this.header = header;
        this.body = body;
    }

    public Packet(String json){
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> map;
        try {
            map = objectMapper.readValue(json, new TypeReference<>(){});
            for (EPacketHeader item: EPacketHeader.values())
                if(item.toString().compareTo((String) map.keySet().toArray()[0]) == 0){
                    this.header = item;
                    break;
                }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if(map.get(this.header.toString()).getClass() == String.class)
            this.body = String.valueOf(map.get(this.header.toString()));
        else
            this.body = json.substring(this.header.toString().length() + 4, json.length() - 1); // 4 is there for additional characters "{,"" and :"
        map.replace(this.header.toString(), this.body);
        this.map = map;
    }

    public void setBody(String body){
        if(!body.contains(":")) {
            if(!body.endsWith("\""))
                this.body += '"';
            if(!body.startsWith("\""))
                this.body = '"' + this.body;
        }
        this.body = body;
    }

    public String getBody(){
        if(this.body.length() == 0) return null;
        return this.body;
    }

    public Map parseBody(){
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> map = new HashMap<>();
        try {
            map = objectMapper.readValue((String)this.map.get(this.header.toString()), new TypeReference<>(){});
        } catch (Exception e) {
            return this.map;
        }
        return map;
    }

    @Override
    public String toString(){
        String packet = "{\"" + header.toString() + "\":"; // add packet opening parentheses and its header

        if(!body.contains(":")) { // if body is only a simple string
            if(!body.endsWith("\""))
                this.body += '"';
            if(!body.startsWith("\""))
                this.body = '"' + this.body;
        }

        if(!body.startsWith("{") && body.contains(":")) packet += "{"; // does body have object opening parentheses

        packet += body;

        if(!body.endsWith("}") && body.contains(":")) packet += "}"; // does body have object closing parentheses

        packet += "}"; // add packet closing parentheses
        if(packet.length() < 8) packet = String.format("%" + -8 + "s", packet); // Making the packet big enough
        if(!packet.endsWith("\n")) packet += "\n"; // adding new line so the stream can know where the packet ends
        return packet;
    }

}
