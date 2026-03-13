package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.Network.Client.ClientManager;
import it.polimi.ingsw.Network.Messages.*;
import it.polimi.ingsw.Utils.Coordinates;
import it.polimi.ingsw.model.CommonCards.CardCommonTarget;
import it.polimi.ingsw.model.PersonalCards.CardPersonalTarget;
import it.polimi.ingsw.model.Tile.ColourTile;


import java.util.ArrayList;
import java.util.Scanner;

import static it.polimi.ingsw.view.cli.ColorCodes.getColorCode;

/**
 * this class represents a command-line interface (CLI) for the game client.
 */
public class Cli extends ClientManager {
    ArrayList<Coordinates> tempCoordinates;

    private String protocol;
    private ColourTile[][] shelf;
    private MyShelfiePrintStream out;
    private String username;
    private long UID;
    private int gameID;
    private CardPersonalTarget cardPersonalTarget;
    private ArrayList<CardCommonTarget> cardCommonTargets;
    private ColourTile[][] board;
    private final String ANSI_RESET = "\u001B[0m";
    private int[] commonTokens;
    private boolean endGameToken;
    private String[] colors;
    private final Scanner in;
    private static final String TEXT_BLACK = "\u001B[30m";
    private boolean emptyShelf;
    private int turn;
    private final ArrayList<String> chatList;

    /**
     * Constructs a CLI object with the specified scanner for user input.
     *
     * @param scanner the scanner object for user input.
     */
    public Cli(Scanner scanner) {
        super();
        in = scanner;
        chatList = new ArrayList<>();
    }

    private void updateChat(int type) {
        while (!Thread.currentThread().isInterrupted()) {
            for (String message: chatList){
                out.println(message);
            }
            chatMode(type);
            Thread.currentThread().interrupt();
        }
    }


    /**
     * Starts the CLI and prompts the user to choose a connection protocol (TCP or RMI).
     */
    public void start() {
        this.out = new MyShelfiePrintStream();
        out.println(("Welcome , choose your connection : "));
        protocol = in.next() + in.nextLine();
        while(true){
            if (!(protocol.equals("TCP") || protocol.equals("tcp") || protocol.equals("RMI") || protocol.equals("rmi"))) {
                out.println("Invalid selection, choose TCP or RMI");
                protocol = in.next() + in.nextLine();
            } else break;
        }

        out.println("Please enter the port: ");
        int port;
        boolean loop = true;
        while (true){
            try {
                port = Integer.parseInt(in.next() + in.nextLine());
                break;
            }catch (NumberFormatException e){
                out.println("Invalid digit, please enter a number: ");
            }
        }

        createConnection(protocol, port);
    }

    /**
     * Method for creating the connection
     * @param connection The type of connection ("TCP" or "RMI") to establish with the server.
     * @param port the port of the server
     */
    @Override
    public void createConnection(String connection, int port) {
        if (protocol.equals("TCP") || protocol.equals("tcp")) {
                super.createConnection("TCP", port);
        }
        if (protocol.equals("RMI") || protocol.equals("rmi")) {
            super.createConnection("RMI", port);
        }
        UID = getClient().getUID();
        login();
    }

    /**
     * Prompts the user to enter a username for login.
     */
    protected void login() {
        out.println("Choose your username\n");
        username = in.next() + in.nextLine();
        getClient().sendMessage(new PreLoginMessage(-1, UID, username));
        getClient().setUsername(username);
    }

    /**
     * Prompts the user to choose the number of players in the game.
     *
     * @param gameID the game ID.
     */
    public void firstSetter(int gameID) {
        getClient().setFirst();
        out.println("Choose number of players");
        int numPlayers = validInt(2,4);
        getClient().sendMessage(new SetMessage(numPlayers, gameID, UID));

    }

    /**
     * Called when a first response arrives, makes the player select the num of players
     * @param firstResponse The response to the first game turn.
     */

    @Override
    public void firstResponse(FirstResponse firstResponse) {
        gameID = firstResponse.getGameID();
        firstSetter(gameID);
    }

    /**
     * sent to a client that becomes a first player after a game is started
     * @param reFirstResponse The response to the re-first game turn.
     */
    @Override
    public void reFirstResponse(ReFirstResponse reFirstResponse) {
        gameID = reFirstResponse.getGameID();
        firstSetter(gameID);
    }


    /**
     * Confirms that the set of the players was successful
     * @param setResponse The response to the request for setting a value.
     */
    @Override
    public void setResponse(SetResponse setResponse) {
        out.println("Setting finished correctly\nWaiting...");

    }

    /**
     * Confirms that the PreLogin phase has ended successfully
     * @param preLoginResponse The preliminary response to the client's login attempt.
     */
    @Override
    public void preLoginResponse(PreLoginResponse preLoginResponse) {
        out.println("Waiting for a game...");

    }

    /**
     *  Confirms that the Login phase has ended successfully
     * @param loginResponse The response to the client's login attempt.
     */
    @Override
    public void loginResponse(LoginResponse loginResponse) {
        out.println("Successful login\nWaiting...");
        gameID = loginResponse.getGameID();
    }
    /**
     * Used when the chosen username is already taken in this game
     */
    @Override
    public void usernameError(UsernameError usernameError) {
        gameID = usernameError.getGameID();
        out.println("Username already exists\nPlease enter new one");
        username = in.next() + in.nextLine();
        getClient().sendMessage(new LoginMessage(username, gameID, UID));
        getClient().setUsername(username);
    }

    /**
     * called when a game begins
     * @param initResponse The response to the game initialization.
     */
    @Override
    public void initResponse(InitResponse initResponse) {

        turn=0;

        setNicknames(initResponse.getPlayers());

        out.println("The game is loading...");
        colors = new String[3];
        for (int i = 0; i < 3; i++) {
            colors[i] = ColourTile.FREE.toString();
        }
        emptyShelf = true;

    }

    @Override
    public void cardsResponse(CardsResponse cardsResponse) {
        this.cardCommonTargets = cardsResponse.getCommonTargets();
        this.cardPersonalTarget = cardsResponse.getCardPersonalTarget();

    }

    @Override
    public void wakeUp(WakeMessage wakeMessage) {
        for (int i = 0; i < 3; i++) {
            colors[i] = ColourTile.FREE.toString();
        }

        getClient().sendMessage(new BoardMessage(username, gameID, UID));
        Thread timerThread = new Thread(this::startTimer);
        timerThread.start();

    }

    /**
     * Starts a timer until the board is loaded.
     * Waits for the board to be initialized before proceeding.
     */
    public void startTimer() {
        while (board == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        removeChoice();
        Thread.currentThread().interrupt();

    }

    /**
     * Displays the remove option based on the remove parameter.
     *
     */
    public void displayRemove() {
        remove();
    }

    /**
     * Displays the turn option based on the turn parameter.
     *
     */
    public void displayTurn() {
        turn();
    }

    /**
     * Allows the user to make a turn choice.
     * Prompts the user to choose an action and continues until an exit condition is met.
     */
    public void turnChoice() {
        int status;
        status = display(false);
        while (status==0){
            status=display(false);
        }
        if (status==1)displayTurn();
    }

    /**
     * Allows the user to make a remove choice.
     * Prompts the user to choose an action and continues until an exit condition is met.
     */
    public void removeChoice() {
        int status;
        status = display(true);
        while (status==0){
           status=display(true);
        }
        if (status==1)displayRemove();

    }


    /**
     * Displays the menu options and handles the user's choice.
     * Returns 0 if the user wants to continue, 1 if the user wants to exit.
     *
     * @param type boolean
     * @return true to continue, false to exit.
     */
    public int display(boolean type) {
        out.println("What do you want to do?\n1: View common cards\n2: View personal card\n3: View board\n4: View endGameToken\n5: View shelf\n6: Chat");
        if (type) {
            out.println("7: Remove tiles from board");
        } else {
            out.println("7: Insert in column");
        }
        out.println("8: Exit");
        int num = validInt(0, 8);

        if (num == 1) {
            printCommonTargets(cardCommonTargets.get(0), cardCommonTargets.get(1));
            return 0;
        } else if (num == 2) {
            printPersonalTargets(cardPersonalTarget);
            return 0;
        } else if (num == 3) {
            printBoard(board);
            return 0;
        } else if (num == 4) {
            if (endGameToken) {
                out.println("EndGameToken already taken");
            } else {
                out.println("EndGameToken still in game");
            }
            return 0;
        } else if (num == 5) {
            printShelf(shelf);
            return 0;
        } else if (num == 6){

            if (type) chatMode(0);
            else chatMode(1);

            return 2;
        } else if (num==8) {
            out.println("Disconnected. Bye bye!");
            System.exit(0);
            return -1;

        } else return 1;
    }


    /**
     * Method for removing tiles from the shelf.
     */
    public void remove() {
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        int freeColumnSpace = 0;
        int count = 0;
        if(!emptyShelf){
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 6; j++) {
                    if (shelf[j][i] == ColourTile.FREE) {
                        count++;
                    }
                }
                if (count > freeColumnSpace) {
                    freeColumnSpace = count;
                }
                count = 0;
            }
        }else freeColumnSpace = 3;
        if(freeColumnSpace>3){
            freeColumnSpace = 3;
        }
        for (int i = 0; i < freeColumnSpace; i++) {
            if (i != 0) {
                out.println("Do you want to remove other cards?");
                String is = in.next() + in.nextLine();
                if (is.equals("no")) {
                    break;
                }
            }
            out.println("Insert the coordinates of the cards you want to remove, in order with respect to column insertion in the shelf :\ninsert row:\n");

            int x = validInt(0,10);
            out.println("Insert column:");
            int y = validInt(0,10);
            while (true) {
                System.out.println("Are you sure? Answer yes or no");
                String response = in.next() + in.nextLine();
                if (response.equals("no")) {
                    while(true){
                        out.println("Insert your new row");
                        x = validInt(0,10);
                        out.println("Insert your new column");
                        y = validInt(0,10);
                        System.out.println("Are you sure? Answer yes or no");
                        String secResponse = in.next() + in.nextLine();
                        if(secResponse.equals("yes")){
                            coordinates.add(new Coordinates(x,y));
                            break;
                        }
                    }
                    break;
                }
                else if (response.equals("yes")) {
                    coordinates.add(new Coordinates(x, y));
                    break;
                }
            }
            colors[i] = board[x][y].toString();
        }
        tempCoordinates = coordinates;
        getClient().sendMessage(new RemoveMessage(coordinates, gameID, UID, username));

    }


    @Override
    public void updateBoard(BoardResponse boardMessage) {
        turn++;
        if(turn==1) out.println("Game started! Wait for your turn...");

        board = boardMessage.getBoard();
        commonTokens = boardMessage.getCommonTokens();
        endGameToken = boardMessage.isEndGameToken();
    }

    /**
     * Executes a turn in the game by prompting the user to choose a column,
     * and sending a TurnMessage to the client with the chosen column
     */
    public void turn() {
        out.println("Choose the column:");
        int column = validInt(0,4);
        ArrayList<String> tempColors = new ArrayList<>();
        for (String color : colors) {
            if (!color.equals(ColourTile.FREE.toString())) {
                tempColors.add(color);
            }
        }
        String[] coloursTurn = new String[tempColors.size()];
        for (int i = 0; i < tempColors.size(); i++) {
            coloursTurn[i] = tempColors.get(i);
        }
        getClient().sendMessage(new TurnMessage(gameID, UID, column, username, coloursTurn));
    }

    @Override
    public void removeResponse(RemoveResponse removeResponse) {
        if (removeResponse.isInvalidSequence()) {
            out.println("The tile sequence you provided is invalid");
            remove();
            return;
        }
        out.println("Remove successful");
        for (Coordinates tempCoordinate : tempCoordinates) {
            board[tempCoordinate.getRow()][tempCoordinate.getColumn()] = ColourTile.FREE;
        }
        turnChoice();
    }

    @Override
    public void turnResponse(TurnResponse turnResponse) {
        if (turnResponse.getStatus() == -1) {
            out.println("The selected column has too many tiles");
            turn();
            return;
        }
        out.println("Insert tile successful");
        shelf = turnResponse.getShelf();
        emptyShelf = false;
        for (int i = 0; i < 3; i++) {
            colors[i] = ColourTile.FREE.toString();
        }
        chatMode(2);

    }

    @Override
    public void endGame(EndMessage endGameMessage) {
        out.println("The game is finished! \nThe winner is " + endGameMessage.getWinner()+"\n Your score is: "+endGameMessage.getPoints());
        System.exit(0);
    }

    /**
     * Prints the common targets of two CardCommonTarget cards
     *
     * @param cardCommonTarget0 The first CardCommonTarget card.
     * @param cardCommonTarget1 The second CardCommonTarget card.
     */
    public void printCommonTargets(CardCommonTarget cardCommonTarget0, CardCommonTarget cardCommonTarget1) {
        printCommon(cardCommonTarget0.getCommonType().getId(), 0);
        printCommon(cardCommonTarget1.getCommonType().getId(), 1);
    }

    /**
     * Prints the common target based on the provided ID.
     *
     * @param id The ID of the common target.
     * @param i  The index of the common target.
     */
    public void printCommon(int id, int i) {
        out.println("            Token: " + commonTokens[i] + "\n");
        switch (id) {
            case 4 -> out.println("""
                       ------------------------------
                       |           six               |
                       |         couples             |
                       |            of               |
                       |     the same colour         |
                       |  (each couple can be of     |
                       |     a different colour)     |
                       -------------------------------
                    """);
            case 8 -> out.println("""
                       ------------------------------
                       |           on                |
                       |          each               |
                       |          angles             |
                       |         must be             |
                       |       the same colour       |
                       |                             |
                       -------------------------------
                    """);
            case 3 -> out.println("""
                       ------------------------------
                       |          four               |
                       |         groups              |
                       |            of               |
                       |     the same colour         |
                       |  (each group can be of      |
                       |     a different colour)     |
                       -------------------------------
                    """);
            case 1 -> out.println("""
                       ------------------------------
                       |           two               |
                       |         groups of           |
                       |     the same colour         |
                       |  that create a square 2x2   |
                       |  (each group have to be of  |
                       |     the same colour)        |
                       -------------------------------
                    """);
            case 5 -> out.println("""
                       ------------------------------
                       |           three             |
                       |        columns of           |
                       |     one, two or three       |
                       |     different  colours      |
                       |  (each column can have      |
                       |    different colours)       |
                       -------------------------------
                    """);
            case 9 -> out.println("""
                       ------------------------------
                       |           eight             |
                       |         tails of            |
                       |     the same colour         |
                       |                             |
                       |  (the position is not       |
                       |          relevant)          |
                       -------------------------------
                    """);
            case 11 -> out.println("""
                       ------------------------------
                       |           five              |
                       |         tails of            |
                       |     the same colour         |
                       |      in diagonal            |
                       |                             |
                       |                             |
                       -------------------------------
                    """);
            case 7 -> out.println("""
                       ------------------------------
                       |           four              |
                       |        rows of              |
                       |     one, two or three       |
                       |     different  colours      |
                       |  (each row can have         |
                       |    different colours)       |
                       -------------------------------
                    """);
            case 2 -> out.println("""
                       ------------------------------
                       |           two               |
                       |        columns of           |
                       |         all six             |
                       |     different  colours      |
                       |                             |
                       |                             |
                       -------------------------------
                    """);
            case 6 -> out.println("""
                       ------------------------------
                       |           two               |
                       |          rows of            |
                       |          five               |
                       |     different  colours      |
                       |                             |
                       |                             |
                       -------------------------------
                    """);
            case 10 -> out.println("""
                    ------------------------------
                       |           five              |
                       |         tails of the        |
                       |        same colour          |
                       |         that create         |
                       |             a X             |
                       |                             |
                       -------------------------------
                    """);
            case 12 -> out.println("""
                       ------------------------------
                       |        five columns         |
                       |      in descending or       |
                       |      in growing  order      |
                       |      (each column have      |
                       |  one less tail or one more  |
                       |  tail of the previous one)  |
                       -------------------------------
                    """);
        }
    }


    /**
     * Prints the game board represented by a 2D array of ColourTile cards.
     *
     * @param colourTiles The 2D array of ColourTile cards representing the game board.
     */
    public void printBoard(ColourTile[][] colourTiles) {
        for (int i = 0; i < 11; i++) {
            System.out.print(" " + i + "  ");
        }
        System.out.print("\n");
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(getColorCode(colourTiles[i][j]) + "*** " + ANSI_RESET);
            }
            System.out.print(" " + i + "\n");
        }
    }

    /**
     * Prints the personal targets of a CardPersonalTarget card.
     *
     * @param cardPersonalTarget The CardPersonalTarget card.
     */
    public void printPersonalTargets(CardPersonalTarget cardPersonalTarget) {
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 5; j++) {
                int found = 0;
                for (int z = 0; z < 6; z++) {
                    if (cardPersonalTarget.personalCardTiles()[z].coordinates().getRow() == i &&
                            cardPersonalTarget.personalCardTiles()[z].coordinates().getColumn() == j) {
                        System.out.print(getColorCode(cardPersonalTarget.personalCardTiles()[z].colourTile()) + "*** " + ANSI_RESET);
                        found = 1;
                    }

                }
                if (found == 0) {
                    System.out.print(TEXT_BLACK + "*** " + ANSI_RESET);

                }

            }
            System.out.print("\n");

        }
    }

    /**
     * Prints the shelf represented by a 2D array of ColourTile cards.
     *
     * @param colourTiles The 2D array of ColourTile cards representing the shelf.
     */
    public void printShelf(ColourTile[][] colourTiles) {
        for(int i = 0; i < 5; i++) {
            out.print(" " + i + "  ");
        }
        out.print("\n");
        if(!emptyShelf){
            for (int i = 5; i >=0; i--) {
                for (int j = 0; j < 5; j++) {
                    System.out.print(getColorCode(colourTiles[i][j]) + "*** " + ANSI_RESET);
                }
                out.print("\n");
            }
        }else{

            for (int i = 5; i >=0; i--) {
                for (int j = 0; j < 5; j++) {
                    System.out.print(TEXT_BLACK + "*** " + ANSI_RESET);
                }
                out.print("\n");
        }

        }
    }
    /**
     * Validates and retrieves an integer input within the specified range.
     *
     * @param min The minimum allowed value.
     * @param max The maximum allowed value.
     * @return The validated integer input.
     */
    public int validInt(int min, int max){
        int input;
        while (true){
                try {
                    input = Integer.parseInt(in.next() + in.nextLine());
                    if (input<min || input> max) {
                        out.println("Invalid digit");
                    } else break;

            }catch (NumberFormatException e){
                    out.println("Please insert a number");
            }
        }
        return input;
    }


    @Override
    public void disconnectionMessage(DisconnectionMessage disconnectionMessage) {
        out.println("\nDisconnection occurred,game finished\n");
        closeConnection();
        System.exit(0);
    }

    @Override
    public void chatMessage(ChatMessage chatMessage) {
        chatList.add(chatMessage.getMessage());

    }

    public void sendChatMessage(String message, String nickname){
        getClient().sendMessage(new ChatMessage(gameID, UID,getClient().getUsername()+ " : " + message, nickname));
    }

    public void chatMode(int type){
        while (true){
            out.println("1: View chat\n2: Send private message\n3: Send broadcast message\n4: Exit from chatMode");
            int choice =validInt(1,4);
            boolean exit = false;

            switch (choice) {
                case 1 -> {

                    Thread chatView=new Thread(()->updateChat(type));
                    chatView.start();
                    return;

                }
                case 2 -> {
                    out.println("Choose the player please : ");
                    for(int i = 0; i < getPlayers().size(); i++){
                        if (getPlayers().get(i).equals(getClient().getUsername())){
                            getPlayers().remove(i);
                            break;
                        }
                    }
                    for (int i = 0; i < getPlayers().size(); i++) {
                        out.println(i + " : " + getPlayers().get(i));
                    }
                    String privateNick = getPlayers().get(validInt(0, getPlayers().size()-1));
                    out.println("Please enter the message you wanna send : ");
                    String message = in.next() + in.nextLine();
                    sendChatMessage( " whispers to " + privateNick + " ' " + message+ " ' ", privateNick);
                }
                case 3 -> {
                    out.println("Please enter the message you wanna send : ");
                    String message = in.next() + in.nextLine();
                    sendChatMessage(message, null);
                }
                case 4 -> {
                    exit = true;

                    if (type!=2){
                        if (type==0) removeChoice();
                        else turnChoice();
                    }
                }
            }
            if(exit)break;
        }
    }


}




