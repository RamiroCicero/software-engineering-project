package it.polimi.ingsw.Exception;

/**
 * Exception thrown when a selected slot is not valid
 */
public class InvalidSlotException extends GameException {
    public InvalidSlotException(String message) {
        super(message);
    }
}
