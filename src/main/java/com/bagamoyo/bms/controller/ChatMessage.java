package com.bagamoyo.bms.controller;

import java.io.Serializable;

/**
 * Serializable chat message for client-server communication.
 * Used by ChatServerController and ChatClientController.
 */
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1112122200L;

    // Message types
    public static final int WHOISIN = 0;
    public static final int MESSAGE = 1;
    public static final int LOGOUT = 2;

    private final int type;
    private final String message;

    /**
     * Create a new chat message.
     *
     * @param type    the message type (WHOISIN, MESSAGE, or LOGOUT)
     * @param message the message content
     */
    public ChatMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    /**
     * Get the message type.
     *
     * @return the type constant
     */
    public int getType() {
        return type;
    }

    /**
     * Get the message content.
     *
     * @return the message text
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ChatMessage{type=" + type + ", message='" + message + "'}";
    }
}
