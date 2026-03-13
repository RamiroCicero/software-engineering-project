package it.polimi.ingsw.model.Tile;

import it.polimi.ingsw.Exception.SoldOutTilesException;
import org.junit.jupiter.api.Test;

public class TileDeckTest {
    @Test
    void randomDrawAll() {
        TileDeck tileDeck = new TileDeck();
        try {
            for (int i = 0; i < 133; i++) {
                Tile tile = tileDeck.randomDraw();
            }
        } catch (SoldOutTilesException e) {
            System.out.println("empty bag");
        }
    }
}