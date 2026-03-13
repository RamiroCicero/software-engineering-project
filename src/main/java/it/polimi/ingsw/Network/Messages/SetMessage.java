package it.polimi.ingsw.Network.Messages;

import java.io.Serializable;

/**
 * The SetMessage class represents a message for setting the maximum number of players in the game.
 */
public class SetMessage extends Message implements Serializable {

    private int maxPlayers;
    private String typeMessage ;

    /**
     * Constructs a SetMessage object with the specified maximum number of players, game ID, and unique identifier.
     *
     * @param maxPlayers The maximum number of players to be set.
     * @param gameID     The ID of the game associated with the message.
     * @param UID        The unique identifier of the client.
     */
    public SetMessage(int maxPlayers, int gameID, Long UID) {
        super(gameID,UID);
        this.maxPlayers = maxPlayers;
        this.typeMessage = "SetMessage";
    }


    /**
     * Returns the maximum number of players to be set.
     *
     * @return The maximum number of players to be set.
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public String typeMessage() {
        return "SetMessage";
    }
}
