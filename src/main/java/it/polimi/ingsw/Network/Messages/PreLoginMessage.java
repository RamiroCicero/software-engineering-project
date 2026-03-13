package it.polimi.ingsw.Network.Messages;

import com.google.gson.Gson;

/**
 * The PreLoginMessage class represents a pre-login message in the game.
 */
public class PreLoginMessage extends Message{

    String nickname;
    String typeMessage ;
    /**
     * Constructs a PreLoginMessage object with the specified game ID, unique identifier, and nickname.
     *
     * @param gameID   The ID of the game associated with the pre-login message.
     * @param UID      The unique identifier of the client.
     * @param nickname The nickname of the user sending the pre-login message.
     */
    public PreLoginMessage(int gameID, long UID, String nickname) {
        super(gameID,UID);
        this.nickname = nickname;
        this.typeMessage = "PreLoginMessage";
    }
    /**
     * Converts the PreLoginMessage object to a JSON string representation.
     *
     * @return The JSON representation of the PreLoginMessage object.
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    @Override
    public String typeMessage() {
        return "PreLoginMessage";
    }

    @Override
    public String getNickname() {
        return nickname;
    }
}
