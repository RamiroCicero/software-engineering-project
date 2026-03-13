package it.polimi.ingsw.model.Player;


import it.polimi.ingsw.Exception.NoSpaceInColumnException;
import it.polimi.ingsw.Utils.TileSlot;
import it.polimi.ingsw.model.Tile.Tile;

import java.io.Serializable;

/**
 * class that represents player's shelf
 */
public class Shelf implements Serializable {
    public static final int MAX_SHELF_ROWS = 6;
    public static final int MAX_SHELF_COLUMNS = 5;

    /**
     * Shelf is a MAX_SHELF_ROWSxMAX_SHELF_COLUMNS matrix
     */
    private final TileSlot[][] shelf = new TileSlot[MAX_SHELF_ROWS][MAX_SHELF_COLUMNS];


    /**
     * initialize shelf with empty slots
     */
    public Shelf() {
        for (int i = 0; i < MAX_SHELF_ROWS; i++) {
            for (int j = 0; j < MAX_SHELF_COLUMNS; j++) {
                shelf[i][j] = new TileSlot();
            }
        }
    }


    /**
     * method that adds up to three selected tiles in Shelf. It counts, in the given column, how many rows are full using the method isFree (form TileSlot)
     * Tiles are stored in array created by Board and put on the shelf with assignTile method
     *
     * @throws NoSpaceInColumnException if there is not enough space for the selected numbers of tiles
     */
    public void addCardInColumn(int col, Tile[] selectedTile) throws NoSpaceInColumnException {


        int row = 0;
        if (!shelf[row][col].isFree()) {
            while (row < MAX_SHELF_ROWS && !shelf[row][col].isFree()) {
                row++;
            }


            if (row > MAX_SHELF_ROWS - selectedTile.length) {
                throw new NoSpaceInColumnException("The column is full");
            }

        }

        for (Tile tile : selectedTile) {
            shelf[row][col].assignTile(tile);
            row++;
        }


    }





    /**
     * method that verifies if shelf is completely full. It controls if every slot of the last row is full
     */
    public boolean isFull() {

        int count = 0;
        for (int i = 0; i < MAX_SHELF_COLUMNS; i++) {
            if (!shelf[MAX_SHELF_ROWS - 1 ][i].isFree()) {
                count++;
            }

            if (count == MAX_SHELF_COLUMNS) {
                return true;
            }
        }
        return false;
    }

    public TileSlot[][] getShelf() {
        return shelf;
    }
}
