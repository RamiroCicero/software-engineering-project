module it.polimi.ingsw {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.rmi;


    opens it.polimi.ingsw.model.Board to com.google.gson;
    opens it.polimi.ingsw.Utils to com.google.gson;
    opens it.polimi.ingsw.model.Player to com.google.gson;
    opens it.polimi.ingsw.model.Tile to com.google.gson;
    opens it.polimi.ingsw.view.gui to javafx.fxml;
    opens it.polimi.ingsw.Network.Messages to com.google.gson;
    opens it.polimi.ingsw.model.CommonCards to com.google.gson;

    exports it.polimi.ingsw.Exception;
    exports it.polimi.ingsw.model.PersonalCards to com.google.gson;
    exports it.polimi.ingsw.model.Tile to com.google.gson;
    exports it.polimi.ingsw.model.Player to com.google.gson;
    exports it.polimi.ingsw.Utils ;
    exports it.polimi.ingsw.view.gui;
    exports it.polimi.ingsw.Network.Messages to java.rmi;
    exports it.polimi.ingsw.model.CommonCards;
    exports it.polimi.ingsw.Network.Client to java.rmi, javafx.graphics;
    exports it.polimi.ingsw.Network.Server to java.rmi, javafx.graphics;
    exports it.polimi.ingsw.Network.Client.Communication to java.rmi, javafx.graphics;

}