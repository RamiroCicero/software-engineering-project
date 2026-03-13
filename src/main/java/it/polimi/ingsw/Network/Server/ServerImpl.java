package it.polimi.ingsw.Network.Server;

import it.polimi.ingsw.Exception.*;
import it.polimi.ingsw.Network.Client.Communication.CommunicationProtocol;
import it.polimi.ingsw.Network.Messages.*;
import it.polimi.ingsw.Utils.Coordinates;
import it.polimi.ingsw.Utils.TileSlot;
import it.polimi.ingsw.controller.MasterController;
import it.polimi.ingsw.model.CommonCards.CardCommonTarget;
import it.polimi.ingsw.model.PersonalCards.CardPersonalTarget;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.model.Tile.ColourTile;
import it.polimi.ingsw.model.Tile.Tile;
import javafx.util.Pair;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;

/**
 * ServerImpl class is an implementation of the RemoteController interface.
 * It provides remote control functionality for managing game sessions.
 */
public class ServerImpl implements Serializable {

    private static final long PING_INTERVAL = 5000;
    private final MasterController masterController;
    private final HashMap<Long, Socket> tempTcp;
    private final HashMap<Long, CommunicationProtocol> tempRmi;
    private final HashMap<Integer, List<Connection>> clients;
    private int currentGameID;
    private final ArrayList<ArrayList<Pair<Long, String>>> lobby;
    private final ArrayList<Timer> lobbyTimers;
    private final ArrayList<Timer> gameTimers;


    /**
     * Constructor for ServerImpl class.
     * Creates a new instance of MasterController and sets the currentGameID to -1.
     *
     */
    public ServerImpl()  {

        lobby = new ArrayList<>();
        lobby.add(new ArrayList<>());
        tempTcp = new HashMap<>();
        tempRmi = new HashMap<>();
        clients = new HashMap<>();
        masterController = new MasterController();
        lobbyTimers = new ArrayList<>();
        gameTimers = new ArrayList<>();
        currentGameID = -1;
    }


    /**
     * starts the ping service in the lobby, disconnecting all the players in a same lobby if someone disconnected
     * @param pos the number of the lobby
     */
    public void startLobbyPingTimer(int pos, Timer timer) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int playersBeforePing=lobby.get(pos).size();
                    lobby.get(pos).removeIf(entry -> {
                        Long clientId = entry.getKey();
                        return ping(clientId);
                    });
                    if (playersBeforePing>lobby.get(pos).size()){

                        for (int i=0;i<lobby.get(pos).size();i++){
                            Long clientId = lobby.get(pos).get(i).getKey();
                            if (tempRmi.containsKey(clientId)) {
                                CommunicationProtocol client = tempRmi.get(clientId);
                                try {
                                    client.onMessage(new DisconnectionMessage(-1, clientId, false));
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else {
                                Socket client = tempTcp.get(clientId);
                                PrintWriter out;
                                try {
                                    out = new PrintWriter(client.getOutputStream(),true);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                out.println(new DisconnectionMessage(-1,clientId, false).toJson());
                                out.flush();
                            }

                        }
                        lobbyTimers.get(pos).cancel();
                        lobbyTimers.remove(pos);
                        lobby.remove(pos);
                    }
            }
        }, 0,PING_INTERVAL);
    }

    private boolean ping(Long clientId) {
        try {
            if (tempRmi.containsKey(clientId)) {
                CommunicationProtocol client = tempRmi.get(clientId);
                client.ping();
            }
            else {
                Socket client = tempTcp.get(clientId);
                PrintWriter out = new PrintWriter(client.getOutputStream(),true);
                out.println(new PingMessage(-1,clientId).toJson());
                out.flush();
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }


    /**
     * Starts the ping service in the game, disconnecting all the players in a same game if someone disconnected
     * @param gameID the game id of the game
     */

    public void startGamePingTimer(int gameID, Timer timer) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int playersBeforePing=clients.get(gameID).size();
                clients.get(gameID).removeIf(entry -> {
                    Long clientId = entry.getUID();
                    return ping(clientId);
                });
                if (playersBeforePing>clients.get(gameID).size()){

                    for (int i=0;i<clients.get(gameID).size();i++){
                        Long clientId = clients.get(gameID).get(i).getUID();
                        clients.get(gameID).get(i).sendMessage(new DisconnectionMessage(-1, clientId, false));
                    }
                    gameTimers.get(gameID).cancel();
                }
            }
        }, 0,PING_INTERVAL);
    }


    /**
     * Method that sorts the various messages arriving to the server
     * @param message the received message
     */

    public void onMessage(Message message)  {


        switch (message.typeMessage()){
            case "LoginMessage"-> registerPlayer(message.getGameID(), message.getNickname(), message.getUID());
            case "PreLoginMessage"-> preRegistration(message);
            case "SetMessage"->setMaxPlayers(message);
            case "RemoveMessage"->remove(message.getGameID(), message.getPositions(), message.getUID());
            case "TurnMessage"->turn(message.getGameID(), message.getColours(), message.getColumn(), message.getNickname(), message.getUID());
            case "BoardMessage"->sendBoard(message.getGameID());
            case "ChatMessage"->chat(message);
            default -> throw new IllegalStateException("Unexpected value: " + message.typeMessage());
        }
    }

    /**
     * Method to manage the chat,both in the case of a message to all and in the other of a private message
     */
    private void chat(Message message) {
        if (message.getPrivateUser()!=null){

            for (int i=0; i<clients.get(message.getGameID()).size();i++) {
                if (clients.get(message.getGameID()).get(i).getNickname().equals(message.getPrivateUser())) {
                    clients.get(message.getGameID()).get(i).sendMessage(message);
                }
                if (clients.get(message.getGameID()).get(i).getUID().equals(message.getUID())){
                    clients.get(message.getGameID()).get(i).sendMessage(message);
                }
            }
        }else {
            for (int i=0; i<clients.get(message.getGameID()).size();i++) {
                clients.get(message.getGameID()).get(i).sendMessage(message);
            }
        }

    }


    /**
     * Performs pre-registration for the player that sent the message.
     *
     * @param message the message for pre-registration
     */
    public void preRegistration(Message message) {
        synchronized (lobby){

            if (lobby.size()==0)lobby.add(new ArrayList<>());

            for (int i = 0; i < lobby.size(); i++) {
                if (lobby.get(i).size() == 0) {
                    lobby.get(i).add(new Pair<>(message.getUID(), message.getNickname()));
                    int gameID = startGame();
                    clients.put(gameID, new ArrayList<>());
                    addClient(message.getUID(), message.getNickname(), gameID);
                    registerPlayer(gameID, message.getNickname(), message.getUID());
                    clients.get(gameID).get(getPosition(message.getUID(), gameID)).sendMessage(new FirstResponse(gameID, message.getUID()));
                    lobbyTimers.add(new Timer());
                    startLobbyPingTimer(i, lobbyTimers.get(lobbyTimers.size()-1));
                    break;
                }
                Message preLogMess = new PreLoginResponse(-1, message.getUID());
                if (lobby.get(i).size() == 1 || lobby.get(i).size() == 2) {
                    lobby.get(i).add(new Pair<>(message.getUID(), message.getNickname()));
                    containsTcpTemp(message, preLogMess);
                    break;
                } else if (lobby.get(i).size() == 3) {
                    lobby.get(i).add(new Pair<>(message.getUID(), message.getNickname()));
                    lobby.add(new ArrayList<>());
                    containsTcpTemp(message, preLogMess);
                    break;
                }

            }
        }
    }

    /**
     * removes an RMI client from the list of all rmi connection if a disconnection occurs
     * @param UID the unique identifier of the client
     */
    public void removeRmiClient(Long UID) {
        tempRmi.remove(UID);
    }

    /**
     * Checks and handles the presence of TCP and RMI connections for the given message.
     *
     * @param message   the message to be checked
     * @param preLogMess the message for pre-login response
     */
    public void containsTcpTemp(Message message, Message preLogMess)  {
        if (tempTcp.containsKey(message.getUID())) {
            try {
                PrintWriter out = new PrintWriter(tempTcp.get(message.getUID()).getOutputStream());
                out.println(preLogMess.toJson());
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (tempRmi.containsKey(message.getUID())) {

            CommunicationProtocol communication = tempRmi.get(message.getUID());
            try {
                communication.onMessage(preLogMess);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

        }
    }

    /**
     * Sets the maximum number of players for the game.
     *
     * @param message the message containing the game ID and maximum players
     */
    public void setMaxPlayers(Message message)  {
        int gameID = message.getGameID();
        int maxPlayers = message.getMaxPlayers();
        masterController.getGameController(gameID).setMaxPlayers(maxPlayers);
        clients.get(gameID).get(0).sendMessage(new SetResponse(gameID,message.getUID()));
        Thread timerThread = new Thread(() -> startTimerSetMessage(message, maxPlayers));
        timerThread.start();
    }

    /**
     * Starts a timer for the SetMessage operation to wait for the specified number of players.
     *
     * @param message     the message containing the game ID and maximum players
     * @param maxPlayers  the maximum number of players
     */
    private void startTimerSetMessage(Message message, int maxPlayers) {
        while (!checkPlayerCount(message, maxPlayers)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lobbyManagement(message, maxPlayers);
        Thread.currentThread().interrupt();
    }

    /**
     * Checks if the required number of players has been reached for the SetMessage operation.
     *
     * @param message    the message containing the game ID and maximum players
     * @param maxPlayers the maximum number of players
     * @return true if the required number of players is reached, false otherwise
     */
    private boolean checkPlayerCount(Message message, int maxPlayers) {
        for (ArrayList<Pair<Long, String>> pairs : lobby) {
            if (pairs.get(0).getKey().equals(message.getUID())) {
                return pairs.size() >= maxPlayers;
            }
        }
        return false;
    }

    /**
     * Manages the lobby for the given message and maximum players.
     *
     * @param message    the message containing the game ID and maximum players
     * @param maxPlayers the maximum number of players
     * @ if there is an error in the remote communication
     */
    public void lobbyManagement(Message message, int maxPlayers)  {

        int gameID = message.getGameID();
        int pos = -1;
        for (int i = 0; i < lobby.size(); i++) {
            if (lobby.get(i).get(0).getKey().equals(message.getUID())) {
                pos = i;
                break;
            }
        }
        ArrayList<Pair<Long, String>> pairs = lobby.get(pos);

        for (int j = 1; j < maxPlayers; j++) {
            Long UID = pairs.get(j).getKey();
            if (tempTcp.containsKey(UID)) {
                clients.get(gameID).add(new TCPConnect(tempTcp.get(UID), UID, pairs.get(j).getValue()));
            }
            if (tempRmi.containsKey(UID)) {
                clients.get(gameID).add(new RMIConnect(tempRmi.get(UID), UID, pairs.get(j).getValue()));
            }
            registerPlayer(gameID, pairs.get(j).getValue(), UID);
        }
        Thread timerThread = new Thread(() -> startTimerUsername(message, maxPlayers));
        timerThread.start();
    }

    /**
     * Starts a timer for the TurnMessage operation to wait for the maximum number of players to join.
     *
     * @param message     the message containing the game ID, colours, column, nickname, and UID
     * @param maxPlayers  the maximum number of players
     */
    private void startTimerUsername(Message message, int maxPlayers) {
        while (!checkMaxPlayer(message)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        init(message, maxPlayers);
        Thread.currentThread().interrupt();
    }

    /**
     * Checks if the maximum number of players has joined for the TurnMessage operation.
     *
     * @param message    the message containing the game ID, colours, column, nickname, and UID
     * @return true if the maximum number of players has joined, false otherwise
     */
    private boolean checkMaxPlayer(Message message) {
        return masterController.getGameController(message.getGameID()).getMaxPlayers() == masterController.getGameController(message.getGameID()).getNumOfPlayers();
    }

    /**
     * Initializes the game for the given message and maximum players.
     *
     * @param message    the message containing the game ID, colours, column, nickname, and UID
     * @param maxPlayers the maximum number of players
     * @ if there is an error in the remote communication
     */
    public void init(Message message,int maxPlayers)  {
        int gameID = message.getGameID();

        initGame(gameID);
        sendCards(gameID);

        synchronized (lobby) {
            int pos = -1;
            for (int i = 0; i < lobby.size(); i++) {
                if (lobby.get(i).get(0).getKey().equals(message.getUID())) {
                    pos = i;
                    break;
                }
            }
            if (lobby.get(pos).size() > maxPlayers) {


                if (pos == lobby.size() - 1) {
                    lobby.add(new ArrayList<>());
                } else if (lobby.get(lobby.size() - 1).size() == 4) {
                    lobby.add(new ArrayList<>());
                }
                for (int j = maxPlayers; j < lobby.get(pos).size(); j++) {
                    for (int k = pos + 1; k < lobby.size(); k++) {
                        if (lobby.get(k).size() == 0) {
                            lobby.get(k).add(new Pair<>(lobby.get(pos).get(j).getKey(), lobby.get(pos).get(j).getValue()));
                            int gameID2 = startGame();
                            clients.put(gameID2, new ArrayList<>());
                            if (tempTcp.containsKey(lobby.get(pos).get(j).getKey())) {
                                clients.get(gameID2).add(new TCPConnect(tempTcp.get(lobby.get(pos).get(j).getKey()), lobby.get(pos).get(j).getKey(), lobby.get(pos).get(j).getValue()));
                            }
                            if (tempRmi.containsKey(lobby.get(pos).get(j).getKey())) {
                                clients.get(gameID2).add(new RMIConnect(tempRmi.get(lobby.get(pos).get(j).getKey()), lobby.get(pos).get(j).getKey(), lobby.get(pos).get(j).getValue()));
                            }
                            registerPlayer(gameID2, lobby.get(pos).get(j).getValue(), lobby.get(pos).get(j).getKey());
                            clients.get(gameID2).get(getPosition(lobby.get(pos).get(j).getKey(), gameID2)).sendMessage(new ReFirstResponse(gameID2, lobby.get(pos).get(j).getKey()));
                            break;
                        } else if (lobby.get(k).size() == 1 || lobby.get(k).size() == 2) {
                            lobby.get(k).add(new Pair<>(lobby.get(pos).get(j).getKey(), lobby.get(pos).get(j).getValue()));
                            break;
                        } else if (lobby.get(k).size() == 3) {
                            lobby.get(k).add(new Pair<>(lobby.get(pos).get(j).getKey(), lobby.get(pos).get(j).getValue()));
                            lobby.add(new ArrayList<>());
                            break;
                        }
                    }
                }
            }

            Long tempUID = clients.get(gameID).get(0).getUID();
            int maxLobbyIndex = -1;
            for (int i = 0; i < lobby.size(); i++) {
                if (lobby.get(i).size()!=0) {
                    if (lobby.get(i).get(0).getKey().equals(tempUID)) {
                        maxLobbyIndex = i;
                    }
                }
            }
            lobby.add(new ArrayList<>());
            lobby.remove(maxLobbyIndex);
            lobbyTimers.get(maxLobbyIndex).cancel();
            lobbyTimers.remove(maxLobbyIndex);


        }
        gameTimers.add(new Timer());
        startGamePingTimer(gameID,gameTimers.get(gameTimers.size()-1));

    }

    /**
     * Adds a client with the specified UID, nickname, and game ID.
     * @param UID The UID of the client.
     * @param nickname The nickname of the client.
     * @param gameID The ID of the game.
     */
    protected void addClient(Long UID, String nickname, int gameID) {
        if (tempTcp.containsKey(UID)) {
            clients.get(gameID).add(new TCPConnect(tempTcp.get(UID), UID, nickname));
        }

        if (tempRmi.containsKey(UID)) {
            clients.get(gameID).add(new RMIConnect(tempRmi.get(UID), UID, nickname));
        }
    }


    /**
     * Adds an RMI client with the given UID and communication protocol.
     * @param UID The UID of the RMI client.
     * @param protocol The communication protocol for the client.
     */
    public void addRmiCl(long UID, CommunicationProtocol protocol)  {
        tempRmi.put(UID, protocol);
    }

    /**
     * Adds a TCP client with the given UID and socket.
     * @param UID The UID of the TCP client.
     * @param socket The socket associated with the client.
     */
    public void addTcpCl(long UID, Socket socket)  {
        tempTcp.put(UID, socket);
    }



    public synchronized int startGame() {
        masterController.newGameController();
        currentGameID=currentGameID + 1;
        return currentGameID;
    }



    public synchronized void registerPlayer(int gameID, String nickname, Long UID)  {
        try {
            clients.get(gameID).get(getPosition(UID, gameID)).setNickname(nickname);
            masterController.getGameController(gameID).login(nickname);

        } catch (UsernameException e) {
            clients.get(gameID).get(getPosition(UID, gameID)).sendMessage(new UsernameError(gameID,UID));
        } catch (GameAlreadyStarted | MaxPlayerException e) {
            throw new RuntimeException(e);
        }
        if (masterController.getGameController(gameID).getNumOfPlayers() != 1) {
            clients.get(gameID).get(getPosition(UID, gameID)).sendMessage(new LoginResponse(gameID,UID));
        }

    }

    /**
     * Sends the cards to the players in the specified game.
     * @param gameID The ID of the game.
     */
    public void sendCards(int gameID){
        ArrayList<CardCommonTarget> commonTargets = masterController.getGameController(gameID).getCommonTargets();
        for (int i = 0; i < clients.get(gameID).size(); i++) {
            CardPersonalTarget cardPersonalTarget = masterController.getGameController(gameID).getCardPersonalTarget(clients.get(gameID).get(i).getNickname());
            Message cardsResponse = new CardsResponse(gameID,clients.get(gameID).get(i).getUID(), commonTargets, cardPersonalTarget);
            clients.get(gameID).get(i).sendMessage(cardsResponse);
        }
    }


    /**
     * Initializes the game for the specified game ID.
     * @param gameID The ID of the game.
     */
    public void initGame(int gameID)  {
        try {
            this.masterController.getGameController(gameID).initGame();
            ArrayList<String> playersNicknames=new ArrayList<>();
            for (Player player:masterController.getGameController(gameID).getPlayers()){
                playersNicknames.add(player.getNickname());
            }

            for (int i = 0; i < clients.get(gameID).size(); i++) {
                Message initResponse = new InitResponse(gameID,clients.get(gameID).get(i).getUID(),playersNicknames);
                clients.get(gameID).get(i).sendMessage(initResponse);
            }
            currentPlayer(gameID);
        } catch (GameNotReadyException | GameAlreadyStarted e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Determines the current player for the specified game and initiates their turn.
     * @param gameID The ID of the game.
     */
    public void currentPlayer(int gameID)  {
        String player = masterController.getGameController(gameID).getCurrentPlayer().getNickname();
        for (int j = 0; j < clients.get(gameID).size(); j++) {
            if (clients.get(gameID).get(j).getNickname().equals(player)) {
                playClient(getPosition(clients.get(gameID).get(j).getUID(), gameID), gameID);
                break;
            }
        }
    }


    public void remove(int gameID, ArrayList<Coordinates> positions, Long UID)  {
        try {
            masterController.getGameController(gameID).remove(positions);
            clients.get(gameID).get(getPosition(UID, gameID)).sendMessage(new RemoveResponse(gameID,UID,false));
        } catch (InvalidPositionsException | EmptySlotException | InvalidSlotException e) {
            clients.get(gameID).get(getPosition(UID, gameID)).sendMessage(new RemoveResponse(gameID,UID,true));
        }
    }


    /**
     * Executes a player's turn with the specified colors, column, nickname, and UID for the given game.
     *
     * @param gameID The ID of the game.
     * @param colors The colors of the tiles to place.
     * @param column The column in which to place the tiles.
     * @param nickname The nickname of the player.
     * @param UID The UID of the player.
     */
    public void turn(int gameID, String[] colors, int column, String nickname, Long UID)  {
        for (int i = 0; i < clients.get(gameID).size(); i++) {
            if (masterController.getGameController(gameID).getPlayers().get(i).getNickname().equals(nickname)) {

                ColourTile[][] shelfColours = new ColourTile[6][5];
                try {
                    Tile[] tiles = new Tile[colors.length];

                    for (int j = 0; j < colors.length; j++) {
                        tiles[j] = new Tile(ColourTile.valueOf(colors[j]));
                    }

                    masterController.getGameController(gameID).turn(tiles, column);
                    getShelfByNickname(gameID, nickname, shelfColours);

                } catch (EmptySlotException | InvalidPositionsException | InvalidSlotException |
                         NoSpaceInColumnException | SoldOutTilesException | GameAlreadyStarted e) {
                    clients.get(gameID).get(getPosition(UID, gameID)).sendMessage(new TurnResponse(-1,gameID,UID, null, -1));
                    return;

                } catch (EndGameException e) {
                    getShelfByNickname(gameID, nickname, shelfColours);

                    Message message = new TurnResponse(0,gameID,UID, shelfColours, column);
                    clients.get(gameID).get(getPosition(UID, gameID)).sendMessage(message);
                    String winner = getWinner(gameID);
                    for (int j = 0; j < clients.get(gameID).size(); j++) {
                        String nick=clients.get(gameID).get(j).getNickname();
                        clients.get(gameID).get(j).sendMessage(new EndMessage(winner,gameID,UID,masterController.getGameController(gameID).getPlayerByNickname(nick).getScore()));
                    }
                    gameTimers.get(gameID).cancel();

                    return;
                }
                Message message = new TurnResponse(0,gameID,UID, shelfColours, column);
                clients.get(gameID).get(getPosition(UID, gameID)).sendMessage(message);
                currentPlayer(gameID);
            }
        }
    }

    /**
     * Retrieves the shelf colors for the specified game, player nickname
     * @param gameID The ID of the game.
     * @param nickname The nickname of the player.
     * @param shelfColours The array to store the shelf colors.
     */
    public void getShelfByNickname(int gameID, String nickname, ColourTile[][] shelfColours) {
        TileSlot[][] shelf = masterController.getGameController(gameID).getShelf(nickname);
        for(int j = 0; j < 6; j++){
            for(int k = 0; k<5 ; k++){
                if(!shelf[j][k].isFree()){
                    shelfColours[j][k] = shelf[j][k].getAssignedTile().colour();
                }
                else{
                    shelfColours[j][k] = ColourTile.FREE;
                }
            }
        }
    }


    /**
     * Retrieves the winner of the specified game.
     * @param gameID The ID of the game.
     * @return The nickname of the winner.
     */
    public String getWinner(int gameID)  {
        return masterController.getGameController(gameID).endGame().getNickname();
    }


    /**
     * Sends the board state to all clients in the specified game.
     * @param gameID The ID of the game.
     */
    public void sendBoard(int gameID) {
        TileSlot[][] board = masterController.getGameController(gameID).getBoard();
        ColourTile[][] colours = new ColourTile[11][11];
        for(int i = 0; i < 11; i++){
            for(int j = 0; j<11 ; j++){
                if(!board[i][j].isFree()){
                    colours[i][j] = board[i][j].getAssignedTile().colour();
                }
                else{
                    colours[i][j] = ColourTile.FREE;
                }
            }
        }
        int[] commonTokens = new int[2];
        boolean endGameToken;
        commonTokens[0] = masterController.getGameController(gameID).getCommonTargets().get(0).getHighestToken();
        commonTokens[1] = masterController.getGameController(gameID).getCommonTargets().get(1).getHighestToken();
        endGameToken = masterController.getGameController(gameID).isEndGameTokenTaken();
        for(int i=0; i<clients.get(gameID).size();i++){
            clients.get(gameID).get(i).sendMessage(new BoardResponse(colours,gameID,clients.get(gameID).get(i).getUID(), commonTokens, endGameToken));
        }

    }


    public void playClient(int client, int gameID) {
        Message message = new WakeMessage(gameID, clients.get(gameID).get(client).getUID());
        clients.get(gameID).get(client).sendMessage(message);
    }

    /**
     * Gets the position of the player with the specified UID in the given game.
     * @param UID The UID of the player.
     * @param gameID The ID of the game.
     * @return The position of the player.
     */
    public int getPosition(Long UID ,int gameID){
            for(int i = 0; i < clients.get(gameID).size();i++){
                if(clients.get(gameID).get(i).getUID().equals(UID)){
                    return i;
                }
            }
        return -1;
    }



}