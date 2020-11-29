package server.Snake.Interface;

import server.Snake.Entity.Entity;
import server.Snake.Entity.Generic.GenericMovingEntity;
import server.Snake.Entity.Generic.GenericStaticEntity;
import server.Snake.Entity.Player;
import server.Snake.Entity.Snake;

import java.util.Map;

public interface IVisitor {
    GenericStaticEntity visit(GenericStaticEntity entity);
    GenericMovingEntity visit(GenericMovingEntity entity);
    Entity visit(Entity entity);
    Snake visit(Snake snake);
    Player visit(Player player);
}
