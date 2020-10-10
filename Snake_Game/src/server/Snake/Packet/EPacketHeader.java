package server.Snake.Packet;

public enum EPacketHeader {
    EMPTY,
    INT,
    STRING,
    FLOAT,
    ARRAY,
    ID,
    MAP,
    PLAYER,
    CLIENTPLAYER,
    CLIENTRESPONSE,
    ALLPLAYERS,
    STATICENTITY,
    MOVINGENTITY
}
