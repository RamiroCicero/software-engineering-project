package it.polimi.ingsw.Network.Client;


import it.polimi.ingsw.Network.Messages.Message;

/**
 * The ClientUpdateListener interface provides a callback method to handle incoming messages during client updates.
 */
public interface ClientUpdateListener {
    /**
     * Handles an incoming message during a client update.
     *
     * @param message The incoming message
     */
    void onUpdate(Message message);
}
