package it.polimi.ingsw.Network.Messages;

import com.google.gson.Gson;

/**
 * The `PingMessage` class represents a "ping" message that extends the `Message` class.
 */
public class PingMessage extends Message {
    private final String typeMessage;

    /**
     * Creates a new `PingMessage` object with the specified game ID and UID.
     *
     * @param gameID The ID of the game.
     * @param UID The UID (User ID) associated with the client.
     */
    public PingMessage(int gameID, Long UID) {
        super(gameID, UID);
        this.typeMessage = "PingMessage";
    }

    /**
     * Converts the `PingMessage` object to its JSON representation.
     *
     * @return The JSON representation of the `PingMessage` object.
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    /**
     * Returns the type of the message as a string.
     *
     * @return The type of the message, which is "PingMessage".
     */
    public String typeMessage(){
        return "PingMessage";
    }
}
