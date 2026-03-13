package it.polimi.ingsw.Network.Messages;

import java.io.Serializable;

/**
 * The RemoveResponse class represents a response to the remove message in the game.
 */
public class RemoveResponse extends Message implements Serializable {
    boolean invalidSequence;
    private String typeMessage ;

    /**
     * Constructs a RemoveResponse object with the specified game ID, unique identifier, and invalid sequence flag.
     *
     * @param gameID          The ID of the game associated with the response.
     * @param UID             The unique identifier of the client.
     * @param invalidSequence The flag indicating whether the remove sequence is invalid or not.
     */
    public RemoveResponse(int gameID,long UID, boolean invalidSequence) {
        super(gameID,UID);
        this.typeMessage = "RemoveResponse";
        this.invalidSequence = invalidSequence;
    }
    /**
     * Returns the type of the response message.
     *
     * @return The type of the response message.
     */
    public String typeMessage(){
        return "RemoveResponse";
    }

    /**
     * Checks if the remove sequence is invalid.
     *
     * @return True if the remove sequence is invalid, false otherwise.
     */
    public boolean isInvalidSequence() {
        return invalidSequence;
    }


}
