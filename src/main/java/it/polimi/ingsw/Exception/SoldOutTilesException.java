package it.polimi.ingsw.Exception;

/**
 * Exception thrown when there are no more tiles in TileDeck
 */

public class SoldOutTilesException extends GameException {
    public SoldOutTilesException(String message) {
        super(message);
    }
}
