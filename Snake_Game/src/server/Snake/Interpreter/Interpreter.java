package server.Snake.Interpreter;

import server.Snake.Enumerator.EEffect;
import server.Snake.Enumerator.EPacketHeader;
import server.Snake.Interpreter.Expressions.Commands.*;
import server.Snake.Interpreter.Expressions.TerminalExpression;
import server.Snake.Interpreter.Expressions.Tokens.ActorGroupExpression;
import server.Snake.Interpreter.Expressions.Tokens.ECommandToken;
import server.Snake.Interpreter.Expressions.Tokens.Expression;
import server.Snake.Network.Handler;


public class Interpreter {

    public static void execute(String commandLine, Handler handler){
        String[] tokens = commandLine.split(" ");

        TerminalExpression exp;
        try{
            if(tokens.length > 2){
                ECommandToken command = ECommandToken.valueOf(tokens[0].toUpperCase());
                ActorGroupExpression actorExp = new ActorGroupExpression(tokens[1]);
                Expression parameter = new Expression(tokens[2]);
                Expression additionalParameter;

                switch (command){
                    case TAIL:
                        exp = new Tail(actorExp, (int)((float)parameter.execute(handler)));
                        exp.execute(handler);
                        break;
                    case SCORE:
                        exp = new Score(actorExp, (int)((float)parameter.execute(handler)));
                        exp.execute(handler);
                        break;
                    case EFFECT:
                        additionalParameter = new Expression(tokens[3]);
                        exp = new Effect(actorExp, (EEffect)parameter.execute(handler), (int)((float)additionalParameter.execute(handler)));
                        exp.execute(handler);
                        break;
                    case POSITION:
                        additionalParameter = new Expression(tokens[3]);
                        exp = new Position(actorExp, (int)((float)parameter.execute(handler)), (int)((float)additionalParameter.execute(handler)));
                        exp.execute(handler);
                        break;
                    case VELOCITY:
                        additionalParameter = new Expression(tokens[3]);
                        exp = new Velocity(actorExp, (int)((float)parameter.execute(handler)), (int)((float)additionalParameter.execute(handler)));
                        exp.execute(handler);
                        break;
                    default:
                        handler.sendPacket(EPacketHeader.COMMAND, "Incorrect command.");
                }
            }
            else {
                handler.sendPacket(EPacketHeader.COMMAND, "Incorrect command. Must consist of at least 3 keywords.");
            }
        } catch (Exception e){
            handler.sendPacket(EPacketHeader.COMMAND, "Command '" + tokens[0] + "' does not exist.");
//            e.printStackTrace();
        }
    }
}
