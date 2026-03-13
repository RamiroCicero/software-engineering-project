package it.polimi.ingsw.Exception;

/**
 * Exception thrown when num of players is more than four
 */

public class MaxPlayerException extends GameException {
    public MaxPlayerException(String s) {
        super(s);
    }
}
