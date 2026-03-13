package it.polimi.ingsw.Network.Messages;

import com.google.gson.Gson;

/**
 * The FirstResponse class represents the first response in the game.
 */

public class FirstResponse extends Message{
    private String typeMessage ;
    /**
     * Constructs a FirstResponse object with the specified game ID and unique identifier.
     *
     * @param gameID The ID of the game associated with the first response.
     * @param UID    The unique identifier of the client.
     */
    public FirstResponse(int gameID,long UID) {
        super(gameID,UID);
        this.typeMessage = "FirstResponse";
    }

    @Override
    public String typeMessage() {
        return "FirstResponse";
    }

    /**
     * Converts the FirstResponse object to a JSON string representation.
     *
     * @return The JSON representation of the FirstResponse object.
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
