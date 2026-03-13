package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.Network.Client.Communication.CommunicationProtocol;
import it.polimi.ingsw.Network.Messages.Message;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * The Client class represents a client in the client-server communication model.
 * It extends the UnicastRemoteObject class to enable remote method invocation.
 */
public class Client extends UnicastRemoteObject{
    private int gameID;
    private String username;
    private final CommunicationProtocol communicationProtocol;
    private boolean first = false;
    /**
     * Constructs a new Client object with the specified communication protocol.
     *
     * @param communicationProtocol The communication protocol used for sending and receiving messages.
     * @throws RemoteException if there is an issue with the remote communication.
     */
    public Client(CommunicationProtocol communicationProtocol) throws RemoteException {
        super();
        this.communicationProtocol = communicationProtocol;
    }


    /**
     * Sends a message from the client to the server.
     *
     * @param message The message to be sent.
     */
    public void sendMessage(Message message) {
        try {
            communicationProtocol.sendMessage(message);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the username of the client.
     *
     * @return The username of the client.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the client.
     *
     * @param username The username to be set.
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * Retrieves the list of messages received by the client.
     *
     * @return The list of received messages.
     */
    public ArrayList<Message> getMessages() {
        try {
            return communicationProtocol.getMessages();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the connection between the client and the server.
     */
    public void closeConnection() {
        try {
            communicationProtocol.closeConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets up the client connection with the server.
     */
    public void setup(){
        try {
            communicationProtocol.setup();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the unique identifier (UID) of the client.
     *
     * @return The UID of the client.
     */
    public long getUID(){
        try {
            return communicationProtocol.getUID();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the game ID associated with the client.
     *
     * @return The game ID associated with the client.
     */
    public int getGameID() {
        return gameID;
    }

    /**
     * Sets the game ID associated with the client.
     *
     * @param gameID The game ID to be set.
     */
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst() {
        this.first = true;
    }
}

