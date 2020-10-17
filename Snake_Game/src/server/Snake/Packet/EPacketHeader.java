package server.Snake.Packet;

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
    CLIENTPLAYER,
    CLIENTREQUEST,
    CLIENTRESPONSE,
    ALLPLAYERS,
    STATICENTITY,
    MOVINGENTITY
}
