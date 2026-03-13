package it.polimi.ingsw.Network.Messages;

import it.polimi.ingsw.model.Tile.ColourTile;

import java.io.Serializable;

/**
 * The TurnResponse class represents a response to a player's turn in the game.
 */
public class TurnResponse extends Message implements Serializable {

    private int status;
    private String typeMessage ;
    ColourTile[][] shelfColours ;
    int column;

    /**
     * Constructs a TurnResponse object with the specified status, game ID, unique identifier, shelf colours, and column.
     *
     * @param status        The status of the turn response.
     * @param gameID        The ID of the game associated with the response.
     * @param UID           The unique identifier of the client.
     * @param shelfColours  The colours of the shelf associated with the response.
     * @param column        The column chosen by the player for their turn.
     */
    public TurnResponse(int status,int gameID,long UID,ColourTile[][] shelfColours, int column) {
        super(gameID,UID);
        this.status = status;
        this.typeMessage = "TurnResponse";
        this.shelfColours = shelfColours;
        this.column = column;
    }

    /**
     * Returns the type of the response message.
     *
     * @return The type of the response message.
     */
    public String typeMessage(){
        return "TurnResponse";
    }

    @Override
    public int getColumn() {
        return column;
    }

    /**
     * Returns the status of the turn response.
     *
     * @return The status of the turn response.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Returns the colours of the shelf associated with the response.
     *
     * @return The colours of the shelf.
     */
    public ColourTile[][] getShelf() {
        return shelfColours;
    }
}
