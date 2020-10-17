package server.Snake.Enums;

public enum EPacketHeader {
    EMPTY,
    INT,
    STRING,
    FLOAT,
    ARRAY,
    ID,
    TERRAIN,
    PLAYER,
    MATCH,
    SERVER,
    CLIENT_PLAYER,
    CLIENT_MATCH_REQUEST,
    CLIENT_RESPONSE,
    ALL_PLAYERS,
    STATIC_ENTITY,
    MOVING_ENTITY
}
