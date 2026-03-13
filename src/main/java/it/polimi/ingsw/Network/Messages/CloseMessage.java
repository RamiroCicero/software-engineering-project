package it.polimi.ingsw.Network.Messages;

import java.io.Serializable;

/**
 * The CloseMessage class represents a message indicating the closure of a game.
 */
public class CloseMessage extends Message implements Serializable {
    private  String typeMessage ;
    /**
     * Constructs a CloseMessage object with the specified game ID and unique identifier.
     *
     * @param gameID The ID of the game being closed.
     * @param UID    The unique identifier of the message.
     */
    public CloseMessage(int gameID,long UID) {
        super(gameID,UID);
        this.typeMessage = "CloseMessage";
    }
    @Override
    public String typeMessage() {
        return "CloseMessage";
    }

}
