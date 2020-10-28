package server.Snake.Enumerator;

public enum EPacketHeader {
    EMPTY,
    INT,
    STRING,
    FLOAT,
    ARRAY,
    ID,
    TERRAIN,
    ENTITY,
    PLAYER,
    MATCH,
    SERVER,
    CLIENT_PLAYER,
    CLIENT_MATCH_REQUEST,
    CLIENT_RESPONSE,
    ALL_PLAYERS,
    STATIC_ENTITY,
    MOVING_ENTITY,
}
