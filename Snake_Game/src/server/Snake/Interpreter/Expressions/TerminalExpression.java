package server.Snake.Interpreter.Expressions;

import server.Snake.Interface.IExpression;
import server.Snake.Interpreter.Expressions.Tokens.EActorToken;
import server.Snake.Interpreter.Expressions.Tokens.ECommandToken;
import server.Snake.Utility.Utils;

public abstract class TerminalExpression implements IExpression {

    public abstract Object execute(Object context);

    public static boolean isCommand(String token){
        for (ECommandToken value : ECommandToken.values()) {
            if(value.toString().toLowerCase() == token.toLowerCase()) return true;
        }
        return false;
    }

    public static boolean isActor(String token){
        for (EActorToken value : EActorToken.values()) {
            if(value.toString().toLowerCase() == token.toLowerCase()) return true;
        }
        return false;
    }

    public static boolean isNumber(String token){
        return Utils.isNumeric(token);
    }

    public static boolean isOperator(String token){
        String operators = "\\+-\\*/%()";
        if (operators.contains(token)) {
            return true;
        }
        return false;
    }

}
