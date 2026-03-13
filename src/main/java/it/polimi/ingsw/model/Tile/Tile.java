package it.polimi.ingsw.model.Tile;

import java.io.Serializable;

/**
 * @param colour ColourTile colour : type of the tile (CATS OR BOOKS OR GAMES OR FRAMES OR TROPHIES OR PLANTS)
 */
public record Tile(ColourTile colour) implements Serializable {
    /**
     * constructor of Tile : assigning the value of colour
     */
    public Tile {
    }


    /**
     * getter of colour
     *
     * @return colour
     */
    @Override
    public ColourTile colour() {
        return colour;
    }
}
