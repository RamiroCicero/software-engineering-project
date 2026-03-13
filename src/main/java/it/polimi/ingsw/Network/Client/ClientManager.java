package it.polimi.ingsw.Network.Client;

import com.google.gson.Gson;
import it.polimi.ingsw.Network.Client.Communication.CommunicationProtocol;
import it.polimi.ingsw.Network.Client.Communication.RMICommunicationProtocol;
import it.polimi.ingsw.Network.Client.Communication.TCPCommunicationProtocol;
import it.polimi.ingsw.Network.Messages.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The class ClientManager serves as a base class for managing the client-side functionality.
 * It implements the ClientListener and ClientUpdateListener interfaces and provides a mechanism to handle
 * different types of messages received from the server.
 */
public abstract class ClientManager implements ClientListener, ClientUpdateListener, Runnable {

    private ClientUpdate clientUpdater;
    private Client client;

    private final Queue<Runnable> queue= new LinkedBlockingQueue<>();
    private ArrayList<String> players;



    /**
     * Constructs a ClientManager object and starts a new thread for running the message handling loop.
     */
    public ClientManager() {
        new Thread(this).start();
    }

    /**
     * the implementation of the onUpdate method, that handles the various message, adding them to a queue of runnable
     * @param message The incoming message
     */
    @Override
    public void onUpdate(Message message) {

        switch (message.typeMessage()) {
            case "LoginResponse" -> handleLoginResponse((LoginResponse) message);
            case "InitResponse" -> handleInitResponse((InitResponse) message);
            case "BoardResponse" -> handleBoardResponse((BoardResponse) message);
            case "RemoveResponse" -> handleRemoveResponse((RemoveResponse) message);
            case "WakeMessage" -> handleWakeMessage((WakeMessage) message);
            case "TurnResponse" -> handleTurnResponse((TurnResponse) message);
            case "EndMessage" -> handleEndMessage((EndMessage) message);
            case "SetResponse"->handleSetResponse((SetResponse)message);
            case "FirstResponse"->handleFirstResponse((FirstResponse)message);
            case "PreLoginResponse"->handlePreLoginResponse((PreLoginResponse) message);
            case "UsernameError"-> handleUsernameError((UsernameError) message);
            case "CardsResponse"-> handleCardResponse((CardsResponse) message);
            case "ReFirstResponse"-> handleReFirstResponse((ReFirstResponse) message);
            case "DisconnectionMessage"-> handleDisconnectionMessage((DisconnectionMessage) message);
            case "ChatMessage"->handleChatMessage((ChatMessage) message);
        }
    }


    /**
     *  Handler methods for different types of messages
     */

    private void handleReFirstResponse(ReFirstResponse message) {
        queue.add(()->reFirstResponse(message));
    }

    private void handleCardResponse(CardsResponse message) {
        queue.add(()->cardsResponse(message));
    }

    private void handleUsernameError(UsernameError message) {
        queue.add(()->usernameError(message));
    }

    private void handlePreLoginResponse(PreLoginResponse message) {
        queue.add(()->preLoginResponse(message));
    }

    private void handleFirstResponse(FirstResponse message) {
        queue.add(()->firstResponse(message));
    }

    private void handleSetResponse(SetResponse message) {
        queue.add(()->setResponse(message));
    }

    private void handleRemoveResponse(RemoveResponse message) {
        queue.add(()->removeResponse(message));
    }

    private void handleInitResponse(InitResponse message) {
        queue.add(()->initResponse(message));
    }

    private void handleLoginResponse(LoginResponse message) {
        queue.add(()->loginResponse(message));
    }

    private void handleBoardResponse(BoardResponse message) {
        queue.add(()->updateBoard(message));
    }

    private void handleEndMessage(EndMessage message) {
        queue.add(()->endGame(message));
    }

    private void handleTurnResponse(TurnResponse message) {
        queue.add(()->turnResponse(message));
    }

    private void handleWakeMessage(WakeMessage message) {
        queue.add(()->wakeUp(message));
    }

    private void handleDisconnectionMessage(DisconnectionMessage message) {
        queue.add(()->disconnectionMessage(message));
    }
    private void handleChatMessage(ChatMessage message) {
        queue.add(()->chatMessage(message));
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                queue.remove().run();
            }catch (NoSuchElementException e){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    /**
     * Creates a connection to the server using the specified connection type.
     *
     * @param connection The type of connection ("TCP" or "RMI") to establish with the server.
     */
    public void createConnection(String connection,int port) {
        CommunicationProtocol communicationProtocol;
        if (connection.equalsIgnoreCase("TCP")) {
            try {
                communicationProtocol = new TCPCommunicationProtocol("localhost", port);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

        } else {
            try {
                communicationProtocol = new RMICommunicationProtocol("RemoteController",port);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            client = new Client(communicationProtocol);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        client.setup();
        startUpdater();
    }

    /**
     * Starts the client updater, which is responsible for receiving updates from the server and notifying the client manager.
     */
    private void startUpdater() {
        clientUpdater = new ClientUpdate(client, this);
    }

    /**
     * Closes the connection to the server.
     */
    public void closeConnection() {
        if (clientUpdater != null) {
            clientUpdater.stop();
            clientUpdater = null;
        }
        client.closeConnection();
        client = null;
    }

    /**
     * Returns the Client object associated with this ClientManager.
     *
     * @return The Client object.
     */
    public Client getClient() {
        return client;
    }

    public void setNicknames(ArrayList<String> players) {
        this.players=players;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }
}
