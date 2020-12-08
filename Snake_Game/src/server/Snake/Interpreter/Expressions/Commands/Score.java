package server.Snake.Interpreter.Expressions.Commands;

import server.Snake.Entity.Player;
import server.Snake.Interpreter.Expressions.Tokens.ActorGroupExpression;
import server.Snake.Interpreter.Expressions.TerminalExpression;
import server.Snake.Interpreter.Expressions.Tokens.EActorToken;
import server.Snake.MatchInstance;
import server.Snake.Network.Handler;

import java.util.Map;

public class Score extends TerminalExpression {
    private ActorGroupExpression left; // What entities to affect
    private int right; // How much to affect

    public Score(ActorGroupExpression exp1, int amount) {
        left = exp1;
        right = amount;
    }

    public Score() {
        left = null;
        right = 0;
    }

    @Override
    public Object execute(Object context) {
        MatchInstance matchInstance = ((Handler) context).getMatchInstance();
        Map<String, Player> players = matchInstance.getPlayers();
        try {
            if(this.left.execute(null) == EActorToken.PLAYERS)
                players.forEach((id, player) -> player.deltaScore(right));
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
