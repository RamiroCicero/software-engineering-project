package it.polimi.ingsw.Network.Messages;

/**
 * The PreLoginResponse class represents a pre-login response in the game.
 */
public class PreLoginResponse extends Message{
    private String typeMessage ;
    /**
     * Constructs a PreLoginResponse object with the specified game ID and unique identifier.
     *
     * @param gameID The ID of the game associated with the pre-login response.
     * @param UID    The unique identifier of the client.
     */
    public PreLoginResponse(int gameID,long UID) {
        super(gameID,UID);
        this.typeMessage = "PreLoginResponse";
    }

    @Override
    public String typeMessage() {
        return "PreLoginResponse";
    }
}
