package server.Snake.Interpreter.Expressions.Tokens;

import server.Snake.Enumerator.EEffect;
import server.Snake.Interpreter.Expressions.TerminalExpression;
import server.Snake.Utility.Utils;

public class Expression extends TerminalExpression {
    private Object value;

    public Expression(String value){
        this.value = null;
        if(TerminalExpression.isActor(value))
            this.value = new ActorGroupExpression(value);
        if(Utils.isNumeric(value))
            this.value = Utils.parseToFloat(value);
        if(TerminalExpression.isOperator(value))
            this.value = value;
        try{
            this.value = EEffect.valueOf(value.toUpperCase());
        } catch (Exception e){}

    }

    @Override
    public Object execute(Object context) {
        return this.value;
    }
}
