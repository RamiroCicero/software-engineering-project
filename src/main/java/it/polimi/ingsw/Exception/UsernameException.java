package it.polimi.ingsw.Exception;

/**
 * exception thrown if username is not valid or already taken
 */
public class UsernameException extends GameException {

    public UsernameException(String message) {
        super(message);
    }
}
