package it.polimi.ingsw.model.Board;

import java.io.Serializable;

/**
 * Token given to the player who first finishes filling the library
 */

public class EndGameToken implements Serializable {

    private boolean taken;



    /**
     * Constructs an EndGameToken with the initial state of not being taken.
     */
    public EndGameToken() {
        this.taken = false;
    }


    /**
     * Sets the state of the end game token to "taken".
     */
    public void setTaken() {
        this.taken=true;

    }
    /**
     * Checks if the end game token has been taken.
     *
     * @return true if the token has been taken, false otherwise.
     */
    public boolean isTaken() {
        return taken;
    }
}
