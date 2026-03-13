package it.polimi.ingsw.model.Player;

import it.polimi.ingsw.Exception.NoSpaceInColumnException;
import it.polimi.ingsw.model.Tile.ColourTile;
import it.polimi.ingsw.model.Tile.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class PlayerTest {
    @Test
    void testAddScore() {
        Player player = new Player("testPlayer");
        assertEquals(0, player.getScore());

        player.addScore(10);
        assertEquals(10, player.getScore());

        player.addScore(5);
        assertEquals(15, player.getScore());
    }

    @Test
    void testAddTilesInLibrary() {
        Player player = new Player("testPlayer");

        Tile tile1 = new Tile(ColourTile.GAMES);
        Tile tile2 = new Tile(ColourTile.TROPHIES);
        Tile tile3 = new Tile(ColourTile.CATS);

        try {
            player.addTilesInLibrary(0, new Tile[]{tile1, tile2});
            int count = 0;
            for (int i = 0; i < 6; i++) {
                if (!player.getPersonalShelf().getShelf()[i][0].isFree()) count++;
            }
            assertEquals(2, count);

            player.addTilesInLibrary(1, new Tile[]{tile3});
            int count1 = 0;
            for (int i = 0; i < 6; i++) {
                if (!player.getPersonalShelf().getShelf()[i][1].isFree()) count1++;
            }


            assertEquals(1, count1);
        } catch (NoSpaceInColumnException e) {
            fail("NoSpaceInColumnException thrown");
        }
    }
}