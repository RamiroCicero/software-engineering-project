package it.polimi.ingsw.Network.Messages;

/**
 * The UIDResponse class represents a response containing a unique identifier (UID) in the game.
 */
public class UIDResponse extends Message {
    private String typeMessage ;
    /**
     * Constructs a UIDResponse object with the specified unique identifier (UID).
     *
     * @param UID The unique identifier.
     */
    public UIDResponse(long UID) {
        super(-1,UID);
        this.typeMessage = "UIDResponse";
    }
}
