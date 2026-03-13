package it.polimi.ingsw.Network.Messages;

/**
 * The ReFirstResponse class represents a response to the first response in the game.
 */
public class ReFirstResponse extends Message {
    private String typeMessage ;

    /**
     * Constructs a ReFirstResponse object with the specified game ID and unique identifier.
     *
     * @param gameID The ID of the game associated with the response.
     * @param UID    The unique identifier of the client.
     */
    public ReFirstResponse(int gameID, Long UID) {
        super(gameID, UID);
        this.typeMessage = "ReFirstResponse";
    }

    @Override
    public String typeMessage() {
        return "ReFirstResponse";
    }

}
