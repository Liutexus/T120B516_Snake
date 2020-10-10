package server.Snake.Packet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Packet {
    public EPacketHeader header;
    private String body;

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
            this.body = (String) map.get(this.header.toString());
        else
            this.body = json.substring(this.header.toString().length() + 4, json.length() - 1); // 4 is there for additional characters "{,"" and :"
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
        HashMap<String, Object> map;
        try {
            map = objectMapper.readValue(this.body, new TypeReference<>(){});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return map;
    }

    @Override
    public String toString(){
        String packet = "{\"" + header.toString() + "\":";

        if(!body.contains(":")) {
            if(!body.endsWith("\""))
                this.body += '"';
            if(!body.startsWith("\""))
                this.body = '"' + this.body;
        }

        if(!body.startsWith("{") && body.contains(":")) packet += "{";
        packet += body;
        if(!body.endsWith("}") && body.contains(":")) packet += "}";

        packet += "}";
        if(packet.length() < 8) packet = String.format("%" + -8 + "s", packet); // Making the packet big enough
        if(!packet.endsWith("\n")) packet += "\n";
        return packet;
    }

}
