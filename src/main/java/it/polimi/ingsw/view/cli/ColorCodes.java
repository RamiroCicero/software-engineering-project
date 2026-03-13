package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Tile.ColourTile;

/**
 * The ColorCodes class provides static methods to retrieve ANSI color codes for different colors.
 * The color codes are used to format the output with colors in supported terminals.
 */
public class ColorCodes {
    private static final String TEXT_BLACK = "\u001B[30m";
    private static final String TEXT_PURPLE = "\u001B[35m";
    private static final String TEXT_GREEN = "\u001B[32m";
    private static final String TEXT_YELLOW = "\u001B[33m";
    private static final String TEXT_BLUE = "\u001B[34m";
    private static final String TEXT_LIGHTBLUE = "\u001B[36m";
    private static final String TEXT_WHITE = "\u001B[37m";


    private ColorCodes() {

    }

    /**
     * Returns the ANSI color code associated with the specified color.
     *
     * @param color The color for which to retrieve the ANSI color code.
     * @return The ANSI color code as a string, or null if the color is not recognized.
     */
   public static String getColorCode(ColourTile color) {

        return switch (color) {
            case GAMES -> TEXT_YELLOW;
            case CATS -> TEXT_GREEN;
            case PLANTS -> TEXT_PURPLE;
            case FRAMES -> TEXT_BLUE;
            case TROPHIES -> TEXT_LIGHTBLUE;
            case BOOKS -> TEXT_WHITE;
            case FREE -> TEXT_BLACK;
        };

    }
}

