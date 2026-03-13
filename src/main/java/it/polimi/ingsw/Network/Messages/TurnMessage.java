package it.polimi.ingsw.Network.Messages;

import java.io.Serializable;

/**
 * The TurnMessage class represents a message for a player's turn in the game.
 */
public class TurnMessage extends Message implements Serializable {
    private int column;
    private String nickname;
    private String[] colours;
    private String typeMessage ;

    /**
     * Constructs a TurnMessage object with the specified game ID, unique identifier, column, nickname, and colours.
     *
     * @param gameID    The ID of the game associated with the message.
     * @param UID       The unique identifier of the client.
     * @param column    The column chosen by the player for their turn.
     * @param nickname  The nickname of the player taking the turn.
     * @param colours   The array of colours associated with the turn.
     */
    public TurnMessage(int gameID,long UID, int column, String nickname, String[] colours) {
        super(gameID,UID);
        this.column = column;
        this.nickname = nickname;
        this.colours = colours;
        this.typeMessage = "TurnMessage";
    }

    /**
     * Returns the column chosen by the player for their turn.
     *
     * @return The column chosen by the player.
     */
    public int getColumn() {
        return column;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the array of colours associated with the turn.
     *
     * @return The array of colours.
     */
    public String[] getColours() {
        return colours;
    }

    @Override
    public String typeMessage() {
        return "TurnMessage";
    }
}
