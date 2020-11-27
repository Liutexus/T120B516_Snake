package server.Snake.Utility.Visitor;

import server.Snake.Entity.Entity;
import server.Snake.Entity.Generic.GenericMovingEntity;
import server.Snake.Entity.Generic.GenericStaticEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public interface IVisitor {
    static GenericStaticEntity mapToStaticEntity(Map<String, Object> map){
        GenericStaticEntity tempEntity = new GenericStaticEntity(0,0);
        return tempEntity;
    };
    static GenericMovingEntity mapToMovingEntity(Map<String, Object> map){
        GenericMovingEntity tempEntity = new GenericMovingEntity(0,0);
        return tempEntity;
    };
    static Entity mapToEntity(Map<String, Object> map){
        Entity tempEntity = new Entity(0,0);
        return tempEntity;
    };

}
