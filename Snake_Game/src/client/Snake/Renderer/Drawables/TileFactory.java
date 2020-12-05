package client.Snake.Renderer.Drawables;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TileFactory {
    private static Map<String, Tile> tileCollection = new ConcurrentHashMap<String, Tile>();

    public static Tile getTile(String name, Color color, String texture){
        if(tileCollection.containsKey(name)){
            return tileCollection.get(name);
        } else {
            Tile tile = new Tile(name, color, texture);
            tileCollection.put(name, tile);
            return tile;
        }
    }
}
