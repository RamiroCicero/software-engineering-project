package it.polimi.ingsw.Network.Messages;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Message that contains information about the board in a turn
 */
public class BoardMessage extends Message implements Serializable {
    private final String nickname;
    private String typeMessage ;

    /**
     * Constructor of the class
     * @param nickname the nick of the sender
     * @param gameID the gameID of the game the client is in
     * @param UID the unique identifier of the client
     */
    public BoardMessage(String nickname, int gameID,Long UID) {
        super(gameID,UID);
        this.nickname = nickname;
        this.typeMessage = "BoardMessage";
    }

    @Override
    public String typeMessage() {
        return "BoardMessage";
    }

    @Override
    public String getNickname() {
        return nickname;
    }
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
