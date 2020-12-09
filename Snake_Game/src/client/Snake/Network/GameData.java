package client.Snake.Network;

import client.Snake.Renderer.Drawables.AllDrawables;
import client.Snake.Interface.IIterator;
import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.AbstractStaticEntity;
import server.Snake.Entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameData implements IIterator {
    private String id;
    private AllDrawables allDrawables = new AllDrawables();
    private Map<String, Player> snakes = new ConcurrentHashMap<>();
    private Map<String, AbstractStaticEntity> staticTerrainEntities;
    private Map<String, AbstractMovingEntity> movingTerrainEntities;
    private Map<Integer, ArrayList> terrain;

    public GameData(){
        this.staticTerrainEntities = new ConcurrentHashMap<>();
        this.movingTerrainEntities = new ConcurrentHashMap<>();
        this.terrain = new ConcurrentHashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AllDrawables getAllDrawables() {
        return allDrawables;
    }

    public void setAllDrawables(AllDrawables allDrawables) {
        this.allDrawables = allDrawables;
    }

    public Map<String, Player> getSnakes() {
        return snakes;
    }

    public void putSnake(String key, Player player){
        this.snakes.put(key, player);
    }

    public Map<String, AbstractStaticEntity> getStaticTerrainEntities() {
        return staticTerrainEntities;
    }

    public void putStaticTerrainEntity(String key, AbstractStaticEntity entity){
        this.staticTerrainEntities.put(key, entity);
    }

    public Map<String, AbstractMovingEntity> getMovingTerrainEntities() {
        return movingTerrainEntities;
    }

    public void putMovingTerrainEntity(String key, AbstractMovingEntity entity){
        this.movingTerrainEntities.put(key, entity);
    }

    public Map<Integer, ArrayList> getTerrain() {
        return terrain;
    }

    public void putTerrain(Integer key, ArrayList array){
        this.terrain.put(key, array);
    }

    @Override
    public Iterator createIterator() {
        return snakes.values().iterator();
    }
}
