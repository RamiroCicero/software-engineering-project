package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Network.Client.Client;
import it.polimi.ingsw.Network.Messages.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Controller that manages the waiting scene, where the players
 * wait that the game starts
 */
public class ConnectionSceneController {
    @FXML
    public AnchorPane rootPane;
    @FXML
    public Label loadingMessage;
    @FXML
    public ProgressIndicator progressIndicator;
    @FXML
    public AnchorPane anchorPane;
    private GuiMaster guiMaster;
    @FXML
    private TextField usernameField;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button reSendButton;
    @FXML
    private Label rimettiUsername;

    /**
     * Method to initialize the scene, also setting the controller in the Gui Master
     */
    @FXML
    public void initialize() {
        guiMaster = GuiMaster.getInstance();
        guiMaster.setConnectionSceneController(this);
        createScene();
    }

    /**
     * Method that creates the scene
     */
    public void createScene() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        double stageWidth = screenWidth * 0.8;
        double stageHeight = screenHeight * 0.8;

        rootPane.setPrefWidth(stageWidth);
        rootPane.setPrefHeight(stageHeight);
        
        String backgroundImage = Objects.requireNonNull(getClass().getResource("/assets/misc/sfondo_parquet.jpg")).toExternalForm();
        anchorPane.setStyle("-fx-background-image: url('" + backgroundImage + "'); -fx-background-size: cover;");
        loadingMessage.setText("Wait for other Players");
    }


    /**
     * Method invoked when the initResponse arrives,changing to the game scene
     * @param initResponse a Message that confirms the init of the game
     */
    public void initResponse(InitResponse initResponse) {
        guiMaster.setNicknames(initResponse.getPlayers());
        Scene scene=rootPane.getScene();
        GuiMaster.setLayout(scene,"/fxml/gameScene.fxml");
    }

    /**
     * Method invoked when the loginResponse arrives,setting the GameID for future communication
     * @param loginResponse a Message that confirms the successful login
     */
    public void loginResponse(LoginResponse loginResponse) {
        guiMaster.getClient().setGameID(loginResponse.getGameID());
    }

    /**
     * Method invoked when the UsernameError arrives, making the player resend a new username
     * @param usernameError a Message that warns the client of a Username already taken
     */
    public void usernameError(UsernameError usernameError) {
        guiMaster.getClient().setGameID(usernameError.getGameID());
        for (Node node : rootPane.getChildren()) {
            if (node.isVisible()) {
                node.setVisible(false);
            }
        }
        usernameLabel.setVisible(true);
        usernameField.setVisible(true);
        reSendButton.setVisible(true);
        rimettiUsername.setVisible(true);
        rimettiUsername.setText("Username already taken!");
    }

    /**
     * Method that makes the client re-set the username if a usernameError occurs
     */
    public void reSendUsername(MouseEvent mouseEvent) {

        String username = usernameField.getText();

        if (username == null || username.trim().isEmpty()) {
            rimettiUsername.setText("Insert a Username!");
        } else {
            Client client = guiMaster.getClient();
            client.setUsername(username);
            client.sendMessage(new LoginMessage(username,client.getGameID(),client.getUID()));
        }
    }

    /**
     * Method invoked when a reFirstResponse arrives, telling the client that it's a first Player of a new Game
     * @param reFirstResponse a Message to warn the Client that he is a new first player, changing the scene to the selection of the number of players
     */
    public void reFirstResponse(ReFirstResponse reFirstResponse) {
        Scene scene=rootPane.getScene();
        GuiMaster.setLayout(scene,"/fxml/firstConnectionScene.fxml");
    }

    public void disconnectionMessage(DisconnectionMessage disconnectionMessage) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Disconnection");
        Button closeButton;
        Label errorLabel;
        if (disconnectionMessage.isServerError()) {
            errorLabel = new Label("Server down, game finished");

            closeButton = new Button("Close Game");

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
}
