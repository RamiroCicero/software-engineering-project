package it.polimi.ingsw.Exception;

/**
 * Exception thrown if there is not enough space for the selected numbers of tiles
 */
public class NoSpaceInColumnException extends GameException {
    public NoSpaceInColumnException(String message) {
        super(message);
    }
}
