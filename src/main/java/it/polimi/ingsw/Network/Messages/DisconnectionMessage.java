package it.polimi.ingsw.Network.Messages;

/**
 * The DisconnectionMessage class represents a disconnection message in the game.
 */
public class DisconnectionMessage extends Message {

    private final String typeMessage;

    private final boolean serverError;

    /**
     * Constructs a DisconnectionMessage object with the specified game ID and unique identifier.
     *
     * @param gameID The ID of the game associated with the disconnection message.
     * @param UID    The unique identifier of the client.
     * @param serverError
     */
    public DisconnectionMessage(int gameID, Long UID, boolean serverError) {
        super(gameID, UID);
        this.serverError = serverError;
        this.typeMessage = "DisconnectionMessage";
    }

    /**
     * Returns the type of the message.
     *
     * @return The type of the message.
     */
    public String typeMessage(){
        return "DisconnectionMessage";
    }

    public boolean isServerError() {
        return serverError;
    }
}
