package server.Snake.Interpreter.Expressions.Tokens;

public enum EActorToken {
    ALL,        // All entities in the game
    ME,         // Only the command sender
    PLAYERS,    // All match players
    NPC,        // All NPC in the match
    DROPS,      // All unmovable entities in the match
}
