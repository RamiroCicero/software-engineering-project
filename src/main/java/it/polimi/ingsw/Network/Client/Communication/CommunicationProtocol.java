package it.polimi.ingsw.Network.Client.Communication;

import it.polimi.ingsw.Network.Messages.Message;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * The CommunicationProtocol interface defines the methods for communication between client and server.
 */
public interface CommunicationProtocol extends Remote {
    /**
     * Sends a message from client to server.
     *
     * @param message The message to be sent
     * @throws RemoteException if a remote communication error occurs
     */
    void sendMessage(Message message) throws RemoteException;


    /**
     * Handles an incoming message on the client side.
     *
     * @param message The incoming message to be processed
     * @throws RemoteException if a remote communication error occurs
     */
    void onMessage(Message message) throws RemoteException;


    /**
     * Retrieves the list of messages on the client side.
     *
     * @return The list of messages
     * @throws RemoteException if a remote communication error occurs
     */
    ArrayList<Message> getMessages() throws RemoteException;


    /**
     * Closes the connection between client and server.
     *
     * @throws RemoteException if a remote communication error occurs
     */
    void closeConnection() throws IOException;


    /**
     * Performs the setup for the communication protocol.
     *
     * @throws RemoteException if a remote communication error occurs
     */
    void setup() throws RemoteException;


    /**
     * Retrieves the unique identifier associated with the communication protocol.
     *
     * @return The unique identifier
     * @throws RemoteException if a remote communication error occurs
     */
    long getUID() throws RemoteException;

    /**
     * handles the ping service, receiving the one from the server and resetting the timer for the server disconnection
     */
    void ping() throws RemoteException;

    /**
     * resets the client timer that ensures the responsiveness of the server
     */
    void resetTimer() throws RemoteException;


}

