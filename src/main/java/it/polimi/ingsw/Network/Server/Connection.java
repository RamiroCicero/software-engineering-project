package it.polimi.ingsw.Network.Server;

import it.polimi.ingsw.Network.Messages.Message;

/**
 * The Connection interface represents a connection between a client and a server.
 */
public interface Connection {
    /**
     * Sends a message over the connection.
     *
     * @param message The message to be sent
     */
    void sendMessage(Message message);


    /**
     * Retrieves the unique identifier associated with the connection.
     *
     * @return The unique identifier
     */
    Long getUID();


    /**
     * Retrieves the nickname associated with the connection.
     *
     * @return The nickname
     */
    String getNickname();


    /**
     * Sets the nickname for the connection.
     *
     * @param nickname The nickname to be set
     */
    void setNickname(String nickname);
}
