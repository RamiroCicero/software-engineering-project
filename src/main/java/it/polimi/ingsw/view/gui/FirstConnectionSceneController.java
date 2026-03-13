package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Network.Client.Client;
import it.polimi.ingsw.Network.Messages.DisconnectionMessage;
import it.polimi.ingsw.Network.Messages.SetMessage;
import it.polimi.ingsw.Network.Messages.SetResponse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Controller that manages the connection of the first player of every game
 */
public class FirstConnectionSceneController {

    public RadioButton TwoPlayers;
    public RadioButton ThreePlayers;
    public RadioButton FourPlayers;
    public AnchorPane rootPane;
    public GridPane gridPane;
    public Label numError;
    @FXML
    public AnchorPane anchorPane;
    private ToggleGroup toggleGroup;
    private GuiMaster guiMaster;

    /**
     * Method to initialize the scene, also setting the controller in the Gui Master
     */
    @FXML
    public void initialize() {
        guiMaster = GuiMaster.getInstance();
        guiMaster.setFirstConnectionSceneController(this);
        createScene();
    }

    /**
     * Method that creates the scene
     */
    public void createScene() {
        String backgroundImage = Objects.requireNonNull(getClass().getResource("/assets/misc/sfondo_parquet.jpg")).toExternalForm();
        anchorPane.setStyle("-fx-background-image: url('" + backgroundImage + "'); -fx-background-size: cover;");

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(columnConstraints);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS);
        gridPane.getRowConstraints().add(rowConstraints);

        toggleGroup=new ToggleGroup();
        TwoPlayers.setToggleGroup(toggleGroup);
        ThreePlayers.setToggleGroup(toggleGroup);
        FourPlayers.setToggleGroup(toggleGroup);

    }

    /**
     * Method invoked when the player selects a number of players
     */
    public void selectNumOfPlayers() {
        RadioButton selected = (RadioButton) toggleGroup.getSelectedToggle();
        if (!TwoPlayers.isSelected() && !ThreePlayers.isSelected()&& !FourPlayers.isSelected()) {
            numError.setText("Choose a Number!");
        }
        else {
            int numOfPlayers = Integer.parseInt(selected.getText());
            Client client = guiMaster.getClient();
            client.sendMessage(new SetMessage(numOfPlayers, client.getGameID(),client.getUID()));
        }
    }

    /**
     * Method that is called when the Set Response arrives, and changes the scene
     * @param setResponse the message that confirms that the setting of the players is successful
     */
    public void setResponse(SetResponse setResponse) {
        Scene scene=gridPane.getScene();
        GuiMaster.setLayout(scene,"/fxml/connectionScene.fxml");
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
}
