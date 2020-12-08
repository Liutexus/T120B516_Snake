package server.Snake.Interpreter.Expressions.Commands;

import server.Snake.Interpreter.Expressions.Tokens.NullExpression;
import server.Snake.Interpreter.Expressions.TerminalExpression;

public class Plus extends TerminalExpression {

    private TerminalExpression left, right;

    public Plus(TerminalExpression exp1, TerminalExpression exp2) {
        left = exp1;
        right = exp2;
    }

    public Plus() {
        left = new NullExpression();
        right = new NullExpression();
    }

    @Override
    public Object execute(Object context) {
        return (float)left.execute(context) * (float)right.execute(context);
    }

}
