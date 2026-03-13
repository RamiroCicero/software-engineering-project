package it.polimi.ingsw.Network.Messages;

import it.polimi.ingsw.model.Tile.ColourTile;

import java.io.Serializable;

/**
 * The BoardResponse class represents a response containing the game board information.
 */
public class BoardResponse extends Message implements Serializable {
    private ColourTile[][] board;
    private String typeMessage ;
    private int[] commonTokens;
    boolean endGameToken;

    /**
     * Constructs a BoardResponse object with the specified game board, game ID, unique identifier,
     * common tokens array, and end game token indicator.
     *
     * @param board          The game board represented as a 2D array of ColourTile objects.
     * @param gameID         The ID of the game.
     * @param UID            The unique identifier of the client.
     * @param commonTokens   An array containing the number of common tokens for each player.
     * @param endGameToken   A boolean indicating if the end game token has been taken.
     */
    public BoardResponse(ColourTile[][] board,int gameID,long UID, int[] commonTokens, boolean endGameToken){
        super(gameID,UID);
        this.board = board;
        this.typeMessage = "BoardResponse";
        this.commonTokens = commonTokens;
        this.endGameToken = endGameToken;
    }

    @Override
    public String typeMessage() {
        return "BoardResponse";
    }

    /**
     * Returns the game board represented as a 2D array of ColourTile objects.
     *
     * @return The game board.
     */
    public ColourTile[][] getBoard() {
        return board;
    }

    /**
     * Returns an array containing the number of common tokens for each player.
     *
     * @return The array of common tokens.
     */
    public int[] getCommonTokens() {
        return commonTokens;
    }

    /**
     * Checks if the end game token has been taken.
     *
     * @return true if the end game token has been taken, false otherwise.
     */

    public boolean isEndGameTokenTaken() {
        return endGameToken;
    }
}
