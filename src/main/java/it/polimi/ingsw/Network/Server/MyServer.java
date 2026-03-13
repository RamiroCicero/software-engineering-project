package it.polimi.ingsw.Network.Server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.Network.Client.Communication.CommunicationProtocol;
import it.polimi.ingsw.Network.Messages.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
//
/**
 * Class that represents the server, handles the messages incoming from the client and sends messages to the clients
 */
public class MyServer extends UnicastRemoteObject implements ServerInterface {
    private static final ServerImpl server;

    static {
        server = new ServerImpl();
    }

    /**
     * Constructs a MyServer object and exports it as a remote object.
     *
     * @throws RemoteException if a communication-related exception occurs
     */
    protected MyServer() throws RemoteException {
        super();
    }

    /**
     * The main method of the server that starts the RMI server and listens for TCP socket connections.
     *
     * @param args command line arguments
     * @throws Exception if an exception occurs
     */
    public static void main(String[] args) throws Exception {

        int portNumberRMI =Integer.parseInt(args[0]);
        int portNumberTCP =Integer.parseInt(args[1]);

        ServerInterface myServer = new MyServer();
        Registry registry = LocateRegistry.createRegistry(portNumberRMI);
        registry.rebind("RemoteController", myServer);
        System.out.println("RMI server is running...");


        // Creare un socket server TCP
       try (ServerSocket serverSocket = new ServerSocket(portNumberTCP)) {
           System.out.println("Il server TCP è in esecuzione...");

           Gson gson=new Gson();
           try {
               while (true) {
                   Socket clientSocket = serverSocket.accept();
                   long UID = System.currentTimeMillis();
                   server.addTcpCl(UID, clientSocket);
                   Message uidResponse = new UIDResponse(UID);
                   String json = gson.toJson(uidResponse);
                   PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                   out.println(json);
                   out.flush();
                   ClientHandler clientHandler = new ClientHandler(clientSocket, myServer);
                   clientHandler.start();
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }catch (IOException e){
           e.printStackTrace();
       }

    }

    /**
     * Called when a message is received from a client.
     *
     * @param message the received message
     * @throws RemoteException if a communication-related exception occurs
     */
    public void onMessage(Message message) throws RemoteException {
        server.onMessage(message);
    }

    @Override
    public void disconnect(Long UID) throws RemoteException {
        server.removeRmiClient(UID);
    }


    /**
     * Adds an RMI client to the server.
     *
     * @param protocol the communication protocol of the RMI client
     * @return the unique ID assigned to the RMI client
     * @throws RemoteException if a communication-related exception occurs
     */
    public long addRmiClient(CommunicationProtocol protocol) throws RemoteException{
        long rmiId = System.currentTimeMillis();
        server.addRmiCl(rmiId, protocol);
        return rmiId;
    }
}

/**
 * The ClientHandler class represents a thread that handles communication with a TCP socket client.
 */
class ClientHandler extends Thread {
    private final Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    private final ServerInterface myServer;

    /**
     * Constructs a ClientHandler object with the specified client socket, socket ID, and server interface.
     *
     * @param socket    the client socket
     * @param myServer  the server interface
     */
    public ClientHandler(Socket socket, ServerInterface myServer) {
        this.clientSocket = socket;
        this.myServer=myServer;
    }

    /**
     * Runs the thread and handles communication with the client.
     */
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            String request;

            while ((request = in.readLine()) != null) {
                onMessage(request);
            }
        }
        catch (IOException e) {
           closeConnection();
        } finally {
            closeConnection();
        }

    }

    private void closeConnection() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles a message received from the client.
     *
     * @param request the received message
     */
    private void onMessage(String request) {
        try {
            Gson gson = new Gson();
            JsonElement rootElement = JsonParser.parseString(request);
            JsonObject jsonObject = rootElement.getAsJsonObject();
            String type = jsonObject.get("typeMessage").getAsString();

            switch (type) {
                case "PreLoginMessage" -> myServer.onMessage(gson.fromJson(request, PreLoginMessage.class));
                case "LoginMessage" -> myServer.onMessage(gson.fromJson(request, LoginMessage.class));
                case "SetMessage" -> myServer.onMessage(gson.fromJson(request, SetMessage.class));
                case "RemoveMessage" -> myServer.onMessage(gson.fromJson(request, RemoveMessage.class));
                case "TurnMessage" -> myServer.onMessage(gson.fromJson(request, TurnMessage.class));
                case "BoardMessage" -> myServer.onMessage(gson.fromJson(request, BoardMessage.class));
                case "ChatMessage"->myServer.onMessage(gson.fromJson(request, ChatMessage.class));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }catch (NullPointerException e) {
            closeConnection();

        }

    }
}
