package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.InputStream;
/**
 * Class for starting the Graphical User Interface, made with JavaFX
 */
public class Gui extends Application {
    /**
     * Method to start the gui, setting the stage
     */
    @Override
    public void start(Stage stage) {

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        double stageWidth = screenWidth * 0.6;
        double stageHeight = screenHeight * 0.8;

        stage.setWidth(stageWidth);
        stage.setHeight(stageHeight);

        InputStream is = Gui.class.getClassLoader().getResourceAsStream("assets/Publisher material/Icon 50x50px.png");
        if (is != null) {
            stage.getIcons().add(new Image(is));
        }

        Scene scene = new Scene(new Pane());
        GuiMaster.setLayout(scene, "/fxml/loginScene.fxml");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Method that stops the Gui
     */
    @Override
    public void stop() {
        System.exit(0);
    }
}