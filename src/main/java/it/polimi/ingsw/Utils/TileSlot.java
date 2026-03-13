package it.polimi.ingsw.Utils;

import it.polimi.ingsw.model.Tile.Tile;

import java.io.Serializable;

/**
 * Class that abstracts the concept of a Slot on the board
 */
public class TileSlot implements Serializable {
    /**
     * Tile that is in this slot at the moment
     */
    private Tile assignedTile;
    /**
     * a boolean to know if the Slot is empty or not
     */
    private boolean free;

    public TileSlot() {
        this.assignedTile = null;
        this.free = true;
    }

    /**
     * given a Tile, this method assigns the Tile at the space and makes the Slot full
     */
    public void assignTile(Tile assignedTile) {
        this.assignedTile = assignedTile;
        this.free = false;
    }

    /**
     * method for knowing if the space is empty or not
     */
    public boolean isFree() {
        return this.free;
    }

    /**
     * @return this method returns the slot's assigned tile
     */
    public Tile getAssignedTile() {
        return assignedTile;
    }

    /**
     * method that reverts the TileSlot to empty if requested
     */
    public void removeAssignedTile() {
        this.assignedTile = null;
        this.free = true;
    }
}
