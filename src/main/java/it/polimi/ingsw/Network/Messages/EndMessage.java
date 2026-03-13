package it.polimi.ingsw.Network.Messages;

import java.io.Serializable;

/**
 * The EndMessage class represents a message indicating the end of a game.
 */
public class EndMessage extends Message implements Serializable {

    private final int points;
    private String winner;
    private  String typeMessage ;

    /**
     * Constructs an EndMessage object with the specified winner, game ID, and unique identifier.
     *
     * @param winner The nickname of the winner of the game.
     * @param gameID The ID of the game that has ended.
     * @param UID    The unique identifier of the client.
     */
    public EndMessage(String winner,int gameID,long UID,int points) {
        super(gameID,UID);
        this.winner = winner;
        this.typeMessage = "EndMessage";
        this.points=points;
    }

    /**
     * Returns the nickname of the winner of the game.
     *
     * @return The nickname of the winner.
     */

    public String getWinner() {
        return winner;
    }

    @Override
    public String typeMessage() {
        return "EndMessage";
    }

    /**
     *returns user points
     */
    public int getPoints() {
        return points;
    }
}
