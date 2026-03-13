package it.polimi.ingsw.Exception;

/**
 * Exception thrown when a selected slot is empty
 */
public class EmptySlotException extends GameException {
    public EmptySlotException(String message) {
        super(message);
    }
}
