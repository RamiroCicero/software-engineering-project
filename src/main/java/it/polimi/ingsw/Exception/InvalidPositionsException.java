package it.polimi.ingsw.Exception;

/**
 * Exception thrown when position selected is not valid
 */
public class InvalidPositionsException extends GameException {
    public InvalidPositionsException(String message) {
        super(message);
    }
}
