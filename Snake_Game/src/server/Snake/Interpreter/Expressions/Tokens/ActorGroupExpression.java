package server.Snake.Interpreter.Expressions.Tokens;

import server.Snake.Interpreter.Expressions.TerminalExpression;

public class ActorGroupExpression extends TerminalExpression {
    private EActorToken actorGroup;

    public ActorGroupExpression(String group){
        try{
            this.actorGroup = EActorToken.valueOf(group.toUpperCase());
        } catch (Exception e){
            this.actorGroup = null;
        }
    }

    @Override
    public Object execute(Object context) {
        return this.actorGroup;
    }
}
