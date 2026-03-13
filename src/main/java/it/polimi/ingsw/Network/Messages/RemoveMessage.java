package it.polimi.ingsw.Network.Messages;

import it.polimi.ingsw.Utils.Coordinates;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The RemoveMessage class represents a message for removing positions in the game.
 */
public class RemoveMessage extends Message implements Serializable {

    private ArrayList<Coordinates> positions;
    private String nickname;
    private String typeMessage ;

    /**
     * Constructs a RemoveMessage object with the specified positions, game ID, unique identifier, and nickname.
     *
     * @param positions The list of coordinates representing the positions to be removed.
     * @param gameID    The ID of the game associated with the message.
     * @param UID       The unique identifier of the client.
     * @param nickname  The nickname of the user sending the remove message.
     */
    public RemoveMessage(ArrayList<Coordinates> positions, int gameID, long UID, String nickname) {
        super(gameID,UID);
        this.positions = positions;
        this.nickname = nickname;
        this.typeMessage = "RemoveMessage";
    }
    @Override
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the list of coordinates representing the positions to be removed.
     *
     * @return The list of coordinates representing the positions to be removed.
     */
    public ArrayList<Coordinates> getPositions() {
        return positions;
    }

    /**
     * Returns the type of the message.
     *
     * @return The type of the message.
     */
    public String typeMessage(){
        return "RemoveMessage";
    }
}
