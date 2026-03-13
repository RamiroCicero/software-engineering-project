package it.polimi.ingsw.Network.Messages;

import com.google.gson.Gson;
import it.polimi.ingsw.Utils.Coordinates;
import it.polimi.ingsw.model.CommonCards.CardCommonTarget;
import it.polimi.ingsw.model.PersonalCards.CardPersonalTarget;
import it.polimi.ingsw.model.Tile.ColourTile;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Superclass of all the messages exchanged  between the client and the server
 */
public class Message implements Serializable {

    private final Long UID;
    private final int gameID;
    /**
     * Constructs a Message object with the specified game ID and unique identifier.
     *
     * @param gameID The ID of the game associated with the message.
     * @param UID    The unique identifier of the client.
     */
    public Message(int gameID,Long UID) {
        this.gameID=gameID;
        this.UID=UID;
    }
    /**
     * Converts the Message object to a JSON string representation.
     *
     * @return The JSON representation of the Message object.
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }



    /**
     * Returns the maximum number of players.
     *
     * @return The maximum number of players. Returns -1 if not applicable.
     */
    public int getMaxPlayers() {
        return -1;
    }


    /**
     * Returns the winner of the game.
     *
     * @return The winner of the game. Returns an empty string if not applicable.
     */
    public String getWinner() {
        return "";
    }


    /**
     * Checks if the message is an initialization message.
     *
     * @return True if the message is an initialization message, false otherwise.
     */
    public boolean isInit() {
        return false;
    }


    /**
     * Returns the column value.
     *
     * @return The column value. Returns -1 if not applicable.
     */
    public int getColumn() {
        return -1;
    }


    /**
     * Checks if the message represents a username error.
     *
     * @return True if the message represents a username error, false otherwise.
     */
    public boolean isUsernameError() {
        return false;
    }


    /**
     * Returns the game ID.
     *
     * @return The game ID.
     */
    public int getGameID() {
        return gameID;
    }


    /**
     * Checks if the message is the first message.
     *
     * @return True if the message is the first message, false otherwise.
     */
    public boolean isFirst() {
        return false;
    }


    /**
     * Returns the nickname.
     *
     * @return The nickname. Returns null if not applicable.
     */
    public String getNickname() {
        return null;
    }


    /**
     * Returns the protocol.
     *
     * @return The protocol. Returns null if not applicable.
     */
    public String getProtocol() {
        return null;
    }


    /**
     * Returns the status value.
     *
     * @return The status value. Returns -1 if not applicable.
     */
    public int getStatus() {
        return -1;
    }


    /**
     * Returns the unique identifier.
     *
     * @return The unique identifier.
     */
    public long getUID() {
        return UID;
    }


    /**
     * Checks if the message represents an initialization.
     *
     * @return True if the message represents an initialization, false otherwise.
     */
    public boolean init(){
        return false;
    }


    /**
     * Returns the type of the message.
     *
     * @return The type of the message. Returns null if not applicable.
     */
    public String typeMessage(){
        return null;
    }


    /**
     * Returns the list of positions.
     *
     * @return The list of positions. Returns null if not applicable.
     */
    public ArrayList<Coordinates> getPositions() {
        return null;
    }


    /**
     * Returns the array of colours.
     *
     * @return The array of colours. Returns null if not applicable.
     */
    public String[] getColours() {
        return null;
    }


    /**
     * Returns the personal target card.
     *
     * @return The personal target card. Returns null if not applicable.
     */
    public CardPersonalTarget getCardPersonalTarget() {
        return null;
    }



    /**
     * Returns the list of common target cards.
     *
     * @return The list of common target cards. Returns null if not applicable.
     */
    public ArrayList<CardCommonTarget> getCommonTargets() {
        return null;
    }


    /**
     * Returns the shelf.
     *
     * @return The shelf. Returns null if not applicable.
     */
    public ColourTile[][] getShelf() {
        return null;
    }


    /**
     * Returns the array of common tokens.
     *
     * @return The array of common tokens. Returns null if not applicable.
     */
    public int[] getCommonTokens() {
        return null;
    }


    /**
     * Checks if the message represents an end game token.
     *
     * @return True if the message represents an end game token, false otherwise.
     */
    public boolean isEndGameToken() {
        return false;
    }

    /**
     * returns a user to which send a private message
     */
    public String getPrivateUser() {
        return null;
    }

    /**
     * returns user points
     */
    public int getPoints() {
        return 0;
    }

}
