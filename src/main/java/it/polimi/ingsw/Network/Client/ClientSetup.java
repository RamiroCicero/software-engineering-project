package it.polimi.ingsw.Network.Client;


import it.polimi.ingsw.view.gui.Gui;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * The ClientSetup class is the entry point of the client application.
 * It provides the option to choose between GUI mode and CLI mode for running the client.
 */
public class ClientSetup extends Application {

    /**
     * The main method of the client application.
     * It prompts the user to choose between GUI mode and CLI mode and launches the application accordingly.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method of the JavaFX application in GUI mode.
     * It creates an instance of the Gui class and starts the GUI application.
     *
     * @param primaryStage The primary stage for the JavaFX application
     */
    public void start(Stage primaryStage) {
        Gui gui = new Gui();
        gui.start(primaryStage);
        primaryStage.setOnHidden(event -> {
            Platform.exit();
            System.exit(0);
        });
    }


}


