package it.polimi.ingsw.Network.Messages;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The InitResponse class represents the initial response in the game.
 */
public class InitResponse extends Message implements Serializable {

    private final ArrayList<String> players;
    private String typeMessage ;

    /**
     * Constructs an InitResponse object with the specified game ID and unique identifier.
     *
     * @param gameID The ID of the game associated with the initial response.
     * @param UID    The unique identifier of the client.
     */
    public InitResponse(int gameID, long UID, ArrayList<String> players) {
        super(gameID,UID);
        this.typeMessage = "InitResponse";
        this.players=players;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    @Override
    public String typeMessage() {
        return "InitResponse";
    }


}
