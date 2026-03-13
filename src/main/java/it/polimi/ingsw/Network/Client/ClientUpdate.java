package it.polimi.ingsw.Network.Client;



import it.polimi.ingsw.Network.Messages.Message;

import java.util.ArrayList;

/**
 * The ClientUpdate class is responsible for continuously updating the client with incoming messages.
 * It runs in a separate thread and retrieves messages from the client, notifying the client update listener for each message.
 */
public class ClientUpdate implements Runnable{
    private final Client client;
    private final ClientUpdateListener clientUpdateListener;
    private final Thread thread;

    /**
     * Constructs a new instance of the ClientUpdate class.
     *
     * @param client                The client object
     * @param clientUpdateListener  The client update listener to be notified of incoming messages
     */
    public ClientUpdate(Client client, ClientUpdateListener clientUpdateListener) {
        this.client = client;
        this.clientUpdateListener = clientUpdateListener;
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (client) {
                ArrayList<Message> messages;

                do {
                    messages = client.getMessages();
                    try {
                        client.wait(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } while (messages.isEmpty());

                messages.forEach(clientUpdateListener::onUpdate);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Stops the client update process by interrupting the thread.
     */

    public void stop() {
        this.thread.interrupt();
    }

    /**
     * Starts the client update process if it has been interrupted.
     */
    public void start() {
        if (this.thread.isInterrupted()) {
            this.thread.start();
        }
    }
}

