package it.polimi.ingsw.model.Tile;

import it.polimi.ingsw.Exception.SoldOutTilesException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class TileDeck implements Serializable {

    /**
     * tileDeck : ArrayList of tiles
     */
    private final ArrayList<Tile> tileDeck;

    /**
     * constructor of TileDeck that adds 132 Tiles in tileDeck
     */
    public TileDeck() {


        tileDeck = new ArrayList<>();


        for (int i = 0; i < 22; i++) tileDeck.add(new Tile(ColourTile.CATS));

        for (int i = 0; i < 22; i++) tileDeck.add(new Tile(ColourTile.BOOKS));

        for (int i = 0; i < 22; i++) tileDeck.add(new Tile(ColourTile.GAMES));

        for (int i = 0; i < 22; i++) tileDeck.add(new Tile(ColourTile.FRAMES));

        for (int i = 0; i < 22; i++) tileDeck.add(new Tile(ColourTile.TROPHIES));

        for (int i = 0; i < 22; i++) tileDeck.add(new Tile(ColourTile.PLANTS));
    }


    /**
     * random draw from TileDeck without re-entering i.e. removed from tileDeck
     *
     * @return tileDeck.remove(...) : return a tile that it's removed from the tileDeck
     */

    public Tile randomDraw() throws SoldOutTilesException {
        if (tileDeck.size() == 0) throw new SoldOutTilesException("The bag is empty!");
        Random random = new Random();
        int number = random.nextInt(tileDeck.size());
        return tileDeck.remove(number);
    }


}
