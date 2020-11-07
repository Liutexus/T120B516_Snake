package server.Snake.Enumerator;

public enum EPacketHeader {
    EMPTY,

    INT,
    STRING,
    FLOAT,
    ARRAY,

    ID,
    PLAYER,
    MATCH,
    ENTITY,
    TERRAIN,
    ALL_PLAYERS,
    STATIC_ENTITY,
    MOVING_ENTITY,

    CLIENT_PLAYER,
    CLIENT_REQUEST_MATCH_JOIN,
    CLIENT_REQUEST_MATCH_LEAVE,
    CLIENT_LOGIN,
    CLIENT_LOGOUT,
    CLIENT_RESPONSE,
}
