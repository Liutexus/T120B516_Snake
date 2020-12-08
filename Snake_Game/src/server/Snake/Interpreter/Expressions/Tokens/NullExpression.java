package server.Snake.Interpreter.Expressions.Tokens;

import server.Snake.Interpreter.Expressions.TerminalExpression;

public class NullExpression extends TerminalExpression {

    public NullExpression(){}

    @Override
    public Object execute(Object context) {
        return 0;
    }

}
