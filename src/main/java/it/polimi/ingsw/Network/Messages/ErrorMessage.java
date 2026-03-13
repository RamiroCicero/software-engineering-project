package it.polimi.ingsw.Network.Messages;

import java.io.Serializable;

/**
 * The ErrorMessage class represents an error message.
 */
public class ErrorMessage extends Message implements Serializable {
    private  String typeMessage ;

    /**
     * Constructs an ErrorMessage object with the specified error message, game ID, and unique identifier.
     *
     * @param message The error message.
     * @param gameID  The ID of the game associated with the error.
     * @param UID     The unique identifier of the client.
     */
    public ErrorMessage(String message,int gameID,long UID) {
        super(gameID,UID);
        this.typeMessage = "ErrorMessage";
    }
    /**
     * Returns the type of the error message.
     *
     * @return The type of the error message.
     */
    public String typeMessage(){
        return "ErrorMessage";
    }




}
