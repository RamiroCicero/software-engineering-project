package it.polimi.ingsw.model.Board;

import java.io.Serializable;

/**
 * Represents a board mask that stores information about available tiles for different numbers of players.
 * The mask is used to determine which tiles are available on the board based on the number of players.
 */
public class BoardMask implements Serializable {
    private final boolean[][] twoPlayersTiles;
    private final boolean[][] threePlayersTiles;
    private final boolean[][] fourPlayersTiles;

    /**
     * Constructs a BoardMask with the specified tile configurations for different player counts.
     *
     * @param twoPlayersTiles   The tile configuration for two players.
     * @param threePlayersTiles The tile configuration for three players.
     * @param fourPlayersTiles  The tile configuration for four players.
     */
    public BoardMask(boolean[][] twoPlayersTiles, boolean[][] threePlayersTiles, boolean[][] fourPlayersTiles) {
        this.twoPlayersTiles = twoPlayersTiles;
        this.threePlayersTiles = threePlayersTiles;
        this.fourPlayersTiles = fourPlayersTiles;
    }

    /**
     * Retrieves the tile configuration for the specified number of players.
     *
     * @param numOfPlayers The number of players.
     * @return The tile configuration for the specified number of players.
     */
    public boolean[][] getTiles(int numOfPlayers) {

        if (numOfPlayers == 2) {
            return twoPlayersTiles;
        }
        if (numOfPlayers == 3) {
            return threePlayersTiles;
        }

        return fourPlayersTiles;

    }

}
