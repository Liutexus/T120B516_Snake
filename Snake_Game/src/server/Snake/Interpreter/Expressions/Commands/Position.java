package server.Snake.Interpreter.Expressions.Commands;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.AbstractStaticEntity;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Player;
import server.Snake.Interpreter.Expressions.TerminalExpression;
import server.Snake.Interpreter.Expressions.Tokens.ActorGroupExpression;
import server.Snake.Interpreter.Expressions.Tokens.EActorToken;
import server.Snake.MatchInstance;
import server.Snake.Network.Handler;

import java.util.Map;

public class Position extends TerminalExpression {
    private ActorGroupExpression left; // What entities to affect
    private int middle; // How to effect
    private int right; // How to effect

    public Position(ActorGroupExpression exp1, int posX, int posY) {
        left = exp1;
        middle = posX;
        right = posY;
    }

    public Position() {
        left = null;
        middle = 0;
        right = 0;
    }

    @Override
    public Object execute(Object context) {
        MatchInstance matchInstance = ((Handler) context).getMatchInstance();
        Map<String, Player> players = matchInstance.getPlayers();
        Map<Integer, Entity> terrainEntities = matchInstance.getTerrainEntities();
        try {
            switch ((EActorToken)this.left.execute(context)){
                case ALL:
                    players.values().forEach(player -> {
                        player.getSnake().setPosition(middle, right);
                    });
                    terrainEntities.values().forEach((entity -> {
                        entity.setPosition(middle, right);
                    }));
                    return true;
                case ME:
                    players.get(((Handler) context).getClientId()).getSnake().setPosition(middle, right);
                    return true;
                case PLAYERS:
                    players.forEach((id, player) -> player.getSnake().setPosition(middle, right));
                    return true;
                case NPC:
                    terrainEntities.forEach((id, entity) -> {
                        if(entity instanceof AbstractMovingEntity)
                            entity.setPosition(middle, right);
                    });
                    return true;
                case DROPS:
                    terrainEntities.forEach((id, entity) -> {
                        if(entity instanceof AbstractStaticEntity)
                            entity.setPosition(middle, right);
                    });
                    return true;
                default:
                    return false;
            }
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
