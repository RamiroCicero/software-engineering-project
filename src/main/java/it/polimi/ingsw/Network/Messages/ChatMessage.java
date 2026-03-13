package it.polimi.ingsw.Network.Messages;

import java.io.Serializable;

/**
 * The ChatMessage class represents a chat message in the game.
 */
public class ChatMessage extends Message implements Serializable {
    private final String message;
    private final String privateUser;
    private  String typeMessage ;

    /**
     * Constructs a ChatMessage object with the specified game ID, unique identifier, and message content.
     *
     * @param gameID      The ID of the game associated with the chat message.
     * @param UID         The unique identifier of the client.
     * @param message     The content of the chat message.
     * @param privateUser
     */
    public ChatMessage(int gameID, long UID, String message, String privateUser) {
        super(gameID,UID);
        this.message=message;
        this.typeMessage = "ChatMessage";
        this.privateUser=privateUser;
    }
    /**
     * Returns the type of the message.
     *
     * @return The type of the message.
     */
    public String typeMessage(){
        return "ChatMessage";
    }

    /**
     * Returns the type of the message.
     *
     * @return The type of the message.
     */
    public String getMessage() {
        return message;
    }

    public String getPrivateUser() {
        return privateUser;
    }
}
