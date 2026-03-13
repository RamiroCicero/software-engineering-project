package it.polimi.ingsw.model.Player;

import it.polimi.ingsw.Exception.NoSpaceInColumnException;
import it.polimi.ingsw.model.Tile.ColourTile;
import it.polimi.ingsw.model.Tile.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShelfTest {

    @Test

    public void FullColumn() {
        Exception exception = assertThrows(NoSpaceInColumnException.class, () -> {

            Shelf shelf = new Shelf();
            Tile[] tiles = new Tile[3];
            tiles[0] = new Tile(ColourTile.CATS);
            tiles[1] = new Tile(ColourTile.CATS);
            tiles[2] = new Tile(ColourTile.CATS);

            Tile[] tiles1 = new Tile[3];
            tiles1[0] = new Tile(ColourTile.TROPHIES);
            tiles1[1] = new Tile(ColourTile.TROPHIES);
            tiles1[2] = new Tile(ColourTile.CATS);


            Tile[] tiles2 = new Tile[1];


            for (int i = 0; i < 3; i++) {
                shelf.addCardInColumn(0, tiles);
            }
            for (int i = 0; i < 3; i++) {
                shelf.addCardInColumn(0, tiles1);
            }
            shelf.addCardInColumn(0, tiles2);
        });
    }


}

