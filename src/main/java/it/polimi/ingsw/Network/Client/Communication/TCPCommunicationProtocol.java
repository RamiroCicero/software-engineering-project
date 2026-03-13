package it.polimi.ingsw.Network.Client.Communication;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.Network.Messages.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The TCPCommunicationProtocol class represents a communication protocol using TCP for remote communication.
 */
public class TCPCommunicationProtocol extends UnicastRemoteObject implements CommunicationProtocol,Runnable {
    private static final long PING_TIMEOUT = 8000;
    private final String serverIp;
    private final int serverPort;
    private PrintWriter out;
    private BufferedReader in;
    private long UID;
    private Socket socket = null;
    private final ArrayList<Message> messageList;
    private Timer timer;
    private Thread messageReceiver;

    /**
     * Constructs a TCPCommunicationProtocol object with the specified server IP and port.
     *
     * @param serverIp   the IP address of the server
     * @param serverPort the port number of the server
     * @throws RemoteException if a remote communication-related exception occurs
     */
    public TCPCommunicationProtocol(String serverIp, int serverPort) throws RemoteException {
        super();
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.messageList = new ArrayList<>();
        startCommunication();


    }

    /**
     * Establishes the communication by creating a socket connection with the server and initializing input and output streams.
     * Throws a RuntimeException if an IOException occurs during the socket creation or stream initialization.
     */
    private void startCommunication() {

        try {
            socket = new Socket(serverIp, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Sends a message using the TCP communication protocol.
     *
     * @param message the message to be sent
     */
    public void sendMessage(Message message) {
        out.println(message.toJson());
        out.flush();
    }

    @Override
    public void closeConnection() throws IOException {
        if (!socket.isClosed()) {
            socket.close();
        }
        if (timer!=null){
            timer.cancel();
        }
        messageReceiver.interrupt();

        in = null;
        out = null;

    }

    @Override
    public void setup() {
        try {
            Gson gson = new Gson();
            UIDResponse uidMessage = gson.fromJson(in.readLine(), UIDResponse.class);
            UID = uidMessage.getUID();
            messageReceiver = new Thread(this);
            messageReceiver.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getUID() {
        return UID;
    }

    @Override
    public void ping() throws RemoteException {
        if (!socket.isClosed()) {
            sendMessage(new PingMessage(-1, UID));
            resetTimer();
        }
    }

    @Override
    public void resetTimer() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onMessage(new DisconnectionMessage(-1,UID,true));
            }
        }, PING_TIMEOUT);
    }


    @Override
    public void onMessage(Message message) {
        messageList.add(message);
    }

    @Override
    public ArrayList<Message> getMessages() {
        ArrayList<Message> copy;

        copy = new ArrayList<>(List.copyOf(messageList));
        messageList.clear();

        return copy;
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                    synchronized (messageList) {
                        Gson gson=new Gson();
                        String request = in.readLine();
                        JsonElement rootElement = JsonParser.parseString(request);
                        JsonObject jsonObject = rootElement.getAsJsonObject();
                        String type = jsonObject.get("typeMessage").getAsString();
                        switch (type) {
                            case "LoginResponse" -> onMessage(gson.fromJson(request, LoginResponse.class));
                            case "InitResponse" -> onMessage(gson.fromJson(request, InitResponse.class));
                            case "BoardResponse" -> onMessage(gson.fromJson(request, BoardResponse.class));
                            case "RemoveResponse" -> onMessage(gson.fromJson(request, RemoveResponse.class));
                            case "WakeMessage" -> onMessage(gson.fromJson(request, WakeMessage.class));
                            case "TurnResponse" -> onMessage(gson.fromJson(request, TurnResponse.class));
                            case "EndMessage" -> onMessage(gson.fromJson(request, EndMessage.class));
                            case "SetResponse"->onMessage(gson.fromJson(request, SetResponse.class));
                            case "FirstResponse"->onMessage(gson.fromJson(request, FirstResponse.class));
                            case "PreLoginResponse"->onMessage(gson.fromJson(request, PreLoginResponse.class));
                            case "UsernameError"-> onMessage(gson.fromJson(request, UsernameError.class));
                            case "CardsResponse"-> onMessage(gson.fromJson(request, CardsResponse.class));
                            case "ReFirstResponse"-> onMessage(gson.fromJson(request, ReFirstResponse.class));
                            case "PingMessage"->ping();

                            case "DisconnectionMessage"->{
                                timer.cancel();
                                onMessage(gson.fromJson(request, DisconnectionMessage.class));
                            }

                            case "ChatMessage"->onMessage(gson.fromJson(request, ChatMessage.class));
                        }
                    }

            }catch (IOException e){
                disconnect();
            }
        }
    }

    private void disconnect() {
        try {
            closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
