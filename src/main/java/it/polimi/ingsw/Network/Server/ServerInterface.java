package it.polimi.ingsw.Network.Server;

import it.polimi.ingsw.Network.Client.Communication.CommunicationProtocol;
import it.polimi.ingsw.Network.Messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The ServerInterface interface defines the methods that a remote server should implement.
 */
public interface ServerInterface extends Remote {
    /**
     * Called when a message is received by the server.
     *
     * @param message the message received by the server
     * @throws RemoteException if a communication-related exception occurs
     */
    void onMessage(Message message) throws RemoteException;

    /**
     * Disconnects the client from the server.
     *
     * @throws RemoteException if a communication-related exception occurs
     */
    void disconnect(Long UID) throws RemoteException;

    /**
     * Adds an RMI client to the server and returns a unique identifier (UID) assigned to the client.
     *
     * @param protocol the communication protocol used by the client
     * @return the unique identifier (UID) assigned to the client
     * @throws RemoteException if a communication-related exception occurs
     */
    long addRmiClient(CommunicationProtocol protocol) throws RemoteException;


}

