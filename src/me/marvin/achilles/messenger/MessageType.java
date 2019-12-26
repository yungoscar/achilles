package me.marvin.achilles.messenger;

public enum MessageType {
    MESSAGE,
    KICK_REQUEST,
    DATA_UPDATE;

    public static MessageType fromId(int id) {
        for (MessageType type : values()) {
            if (type.ordinal() == id) return type;
        }
        return null;
    }
}
