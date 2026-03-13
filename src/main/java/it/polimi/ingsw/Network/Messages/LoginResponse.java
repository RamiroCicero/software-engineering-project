package it.polimi.ingsw.Network.Messages;

import java.io.Serializable;

/**
 * The LoginResponse class represents a login response in the game.
 */
public class LoginResponse extends Message implements Serializable {

    private String typeMessage ;
    /**
     * Constructs a LoginResponse object with the specified game ID and unique identifier.
     *
     * @param gameID The ID of the game associated with the login response.
     * @param UID    The unique identifier of the client.
     */
    public LoginResponse(int gameID,long UID) {
        super(gameID,UID);
        this.typeMessage = "LoginResponse";
    }

    /**
     * Returns the type of the login response message.
     *
     * @return The type of the login response message.
     */
    public String typeMessage(){
        return "LoginResponse";
    }

}
