package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Network.Client.Client;
import it.polimi.ingsw.Network.Messages.*;
import it.polimi.ingsw.Utils.Coordinates;
import it.polimi.ingsw.model.CommonCards.CardCommonTarget;
import it.polimi.ingsw.model.PersonalCards.CardPersonalTarget;
import it.polimi.ingsw.model.Tile.ColourTile;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Controller that manages the Game scene in all aspects
 */
public class GameSceneController {
    private static final int SHELF_ROWS = 6;
    private static final int SHELF_COLUMNS = 5;
    private static final int BOARD_SIZE = 11;
    @FXML
    public AnchorPane rootPane;
    @FXML
    public ImageView board;
    @FXML
    public ImageView shelf;
    @FXML
    public ListView<String> chatListView;
    @FXML
    public TextField messageTextField;
    @FXML
    public GridPane boardMask;
    @FXML
    public GridPane shelfMask;
    @FXML
    public Label tileError;

    @FXML
    public GridPane hand;
    @FXML
    public GridPane personalCard;
    @FXML
    public GridPane commonTarget2;
    @FXML
    public GridPane commonTarget1;
    @FXML
    public GridPane scoringToken1;
    @FXML
    public GridPane scoringToken2;
    @FXML
    public Label turnText;
    @FXML
    public Button sendHandButton;
    @FXML
    public Label box1;
    @FXML
    public Label box2;
    @FXML
    public Label box3;
    public Label[] boxArray;

    public Button[] columnArray;
    @FXML
    public Button column1;
    @FXML
    public Button column2;
    @FXML
    public Button column3;
    @FXML
    public Button column4;
    @FXML
    public Button column5;
    public GridPane endGameToken;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public Button privateMessageButton;
    @FXML
    public RadioButton firstPlayer;
    @FXML
    public RadioButton secondPlayer;
    @FXML
    public RadioButton thirdPlayer;
    public Label selectedUser;
    public Button closeButton;
    private ColourTile[][] turnBoard;
    private final ArrayList<Coordinates> tileHand=new ArrayList<>();
    private final ArrayList<Coordinates> tileHandTmp=new ArrayList<>();
    @FXML
    private Button remakeTurnButton;

    private GuiMaster guiMaster;
    private ObservableList<String> messages;
    private ToggleGroup playerToggle;

    public ToggleButton[] toggleButtons;
    @FXML
    private Button privateSelectButton;
    private RadioButton selectedForPrivateMessage;
    @FXML
    private Label winnerLabel;


    /**
     * Method to initialize the scene, also setting the controller in the Gui Master
     */
    @FXML
    public void initialize() {

        guiMaster = GuiMaster.getInstance();
        guiMaster.setGameSceneController(this);

        playerToggle =new ToggleGroup();

        firstPlayer.setToggleGroup(playerToggle);
        secondPlayer.setToggleGroup(playerToggle);
        thirdPlayer.setToggleGroup(playerToggle);

        toggleButtons=new ToggleButton[]{firstPlayer,secondPlayer,thirdPlayer};

        messages = FXCollections.observableArrayList();
        chatListView.setItems(messages);
        
        board.fitWidthProperty().bind(boardMask.widthProperty());
        boxArray=new Label[]{box1, box2, box3};
        columnArray=new Button[]{column1,column2,column3,column4,column5};

        String boardImage = Objects.requireNonNull(getClass().getResource("/assets/boards/livingroom.png")).toExternalForm();
        board.setImage(new Image(boardImage));

        String backgroundImage = Objects.requireNonNull(getClass().getResource("/assets/misc/sfondo_parquet.jpg")).toExternalForm();
        anchorPane.setStyle("-fx-background-image: url('" + backgroundImage + "'); -fx-background-size: cover;");
        
        turnText.setVisible(false);
        disableGUI();

    }
    public void sendChatMessage(ActionEvent actionEvent) {
        Client client=guiMaster.getClient();
        if (selectedForPrivateMessage!=null){

            String privateUser=selectedForPrivateMessage.getText();
            String message= client.getUsername()+ " whispers to "+ privateUser+ " : " +messageTextField.getText();
            client.sendMessage(new ChatMessage(client.getGameID(), client.getUID(),message,privateUser));

            privateMessageButton.setVisible(true);
            privateSelectButton.setVisible(false);
            selectedUser.setText("");
            playerToggle.getSelectedToggle().setSelected(false);

            for (ToggleButton toggleButton:toggleButtons){
                if (toggleButton.isVisible()) toggleButton.setVisible(false);
            }

            selectedForPrivateMessage=null;
        }else {
            String message= client.getUsername()+ " : " +messageTextField.getText();
            client.sendMessage(new ChatMessage(client.getGameID(), client.getUID(),message,null));
        }

        messageTextField.setText("");
    }

    /**
     * Method that is invoked when a BoardResponse arrives,updating the board to the previous turn
     * and reloading the scoring tokens on the common card
     * @param boardMessage a Message that contains information about the board and the tokens
     */
    public void updateBoard(BoardResponse boardMessage) {
        int[] tokens = boardMessage.getCommonTokens();
        loadScoringToken(tokens[0],scoringToken1);
        loadScoringToken(tokens[1],scoringToken2);
        if (boardMessage.isEndGameTokenTaken()) emptyGridPane(endGameToken);
        else loadEndGameToken();
        turnBoard = boardMessage.getBoard();
        createBoard(turnBoard);
    }

    /**
     * Method used to create the tiles of the board and adding them to the board
     * @param turnBoard the board in the state of the current turn
     */
    private void createBoard(ColourTile[][] turnBoard) {
        emptyGridPane(boardMask);
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (!turnBoard[row][col].equals(ColourTile.FREE)) {
                    ImageView tile = createBoardTile(turnBoard[row][col],row,col);
                    boardMask.add(tile, col, row);
                }
            }
        }
    }

    /**
     * method to load the image of the end game token
     */
    private void loadEndGameToken() {
        String path="/assets/scoring tokens/end game.jpg";
        String EndGameTokenImage = Objects.requireNonNull(getClass().getResource(path)).toExternalForm();
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(EndGameTokenImage));
        imageView.setFitWidth(35);
        imageView.setFitHeight(35);

        endGameToken.add(imageView,0,0);
    }

    /**
     * Method to create a single tile to put on the board, also adding the appropriate Event Listener
     * @param colourTile the colour of the tile
     * @param row the row of the tile on the board
     * @param col the column of the tile on the board
     * @return the ImageView to add to the board
     */
    private ImageView createBoardTile(ColourTile colourTile, int row, int col) {
        String path = "/assets/item tiles/";
        switch (colourTile){

            case CATS -> path+="Gatti1.1.png";
            case BOOKS -> path+="Libri1.1.png";
            case GAMES -> path+="Giochi1.1.png";
            case FRAMES -> path+="Cornici1.1.png";
            case TROPHIES -> path+="Trofei1.1.png";
            case PLANTS -> path+="Piante1.1.png";

        }
        String TileImage = Objects.requireNonNull(getClass().getResource(path)).toExternalForm();
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(TileImage));

        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        String finalPath = path;
        imageView.setOnMouseClicked(event -> ChooseTile(row,col, finalPath));
        return imageView;

    }

    /**
     * Listener added to every tile on the Board,that adds the tile to the hand,
     * checking also if the selected tile is valid
     * @param row  the row of the tile on the board
     * @param col the column of the tile on the board
     * @param finalPath path used to create the tile in the hand with consistency
     */
    private void ChooseTile(int row, int col, String finalPath) {
        ImageView tile = findTile(row, col);
        Coordinates newTile=new Coordinates(row, col);
        if (tileHandTmp.size()<getMaxEmptyBoxesInColumns(shelfMask)){
            if ((turnBoard[row + 1][col].equals(ColourTile.FREE)) || (turnBoard[row - 1][col].equals(ColourTile.FREE)) || (turnBoard[row][col + 1].equals(ColourTile.FREE)) || (turnBoard[row][col - 1].equals(ColourTile.FREE))) {
                if (tileHandTmp.isEmpty()) {
                    tileHandTmp.add(newTile);
                    tile.setImage(null);
                    ImageView handTile = createHandTile(finalPath);
                    hand.add(handTile, findFirstEmptyColumn(hand), 0);

                } else if (tileHandTmp.size()==3) {

                } else {

                    int lastCoordinateIndex = tileHandTmp.size() - 1;
                    Coordinates lastCoordinate = tileHandTmp.get(lastCoordinateIndex);
                    int lastRow = lastCoordinate.getRow();
                    int lastCol = lastCoordinate.getColumn();

                    boolean hasSameRow = (row == lastRow);
                    boolean hasSameCol = (col == lastCol);
                    boolean isAdjacent = false;

                    for (Coordinates coordinate : tileHandTmp) {
                        if (row != coordinate.getRow()) {
                            hasSameRow = false;
                        }
                        if (col != coordinate.getColumn()) {
                            hasSameCol = false;
                        }
                    }

                    for (Coordinates coordinate : tileHandTmp) {
                        int coordinateRow = coordinate.getRow();
                        int coordinateCol = coordinate.getColumn();

                        if ((row == coordinateRow && (col == coordinateCol + 1 || col == coordinateCol - 1)) ||
                                (col == coordinateCol && (row == coordinateRow + 1 || row == coordinateRow - 1))) {
                            isAdjacent = true;
                            break;
                        }
                    }

                    if ((hasSameRow || hasSameCol) && isAdjacent) {
                        tileHandTmp.add(newTile);
                        tile.setImage(null);
                        ImageView handTile = createHandTile(finalPath);
                        hand.add(handTile, findFirstEmptyColumn(hand), 0);
                    }
                }
            }
        }

    }


    /**
     * @param hand the Temporary hand of the player
     * @return the int that represents the first empty column of the hand where to place the tile
     */
    private int findFirstEmptyColumn(GridPane hand) {
        int numColumns = 3;

        for (int column = 0; column < numColumns; column++) {
            if (isColumnEmpty(column,hand)) {
                return column;
            }
        }
        return -1;
    }

    /**
     * method to check if the column passed is empty
     * @param column the column to check
     * @param hand the hand of the player
     * @return if the column is empty or not
     */
    private boolean isColumnEmpty(int column, GridPane hand) {
        for (javafx.scene.Node node : hand.getChildren()) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            if (columnIndex != null && columnIndex == column) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method that creates a Tile to place in the Temporary Hand, adding also the listener
     * @param finalPath path to the image
     * @return the ImageView that represents the tile
     */
    private ImageView createHandTile(String finalPath) {
        String TileImage = Objects.requireNonNull(getClass().getResource(finalPath)).toExternalForm();
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(TileImage));

        imageView.setFitWidth(45);
        imageView.setFitHeight(45);

        int pos=tileHandTmp.size()-1;

        imageView.setOnMouseClicked(event ->addToHand(pos));

        return imageView;
    }

    /**
     * Method to add the tiles in the Temporary hand to the Hand,
     * permitting to the player to choose in which order to place the shelf
     * @param i the position where to add the tile
     */
    private void addToHand(int i) {
        if ((tileHand.size()<tileHandTmp.size())&&!isTileAlreadyPresent(i)) {
            Coordinates coordinates = tileHandTmp.get(i);
            tileHand.add(coordinates);
            Label label = boxArray[i];
            label.setText(String.valueOf(tileHand.size()));
            if (tileHand.size() == tileHandTmp.size()) sendHandButton.setVisible(true);
        }
    }

    /**
     * methods that checks if a tile is already present in the hand
     * @param i the index of the coordinates to add
     */
    private boolean isTileAlreadyPresent(int i) {
        for (Coordinates coordinates:tileHand){
            if (tileHand.contains(tileHandTmp.get(i))) return true;
        }
        return false;
    }


    /**
     * method to find a specific image view in the board
     * @param row the row
     * @param col the column
     * @return the Image View
     */
    private ImageView findTile(int row, int col) {

        ImageView targetImageView = null;
        for (javafx.scene.Node node : boardMask.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                if (node instanceof ImageView) {
                    targetImageView = (ImageView) node;
                    break;
                }
            }
        }
        return targetImageView;
    }

    /**
     * Method invoked when a removeResponse arrives,
     * making the buttons of the selection of the column disappear
     * @param removeResponse a message that confirms that the hand is valid
     */
    public void removeResponse(RemoveResponse removeResponse) {

       for (int i=0;i<columnArray.length;i++){
           if (countEmptyBoxesInColumn(shelfMask,i)>=tileHand.size()) {
               columnArray[i].setVisible(true);
           }
       }
    }

    /**
     * Method to count the number of empty spaces on a column in a gridpane
     */
    public int countEmptyBoxesInColumn(GridPane gridPane, int columnIndex) {
        int emptyBoxCount = 0;

        for (int row = 0; row < gridPane.getRowCount(); row++) {
            Node node = getNodeFromGridPane(gridPane, columnIndex, row);
            if (node == null) {
                emptyBoxCount++;
            }
        }

        return emptyBoxCount;
    }

    /**
     * Method to get a specific gridPane node from a grid pane
     */
    private Node getNodeFromGridPane(GridPane gridPane, int columnIndex, int rowIndex) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == columnIndex && GridPane.getRowIndex(node) == rowIndex) {
                return node;
            }
        }
        return null;
    }


    /**
     * Method to get the max value of empty spaces in the shelf among all the columns
     */
    public int getMaxEmptyBoxesInColumns(GridPane gridPane) {
        int maxEmptyBoxCount = 0;

        for (int columnIndex = 0; columnIndex < gridPane.getColumnCount(); columnIndex++) {
            int emptyBoxCount = countEmptyBoxesInColumn(gridPane, columnIndex);
            if (emptyBoxCount > maxEmptyBoxCount) {
                maxEmptyBoxCount = emptyBoxCount;
            }
        }

        return maxEmptyBoxCount;
    }

    /**
     * Method that ends a player Turn, disabling the Gui to prevent the sending of messages or makes the player remake the turn
     * @param turnResponse a message that tells if the insert in the shelf of the player is valid or not
     *                     it also contains the update shelf after the insert of the new tiles
     */
    public void turnResponse(TurnResponse turnResponse) {
        if (turnResponse.getStatus()==0) {
            tileError.setVisible(false);
            updateShelf(turnResponse);
            disableGUI();

        }
    }

    /**
     * Method to update the shelf after the turn ends
     */
    private void updateShelf(TurnResponse turnResponse) {
        ColourTile[][] turnShelf = turnResponse.getShelf();
        emptyGridPane(shelfMask);
        for (int row = SHELF_ROWS - 1; row >= 0; row--) {
            for (int col = 0; col < SHELF_COLUMNS; col++) {
                int adjustedRow = SHELF_ROWS - 1 - row;

                if (!turnShelf[adjustedRow][col].equals(ColourTile.FREE)) {
                    ImageView tile = createShelfTile(turnShelf[adjustedRow][col]);
                    shelfMask.add(tile, col, row);
                }
            }
        }
    }


    /**
     * method to empty a gridPane like the shelf or board of all the Image Views
     * @param gridPane the pane to empty
     */
    private void emptyGridPane(GridPane gridPane) {
        gridPane.getChildren().clear();
    }

    /**
     * Method that creates a tile for the shelf
     * @param colourTile the color of the tile
     * @return the imageView to add
     */
    private ImageView createShelfTile(ColourTile colourTile) {
        String path = "/assets/item tiles/";
        switch (colourTile){

            case CATS -> path+="Gatti1.1.png";
            case BOOKS -> path+="Libri1.1.png";
            case GAMES -> path+="Giochi1.1.png";
            case FRAMES -> path+="Cornici1.1.png";
            case TROPHIES -> path+="Trofei1.1.png";
            case PLANTS -> path+="Piante1.1.png";

        }
        String TileImage = Objects.requireNonNull(getClass().getResource(path)).toExternalForm();
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(TileImage));

        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        return imageView;
    }

    /**
     * Method that manages the end of the game, showing the winner
     * @param endGameMessage a Message that contains information about the winner of the game
     */
    public void endGame(EndMessage endGameMessage) {
        turnText.setVisible(true);
        turnText.setText("PARTITA FINITA");
        winnerLabel.setVisible(true);
        winnerLabel.setText("The winner is: " + endGameMessage.getWinner()+"\nYour score: "+endGameMessage.getPoints());
        guiMaster.closeConnection();

        closeButton.setVisible(true);
        closeButton.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });

    }


    /**
     * Method that starts a player turn, invoked when a wakeMessage arrives,and sends a BoardMessage to request the board
     * @param wakeMessage a message that wakes up the player when it is its turn
     */
    public void wakeUp(WakeMessage wakeMessage) {
        enableGUI();
        Client client=GuiMaster.getInstance().getClient();
        client.sendMessage(new BoardMessage(client.getUsername(), client.getGameID(), client.getUID()));
    }

    /**
     * Method to enable aspects of the GUI
     */
    private void enableGUI() {
        turnText.setVisible(false);
        boardMask.setDisable(false);
        shelfMask.setDisable(false);
        hand.setDisable(false);
        remakeTurnButton.setVisible(true);
    }

    /**
     * Method to disable aspects of the GUI
     */
    private void disableGUI() {
        turnText.setVisible(true);
        boardMask.setDisable(true);
        shelfMask.setDisable(true);
        hand.setDisable(true);
        tileHand.clear();
        emptyGridPane(hand);
        for (Label box:boxArray) {
            box.setText("");
        }
        tileError.setVisible(false);
    }

    /**
     * Method invoked when a CardResponse arrives, makes the card visible to the player
     * @param cardsResponse a message that contains information about the common and personal cards
     */
    public void cardsResponse(CardsResponse cardsResponse) {
        CardPersonalTarget personalTarget = cardsResponse.getCardPersonalTarget();
        showPersonalTarget(personalTarget);
        ArrayList<CardCommonTarget> commonTargets = cardsResponse.getCommonTargets();
        showCommonTargets(commonTargets);
    }

    /**
     * Method to display the common card on the GUI
     * @param commonTargets the List of cards
     */
    private void showCommonTargets(ArrayList<CardCommonTarget> commonTargets) {
        String path1= "/assets/commonGoalCards/" +commonTargets.get(0).getCommonType().getId()+".jpg";
        loadCommonTargetImage(path1, commonTarget1);


        String path2= "/assets/commonGoalCards/" +commonTargets.get(1).getCommonType().getId()+".jpg";
        loadCommonTargetImage(path2, commonTarget2);

    }

    /**
     * Method to display the scoring tokens on the common Cards
     * @param highestToken the value of the highest token of the stack
     * @param scoringToken the grid pane where to load the token
     */
    private void loadScoringToken(int highestToken, GridPane scoringToken) {
        String path="/assets/scoring tokens/scoring_"+ highestToken +".jpg";
        String tokenImage = Objects.requireNonNull(getClass().getResource(path)).toExternalForm();
        ImageView imageView = new ImageView();
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setImage(new Image(tokenImage));
        scoringToken.add(imageView,0,0);
    }

    /**
     * Method to retrieve the common card image and fitting it in the imageView
     * @param path the path of the image
     * @param commonTarget the grid pane where to load the card
     */
    private void loadCommonTargetImage(String path, GridPane commonTarget) {
        String cardImage = Objects.requireNonNull(getClass().getResource(path)).toExternalForm();
        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(100);
        imageView.setImage(new Image(cardImage));
        commonTarget.add(imageView,0,0);
    }

    /**
     * Method to display the personal card on the GUI
     * @param personalTarget the card
     */
    private void showPersonalTarget(CardPersonalTarget personalTarget) {
        String path = "/assets/personal goal cards/Personal_Goals" + personalTarget.id() + ".png";
        String cardImage = Objects.requireNonNull(getClass().getResource(path)).toExternalForm();
        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(200);
        imageView.setImage(new Image(cardImage));
        personalCard.add(imageView,0,0);
    }

    /**
     * Method to send the hand of the player to the Server, for checking if it's valid
     */
    public void sendHand(ActionEvent actionEvent) {
        remakeTurnButton.setVisible(false);
        sendHandButton.setVisible(false);
        tileHandTmp.clear();
        Client client = GuiMaster.getInstance().getClient();
        client.sendMessage(new RemoveMessage(tileHand, client.getGameID(), client.getUID(), client.getUsername()));
    }

    /**
     * Method to send the TurnMessage to the server, that contains information
     * about the order in which to place the tiles, and the column in which to add them
     */
    public void sendTurn(MouseEvent mouseEvent) {
        column1.setVisible(false);
        column2.setVisible(false);
        column3.setVisible(false);
        column4.setVisible(false);
        column5.setVisible(false);

        Button clickedButton = (Button) mouseEvent.getSource();
        String buttonText = clickedButton.getText();
        int column = Integer.parseInt(buttonText);
        column=column-1;

        Client client = GuiMaster.getInstance().getClient();
        String[] colours=createColoursArray();
        client.sendMessage(new TurnMessage(client.getGameID(), client.getUID(), column, client.getUsername(), colours));
    }

    /**
     * @return the array of colour of the tiles based on the hand of the player
     */
    private String[] createColoursArray() {
        String[] colours=new String[tileHand.size()];

        for (int i=0;i<colours.length;i++){
            colours[i]= String.valueOf(turnBoard[tileHand.get(i).getRow()][tileHand.get(i).getColumn()]);
        }

        return colours;
    }

    /**
     * Method invoked when the reset button is pressed,resetting the current turn.
     */
    public void remakeTurn(MouseEvent mouseEvent) {
        emptyGridPane(hand);
        tileHandTmp.clear();
        tileHand.clear();
        for (Label box:boxArray) {
            box.setText("");
        }
        createBoard(turnBoard);
    }

    public void disconnectionMessage(DisconnectionMessage disconnectionMessage) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Disconnection");
        Button closeButton = null;
        Label errorLabel = null;
        if (disconnectionMessage.isServerError()) {
            errorLabel = new Label("Server down, game finished");

            closeButton = new Button("Close Game");
            Button finalCloseButton = closeButton;

            closeButton.setOnAction(event -> {
                modalStage.close();

                Platform.exit();
            });

        } else {
            errorLabel = new Label("Someone disconnected, game finished");

            closeButton = new Button("Close Game");
            closeButton.setOnAction(event -> {
                guiMaster.closeConnection();
                modalStage.close();

                Platform.exit();
            });
        }

        VBox modalVBox = new VBox(errorLabel, closeButton);
        modalVBox.setStyle("-fx-padding: 20px; -fx-spacing: 10px");

        Scene modalScene = new Scene(modalVBox);

        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }

    public void receivedChatMessage(ChatMessage chatMessage) {
        messages.add(chatMessage.getMessage());
        chatListView.scrollTo(messages.size() - 1);
    }

    public void makeUsersAppear(MouseEvent mouseEvent) {
        for (int i=0;i<guiMaster.getPlayers().size();i++){
            if (!guiMaster.getPlayers().get(i).equals(guiMaster.getClient().getUsername())){
                int pos=findFirstEmptyPosition(toggleButtons);
                toggleButtons[pos].setText(guiMaster.getPlayers().get(i));
                toggleButtons[pos].setVisible(true);
            }

        }
        privateMessageButton.setVisible(false);
        privateSelectButton.setVisible(true);
    }
    public int findFirstEmptyPosition(ToggleButton[] array) {
        for (int i = 0; i < array.length; i++) {
            if (!array[i].isVisible()) {
                return i;
            }
        }
        return -1;
    }
    public void selectUser(MouseEvent mouseEvent) {
        selectedForPrivateMessage = (RadioButton) playerToggle.getSelectedToggle();
        selectedUser.setText("you selected: "+selectedForPrivateMessage.getText());
    }
}
