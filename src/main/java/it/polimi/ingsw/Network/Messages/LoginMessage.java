package it.polimi.ingsw.Network.Messages;

import java.io.Serializable;

/**
 * The LoginMessage class represents a login message in the game.
 */
public class LoginMessage extends Message implements Serializable {

    private String nickname;
    private String protocol;
    private String typeMessage ;

    /**
     * Constructs a LoginMessage object with the specified nickname, game ID, and unique identifier.
     *
     * @param nickname The nickname of the user logging in.
     * @param gameId   The ID of the game associated with the login.
     * @param UID      The unique identifier of the client.
     */
    public LoginMessage(String nickname, int gameId, long UID) {
        super(gameId,UID);
        this.nickname = nickname;
        this.typeMessage = "LoginMessage";
    }

    /**
     * Returns the nickname of the user.
     *
     * @return The nickname of the user.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the protocol associated with the login message.
     *
     * @return The protocol associated with the login message.
     */
    public String getProtocol() {
        return protocol;
    }


    /**
     * Returns the type of the login message.
     *
     * @return The type of the login message.
     */
    public String typeMessage(){
        return "LoginMessage";
    }
}
