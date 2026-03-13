package it.polimi.ingsw;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.Network.Client.ClientSetup;
import it.polimi.ingsw.view.cli.Cli;
import javafx.application.Application;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Launcher {

    public static void main(final String[] args) {
        if (args[0].equalsIgnoreCase("gui")) {
            Application.launch(ClientSetup.class, args);
        } else if (args[0].equalsIgnoreCase("cli")) {
            startCLI(new Scanner(System.in));
        }
    }
    /**
     * Starts the application in CLI mode.
     * It creates an instance of the Cli class and starts the CLI application.
     *
     * @param scanner The scanner object used for CLI input
     */
    public static void startCLI(Scanner scanner) {
        new Cli(scanner).start();
    }
}
