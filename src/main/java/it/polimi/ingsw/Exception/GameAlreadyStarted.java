package it.polimi.ingsw.Exception;

/**
 * exception thrown if game is already started
 */
public class GameAlreadyStarted extends GameException {

    public GameAlreadyStarted(String message) {
        super(message);
    }
}
