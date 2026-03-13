package it.polimi.ingsw.Network.Server;

import it.polimi.ingsw.Network.Messages.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Represents a TCP connection implementation of the Connection interface.
 * It maintains a socket connection with the server and provides methods to send and receive messages.
 */
public class TCPConnect implements Connection {
    private Socket socket;
    private String nickname;
    private final PrintWriter out;
    private BufferedReader in;
    long UID;

    /**
     * Constructs a TCPConnect object with the specified socket, UID, and nickname.
     * Initializes input and output streams for the socket.
     *
     * @param socket   The socket representing the TCP connection.
     * @param UID      The unique identifier associated with the connection.
     * @param nickname The nickname associated with the client.
     * @throws RuntimeException If an IOException occurs while initializing the input or output streams.
     */
    public TCPConnect(Socket socket, Long UID, String nickname) {
        this.socket = socket;
        this.UID = UID;
        this.nickname = nickname;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public Long getUID() {
        return UID;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Sends a message by converting it to JSON format and writing it to the output stream.
     *
     * @param message The message to be sent.
     */
    public void sendMessage(Message message){
        out.println(message.toJson());
        out.flush();
    }
}
