package it.polimi.ingsw.Network.Messages;

import java.io.Serializable;

/**
 * The SetResponse class represents a response to the set message in the game.
 */
public class SetResponse extends Message implements Serializable {
    /**
     * Constructs a SetResponse object with the specified game ID and unique identifier.
     *
     * @param gameID The ID of the game associated with the response.
     * @param UID    The unique identifier of the client.
     */
    public SetResponse(int gameID,long UID) {
        super(gameID,UID);
        this.typeMessage = "SetResponse";
    }

    /**
     * Returns the type of the response message.
     *
     * @return The type of the response message.
     */
    public String typeMessage(){
        return "SetResponse";
    }
    private String typeMessage ;

}
