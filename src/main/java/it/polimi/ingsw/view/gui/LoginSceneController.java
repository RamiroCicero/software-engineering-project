package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Network.Client.Client;
import it.polimi.ingsw.Network.Messages.*;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;

import java.util.Objects;


/**
 * Controller that manages the login of the player
 */

public class LoginSceneController {
    public GridPane gridPane;
    public Button communication;
    public Button loginButton;
    public Label usernameLabel;
    public GridPane ShelfieLogo;
    @FXML
    public AnchorPane anchorPane;
    public TextField portField;
    @FXML
    public Label portLabel;
    @FXML
    public Button portButton;

    @FXML
    private TextField usernameField;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private RadioButton TCP;
    @FXML
    private RadioButton RMI;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private Label protocolError;
    @FXML
    private Label usernameError;
    private String connection;
    private String username;
    private GuiMaster guiMaster;

    /**
     * Method to initialize the scene, also setting the controller in the Gui Master
     */
    @FXML
    public void initialize() {
        guiMaster = GuiMaster.getInstance();
        guiMaster.setLoginSceneController(this);
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

        gridPane.setPrefWidth(rootPane.getWidth()*0.4);
        gridPane.setPrefHeight(rootPane.getHeight()*0.4);



        imageView.fitWidthProperty().bind(ShelfieLogo.widthProperty());
        imageView.fitHeightProperty().bind(ShelfieLogo.heightProperty());
        String backgroundImage = Objects.requireNonNull(getClass().getResource("/assets/misc/sfondo_parquet.jpg")).toExternalForm();
        anchorPane.setStyle("-fx-background-image: url('" + backgroundImage + "'); -fx-background-size: cover;");

        usernameField.setVisible(false);
        usernameLabel.setVisible(false);

        toggleGroup=new ToggleGroup();
        RMI.setToggleGroup(toggleGroup);
        TCP.setToggleGroup(toggleGroup);
    }


    /**
     * Method that retrieves the inputted username and sends a PreLoginMessage to the Server
     */
    public void login() {
        username = usernameField.getText();
        if (username == null || username.trim().isEmpty()) {
            usernameError.setText("Insert a Username!");
        } else {
            Client client = guiMaster.getClient();
            client.setUsername(username);
            client.sendMessage(new PreLoginMessage(-1,client.getUID(),username));
        }
    }


    /**
     * Method to retrieve the chosen protocol and initialize the connection
     */
    public void communicationChoice(MouseEvent mouseEvent) {

        RadioButton selected = (RadioButton) toggleGroup.getSelectedToggle();

        if (!RMI.isSelected() && !TCP.isSelected()) {
            protocolError.setText("Choose a protocol!");
        }

        connection = selected.getText();

        communication.setVisible(false);
        TCP.setVisible(false);
        RMI.setVisible(false);
        portField.setVisible(true);
        portButton.setVisible(true);
        portLabel.setVisible(true);

    }

    /**
     * Method invoked when a firstResponse arrives, that tells the client that it is a first Player
     */
    public void firstResponse(FirstResponse firstResponse) {
        GuiMaster.getInstance().getClient().setFirst();
        GuiMaster.getInstance().getClient().setGameID(firstResponse.getGameID());
        Scene scene=gridPane.getScene();
        GuiMaster.setLayout(scene, "/fxml/firstConnectionScene.fxml");
    }

    /**
     * Method invoked when a PreLoginResponse arrives, that moves the client to the waiting screen
     */
    public void preLoginResponse(PreLoginResponse preLoginResponse) {
        Scene scene=gridPane.getScene();
        GuiMaster.setLayout(scene, "/fxml/connectionScene.fxml");
    }

    public void portChoice(MouseEvent mouseEvent) {
        try {
            int port = Integer.parseInt(portField.getText());

            portField.setVisible(false);
            portButton.setVisible(false);
            portLabel.setVisible(false);
            loginButton.setVisible(true);
            usernameField.setVisible(true);
            usernameLabel.setVisible(true);

            guiMaster.createConnection(connection,port);
        }catch (NumberFormatException e){
            usernameError.setText("Inserisci una porta!");
        }


    }
}

