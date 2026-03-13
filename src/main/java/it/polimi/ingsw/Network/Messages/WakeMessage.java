package it.polimi.ingsw.Network.Messages;

import java.io.Serializable;

/**
 * The WakeMessage class represents a wake-up message in the game.
 */
public class WakeMessage extends Message implements Serializable {
    private String typeMessage ;
    /**
     * Constructs a WakeMessage object with the specified game ID and unique identifier.
     *
     * @param gameID The ID of the game associated with the wake-up message.
     * @param UID    The unique identifier of the client.
     */
    public WakeMessage(int gameID,long UID) {
        super(gameID,UID);
        this.typeMessage = "WakeMessage";
    }

    /**
     * Returns the type of the message.
     *
     * @return The type of the message.
     */
    public String typeMessage(){
        return "WakeMessage";
    }
}
