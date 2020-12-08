package server.Snake.Interpreter.Expressions.Commands;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.AbstractStaticEntity;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Player;
import server.Snake.Enumerator.EEffect;
import server.Snake.Interpreter.Expressions.Tokens.ActorGroupExpression;
import server.Snake.Interpreter.Expressions.TerminalExpression;
import server.Snake.Interpreter.Expressions.Tokens.EActorToken;
import server.Snake.MatchInstance;
import server.Snake.Network.Handler;

import java.util.Map;

public class Effect extends TerminalExpression {
    private ActorGroupExpression left; // What entities to affect
    private EEffect middle; // How much to affect
    private int right; // How much to affect

    public Effect(ActorGroupExpression exp1, EEffect effect, int amount) {
        left = exp1;
        middle = effect;
        right = amount;
    }

    public Effect() {
        left = null;
        middle = EEffect.NONE;
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
                    players.forEach((id, player) -> player.getSnake().setEffect(middle, right));
                    terrainEntities.forEach((id, entity) -> entity.setEffect(middle, right));
                    return true;
                case ME:
                    players.get(((Handler) context).getClientId()).getSnake().setEffect(middle, right);
                    return true;
                case PLAYERS:
                    players.forEach((id, player) -> player.getSnake().setEffect(middle, right));
                    return true;
                case NPC:
                    terrainEntities.forEach((id, entity) -> {
                        if(entity instanceof AbstractMovingEntity)
                            entity.setEffect(middle, right);
                    });
                    return true;
                case DROPS:
                    terrainEntities.forEach((id, entity) -> {
                        if(entity instanceof AbstractStaticEntity)
                            entity.setEffect(middle, right);
                    });
                    return true;
                default:
                    return false;
            }
        } catch (Exception e){
            return false;
        }
    }
}
