package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.Network.Client.ClientManager;
import it.polimi.ingsw.Network.Messages.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Class that manages the Messages sent to the client,using the according method of the controllers
 */
public class GuiMaster extends ClientManager {

    private LoginSceneController loginSceneController;
    private GameSceneController gameSceneController;
    private ConnectionSceneController connectionSceneController;
    private FirstConnectionSceneController firstConnectionSceneController;
    private static GuiMaster instance = null;


    /**
     * Method that loads the desired scene
     * @param scene the main scene
     * @param path the path to the FXML to load
     */
    public static <T> T setLayout(Scene scene, String path) {
        FXMLLoader loader = new FXMLLoader(GuiMaster.class.getResource(path));

        Parent root;

        try {
            root = loader.load();
        } catch (IOException e) {
            return null;
        }

        scene.setRoot(root);
        return loader.getController();
    }

    public static GuiMaster getInstance() {
        if (instance==null){
            instance=new GuiMaster();
        }
        return instance;
    }

    public void setLoginSceneController(LoginSceneController loginSceneController) {
        this.loginSceneController = loginSceneController;
    }

    public void setGameSceneController(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    public void setFirstConnectionSceneController(FirstConnectionSceneController firstConnectionSceneController) {
        this.firstConnectionSceneController=firstConnectionSceneController;
    }

    public void setConnectionSceneController(ConnectionSceneController connectionSceneController) {
        this.connectionSceneController=connectionSceneController;
    }


    /**
     * Method that forwards the loginResponse to the right controller
     */
    @Override
    public void loginResponse(LoginResponse loginResponse) {
        Platform.runLater(() ->
                connectionSceneController.loginResponse(loginResponse));
    }
    /**
     * Method that forwards the initResponse to the right controller
     */

    @Override
    public void initResponse(InitResponse initResponse) {
        Platform.runLater(() ->
                connectionSceneController.initResponse(initResponse));
    }
    /**
     * Method that forwards the updateBoard to the right controller
     */

    @Override
    public void updateBoard(BoardResponse boardMessage) {
        Platform.runLater(() ->
                gameSceneController.updateBoard(boardMessage));
    }
    /**
     * Method that forwards the removeResponse to the right controller
     */

    @Override
    public void removeResponse(RemoveResponse removeResponse) {
        Platform.runLater(() ->
                gameSceneController.removeResponse(removeResponse));
    }
    /**
     * Method that forwards the turnResponse to the right controller
     */

    @Override
    public void turnResponse(TurnResponse turnResponse) {
        Platform.runLater(() ->
                gameSceneController.turnResponse(turnResponse));
    }
    /**
     * Method that forwards the endGame to the right controller
     */
    @Override
    public void endGame(EndMessage endGameMessage) {
        Platform.runLater(() ->
                gameSceneController.endGame(endGameMessage));
    }
    /**
     * Method that forwards the wakeUp to the right controller
     */
    @Override
    public void wakeUp(WakeMessage wakeMessage) {
        Platform.runLater(() ->
                gameSceneController.wakeUp(wakeMessage));
    }
    /**
     * Method that forwards the setResponse to the right controller
     */
    @Override
    public void setResponse(SetResponse setResponse) {
        Platform.runLater(() ->
                firstConnectionSceneController.setResponse(setResponse));

    }
    /**
     * Method that forwards the firstResponse to the right controller
     */
    public void firstResponse(FirstResponse firstResponse) {
        Platform.runLater(() ->
                loginSceneController.firstResponse(firstResponse));
    }
    /**
     * Method that forwards the preLoginResponse to the right controller
     */
    public void preLoginResponse(PreLoginResponse preLoginResponse) {
        Platform.runLater(() ->
                loginSceneController.preLoginResponse(preLoginResponse));
    }
    /**
     * Method that forwards the usernameError to the right controller
     */
    public void usernameError(UsernameError usernameError) {
        Platform.runLater(() ->
                connectionSceneController.usernameError(usernameError));
    }
    /**
     * Method that forwards the cardsResponse to the right controller
     */
    public void cardsResponse(CardsResponse cardsResponse) {
        Platform.runLater(() ->
                gameSceneController.cardsResponse(cardsResponse));
    }
    /**
     * Method that forwards the reFirstResponse to the right controller
     */
    public void reFirstResponse(ReFirstResponse reFirstResponse) {
        Platform.runLater(() ->
                connectionSceneController.reFirstResponse(reFirstResponse));
    }

    @Override
    public void disconnectionMessage(DisconnectionMessage disconnectionMessage) {
        if(getClient().isFirst() && firstConnectionSceneController!= null){
           if(gameSceneController==null){
               if(connectionSceneController==null){
                   Platform.runLater(() ->
                           firstConnectionSceneController.disconnectionMessage(disconnectionMessage));
               }else{
                   Platform.runLater(() ->
                           connectionSceneController.disconnectionMessage(disconnectionMessage));
               }
           }else {
               Platform.runLater(() ->
                       gameSceneController.disconnectionMessage(disconnectionMessage));
           }
        } else if(!getClient().isFirst() && connectionSceneController!= null){
            if(gameSceneController==null){
                Platform.runLater(() ->
                        connectionSceneController.disconnectionMessage(disconnectionMessage));
            }else {
                Platform.runLater(() ->
                        gameSceneController.disconnectionMessage(disconnectionMessage));
            }
        }
    }

    @Override
    public void chatMessage(ChatMessage chatMessage) {
        Platform.runLater(() ->
                gameSceneController.receivedChatMessage(chatMessage));
    }
}
