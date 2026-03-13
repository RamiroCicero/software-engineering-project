package it.polimi.ingsw.Utils;

import it.polimi.ingsw.Exception.NoSpaceInColumnException;
import it.polimi.ingsw.Exception.SoldOutTilesException;
import it.polimi.ingsw.model.CommonCards.CardCommonTarget;
import it.polimi.ingsw.model.CommonCards.CommonList;
import it.polimi.ingsw.model.PersonalCards.PersonalCardTile;
import it.polimi.ingsw.model.Player.Shelf;
import it.polimi.ingsw.model.Tile.ColourTile;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.TileDeck;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static it.polimi.ingsw.view.cli.ColorCodes.getColorCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilsTest {

    @Test
    void checkSixGroupsOfTwo() {
        Utils utils = new Utils();
        Shelf shelf = new Shelf();
        utils.shelfDebug(shelf, new PersonalCardTile[]{
                new PersonalCardTile((new Coordinates(0, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(1, 0)), ColourTile.BOOKS),

                new PersonalCardTile((new Coordinates(2, 0)), ColourTile.TROPHIES),
                new PersonalCardTile((new Coordinates(3, 0)), ColourTile.TROPHIES),

                new PersonalCardTile((new Coordinates(0, 1)), ColourTile.CATS),
                new PersonalCardTile((new Coordinates(1, 1)), ColourTile.CATS),

                new PersonalCardTile((new Coordinates(2, 2)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(2, 3)), ColourTile.BOOKS),

                new PersonalCardTile((new Coordinates(1, 3)), ColourTile.GAMES),
                new PersonalCardTile((new Coordinates(1, 4)), ColourTile.GAMES),

                new PersonalCardTile((new Coordinates(4, 4)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(4, 3)), ColourTile.BOOKS)});


        assertTrue(utils.checkCommonTarget(shelf, new CardCommonTarget(CommonList.SIX_GROUPS_OF_TWO, 0, 3)));
    }

    @Test
    void checkFourEqualsAngles() {
        Utils utils = new Utils();
        Shelf shelf = new Shelf();
        utils.shelfDebug(shelf, new PersonalCardTile[]{
                new PersonalCardTile((new Coordinates(0, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(5, 0)), ColourTile.BOOKS),

                new PersonalCardTile((new Coordinates(5, 4)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(0, 4)), ColourTile.BOOKS),});


        assertTrue(utils.checkCommonTarget(shelf, new CardCommonTarget(CommonList.FOUR_EQUALS_ANGLES, 0, 3)));
    }

    @Test
    void checkFourGroupsOfFour() {
        Utils utils = new Utils();
        Shelf shelf = new Shelf();
        utils.shelfDebug(shelf, new PersonalCardTile[]{
                new PersonalCardTile((new Coordinates(0, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(1, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(1, 1)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(0, 1)), ColourTile.BOOKS),

                new PersonalCardTile((new Coordinates(5, 0)), ColourTile.CATS),
                new PersonalCardTile((new Coordinates(5, 1)), ColourTile.CATS),
                new PersonalCardTile((new Coordinates(4, 0)), ColourTile.CATS),
                new PersonalCardTile((new Coordinates(4, 1)), ColourTile.CATS),

                new PersonalCardTile((new Coordinates(3, 3)), ColourTile.GAMES),
                new PersonalCardTile((new Coordinates(3, 4)), ColourTile.GAMES),
                new PersonalCardTile((new Coordinates(2, 4)), ColourTile.GAMES),
                new PersonalCardTile((new Coordinates(2, 3)), ColourTile.GAMES),

                new PersonalCardTile((new Coordinates(1, 3)), ColourTile.TROPHIES),
                new PersonalCardTile((new Coordinates(1, 4)), ColourTile.TROPHIES),
                new PersonalCardTile((new Coordinates(0, 3)), ColourTile.TROPHIES),
                new PersonalCardTile((new Coordinates(0, 4)), ColourTile.TROPHIES)});


        assertTrue(utils.checkCommonTarget(shelf, new CardCommonTarget(CommonList.FOUR_GROUPS_OF_FOUR, 0, 3)));
    }

    @Test
    void checkThreeColumnsThreeDifferentTypes() {
        Utils utils = new Utils();
        Shelf shelf = new Shelf();
        utils.shelfDebug(shelf, new PersonalCardTile[]{

                new PersonalCardTile((new Coordinates(0, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(1, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(2, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(3, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(4, 0)), ColourTile.CATS),
                new PersonalCardTile((new Coordinates(5, 0)), ColourTile.CATS),

                new PersonalCardTile((new Coordinates(0, 1)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(1, 1)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(2, 1)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(3, 1)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(4, 1)), ColourTile.CATS),
                new PersonalCardTile((new Coordinates(5, 1)), ColourTile.CATS),

                new PersonalCardTile((new Coordinates(0, 3)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(1, 3)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(2, 3)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(3, 3)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(4, 3)), ColourTile.CATS),
                new PersonalCardTile((new Coordinates(5, 3)), ColourTile.CATS),});


        assertTrue(utils.checkCommonTarget(shelf, new CardCommonTarget(CommonList.THREE_COLUMNS_THREE_DIFFERENT_TYPES, 0, 3)));
    }

    @Test
    void checkTwoGroupsInSquare() {
        Utils utils = new Utils();
        Shelf shelf = new Shelf();
        utils.shelfDebug(shelf, new PersonalCardTile[]{
                new PersonalCardTile((new Coordinates(0, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(1, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(1, 1)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(0, 1)), ColourTile.BOOKS),

                new PersonalCardTile((new Coordinates(5, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(5, 1)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(4, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(4, 1)), ColourTile.BOOKS),});


        assertTrue(utils.checkCommonTarget(shelf, new CardCommonTarget(CommonList.TWO_GROUPS_IN_SQUARE, 0, 3)));
    }

    @Test
    void checkEightEquals() {
        Utils utils = new Utils();
        Shelf shelf = new Shelf();
        utils.shelfDebug(shelf, new PersonalCardTile[]{

                new PersonalCardTile((new Coordinates(0, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(1, 1)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(2, 3)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(3, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(4, 1)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(5, 3)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(2, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(0, 1)), ColourTile.BOOKS),});


        assertTrue(utils.checkCommonTarget(shelf, new CardCommonTarget(CommonList.EIGHT_EQUALS, 0, 3)));
    }

    @Test
    void checkFiveInDiagonal() {
        Utils utils = new Utils();
        Shelf shelf = new Shelf();
        utils.shelfDebug(shelf, new PersonalCardTile[]{

                new PersonalCardTile((new Coordinates(4, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(3, 1)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(2, 2)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(1, 3)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(0, 4)), ColourTile.BOOKS)});


        assertTrue(utils.checkCommonTarget(shelf, new CardCommonTarget(CommonList.FIVE_IN_DIGONAL, 0, 3)));
    }

    @Test
    void checkFourRowsThreeDifferentTypes() {
        Utils utils = new Utils();
        Shelf shelf = new Shelf();
        utils.shelfDebug(shelf, new PersonalCardTile[]{

                new PersonalCardTile((new Coordinates(0, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(0, 1)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(0, 2)), ColourTile.GAMES),
                new PersonalCardTile((new Coordinates(0, 3)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(0, 4)), ColourTile.TROPHIES),

                new PersonalCardTile((new Coordinates(1, 0)), ColourTile.CATS),
                new PersonalCardTile((new Coordinates(1, 1)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(1, 2)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(1, 3)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(1, 4)), ColourTile.BOOKS),

                new PersonalCardTile((new Coordinates(2, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(2, 1)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(2, 2)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(2, 3)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(2, 4)), ColourTile.CATS),

                new PersonalCardTile((new Coordinates(3, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(3, 1)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(3, 2)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(3, 3)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(3, 4)), ColourTile.CATS)});


        assertTrue(utils.checkCommonTarget(shelf, new CardCommonTarget(CommonList.FOUR_ROWS_THREE_DIFFERENT_TYPES, 0, 3)));
    }

    @Test
    void checkTwoColumnAllDifferent() {
        Utils utils = new Utils();
        Shelf shelf = new Shelf();
        utils.shelfDebug(shelf, new PersonalCardTile[]{

                new PersonalCardTile((new Coordinates(0, 0)), ColourTile.FRAMES),
                new PersonalCardTile((new Coordinates(1, 0)), ColourTile.PLANTS),
                new PersonalCardTile((new Coordinates(2, 0)), ColourTile.GAMES),
                new PersonalCardTile((new Coordinates(3, 0)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(4, 0)), ColourTile.TROPHIES),
                new PersonalCardTile((new Coordinates(5, 0)), ColourTile.CATS),

                new PersonalCardTile((new Coordinates(0, 1)), ColourTile.FRAMES),
                new PersonalCardTile((new Coordinates(1, 1)), ColourTile.PLANTS),
                new PersonalCardTile((new Coordinates(2, 1)), ColourTile.GAMES),
                new PersonalCardTile((new Coordinates(3, 1)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(4, 1)), ColourTile.TROPHIES),
                new PersonalCardTile((new Coordinates(5, 1)), ColourTile.CATS)});


        assertTrue(utils.checkCommonTarget(shelf, new CardCommonTarget(CommonList.TWO_COLUMNS_ALL_DIFFERENT, 0, 3)));
    }

    @Test
    void checkTwoRowsAllDifferent() {
        Utils utils = new Utils();
        Shelf shelf = new Shelf();
        utils.shelfDebug(shelf, new PersonalCardTile[]{

                new PersonalCardTile((new Coordinates(0, 0)), ColourTile.FRAMES),
                new PersonalCardTile((new Coordinates(0, 1)), ColourTile.PLANTS),
                new PersonalCardTile((new Coordinates(0, 2)), ColourTile.GAMES),
                new PersonalCardTile((new Coordinates(0, 3)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(0, 4)), ColourTile.TROPHIES),

                new PersonalCardTile((new Coordinates(1, 0)), ColourTile.FRAMES),
                new PersonalCardTile((new Coordinates(1, 1)), ColourTile.PLANTS),
                new PersonalCardTile((new Coordinates(1, 2)), ColourTile.CATS),
                new PersonalCardTile((new Coordinates(1, 3)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(1, 4)), ColourTile.TROPHIES),});


        assertTrue(utils.checkCommonTarget(shelf, new CardCommonTarget(CommonList.TWO_ROWS_ALL_DIFFERENT, 0, 3)));
    }

    @Test
    void checkFiveInAX() {
        Utils utils = new Utils();
        Shelf shelf = new Shelf();
        utils.shelfDebug(shelf, new PersonalCardTile[]{

                new PersonalCardTile((new Coordinates(3, 2)), ColourTile.PLANTS),
                new PersonalCardTile((new Coordinates(4, 3)), ColourTile.PLANTS),
                new PersonalCardTile((new Coordinates(2, 1)), ColourTile.PLANTS),
                new PersonalCardTile((new Coordinates(4, 1)), ColourTile.PLANTS),
                new PersonalCardTile((new Coordinates(2, 3)), ColourTile.PLANTS),});


        assertTrue(utils.checkCommonTarget(shelf, new CardCommonTarget(CommonList.FIVE_IN_A_X, 0, 3)));
    }

    @Test
    void checkInDescendingOrder() {
        Utils utils = new Utils();
        Shelf shelf = new Shelf();
        utils.shelfDebug(shelf, new PersonalCardTile[]{

                new PersonalCardTile((new Coordinates(0, 4)), ColourTile.BOOKS),
                new PersonalCardTile((new Coordinates(1, 4)), ColourTile.PLANTS),
                new PersonalCardTile((new Coordinates(2, 4)), ColourTile.TROPHIES),
                new PersonalCardTile((new Coordinates(3, 4)), ColourTile.PLANTS),
                new PersonalCardTile((new Coordinates(4, 4)), ColourTile.CATS),

                new PersonalCardTile((new Coordinates(0, 3)), ColourTile.GAMES),
                new PersonalCardTile((new Coordinates(1, 3)), ColourTile.PLANTS),
                new PersonalCardTile((new Coordinates(2, 3)), ColourTile.PLANTS),
                new PersonalCardTile((new Coordinates(3, 3)), ColourTile.PLANTS),

                new PersonalCardTile((new Coordinates(0, 2)), ColourTile.PLANTS),
                new PersonalCardTile((new Coordinates(1, 2)), ColourTile.FRAMES),
                new PersonalCardTile((new Coordinates(2, 2)), ColourTile.PLANTS),

                new PersonalCardTile((new Coordinates(0, 1)), ColourTile.PLANTS),
                new PersonalCardTile((new Coordinates(1, 1)), ColourTile.BOOKS),

                new PersonalCardTile((new Coordinates(0, 0)), ColourTile.GAMES),});


        assertTrue(utils.checkCommonTarget(shelf, new CardCommonTarget(CommonList.IN_DESCENDING_ORDER, 0, 3)));
    }

    @Test
    void checkAllDifferent() {
        Utils utils = new Utils();
        TileSlot[] libraryMatrix = new TileSlot[5];
        for (int i = 0; i < 5; i++) {
            libraryMatrix[i] = new TileSlot();
        }


        libraryMatrix[0].assignTile(new Tile(ColourTile.CATS));
        libraryMatrix[1].assignTile(new Tile(ColourTile.TROPHIES));
        libraryMatrix[2].assignTile(new Tile(ColourTile.PLANTS));
        libraryMatrix[3].assignTile(new Tile(ColourTile.BOOKS));
        libraryMatrix[4].assignTile(new Tile(ColourTile.BOOKS));

        assertEquals(4, utils.checkAllDifferent(libraryMatrix, "ROW"));


        TileSlot[] libraryMatrix1 = new TileSlot[6];

        for (int i = 0; i < 6; i++) {
            libraryMatrix1[i] = new TileSlot();
        }


        libraryMatrix1[0].assignTile(new Tile(ColourTile.CATS));
        libraryMatrix1[1].assignTile(new Tile(ColourTile.TROPHIES));
        libraryMatrix1[2].assignTile(new Tile(ColourTile.PLANTS));
        libraryMatrix1[3].assignTile(new Tile(ColourTile.BOOKS));
        libraryMatrix1[4].assignTile(new Tile(ColourTile.BOOKS));
        libraryMatrix1[5].assignTile(new Tile(ColourTile.BOOKS));

        assertEquals(4, utils.checkAllDifferent(libraryMatrix1, "COLUMN"));

    }

    @Test
    void checkDiagonal() throws SoldOutTilesException {
        Utils utils = new Utils();
        TileDeck bag = new TileDeck();
        TileSlot[][] libraryMatrix = new TileSlot[6][5];

        Coordinates coordinates = new Coordinates(0, 4);
        int h = 1;
        int k = 1;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                libraryMatrix[i][j] = new TileSlot();
            }
        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                libraryMatrix[i][j].assignTile(bag.randomDraw());
            }
        }

        int j = 4;
        for (int i = 0; i < 5; i++) {
            libraryMatrix[i][j].assignTile(new Tile(ColourTile.BOOKS));
            j--;
        }

        assertTrue(utils.checkDiagonal(libraryMatrix, coordinates));
    }

    @Test
    void checkUltimate(){
        Shelf shelf = new Shelf();
        Utils utils = new Utils();

        for(int i = 0; i< 5; i++){
            for(int j = 0; j< 6; j++){
                Random random = new Random();
                int num = random.nextInt(6);
                Tile[] tiles = new Tile[1];
                if(num == 0){
                    tiles[0] = new Tile(ColourTile.BOOKS);
                }
                if(num == 1){
                    tiles[0] = new Tile(ColourTile.FRAMES);
                }
                if(num == 2){
                    tiles[0] = new Tile(ColourTile.GAMES);
                }
                if(num == 3){
                    tiles[0] = new Tile(ColourTile.PLANTS);
                }
                if(num == 4){
                    tiles[0] = new Tile(ColourTile.TROPHIES);
                }
                if(num == 5){
                    tiles[0] = new Tile(ColourTile.CATS);
                }

                try {
                    shelf.addCardInColumn(i, tiles);
                } catch (NoSpaceInColumnException e) {
                    throw new RuntimeException(e);
                }
            }
        }


        for(int i = 0; i < 5; i++) {
            System.out.print(" " + i + "  ");
        }
        System.out.print("\n");

        for (int i = 5; i >=0; i--) {
            for (int j = 0; j < 5; j++) {
                String ANSI_RESET = "\u001B[0m";
                System.out.print(getColorCode(shelf.getShelf()[i][j].getAssignedTile().colour()) + "*** " + ANSI_RESET);
            }
            System.out.print("\n");
        }

    }
    @Test
    void checkUltimateWithSpace(){
        Shelf shelf = new Shelf();
        Utils utils=new Utils();
        for(int i = 0; i< 5; i++){
            for(int j = 0; j< 6; j++){
                Random random = new Random();
                int num = random.nextInt(6);
                Tile[] tiles = new Tile[1];
                if(num == 0){
                    tiles[0] = new Tile(ColourTile.BOOKS);
                }
                if(num == 2){
                    tiles[0] = new Tile(ColourTile.GAMES);
                }
                if(num == 3){
                    tiles[0] = new Tile(ColourTile.PLANTS);
                }
                if(num == 4){
                    tiles[0] = new Tile(ColourTile.TROPHIES);
                }

                if (tiles[0]!=null) {
                    try {
                        shelf.addCardInColumn(i, tiles);
                    } catch (NoSpaceInColumnException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }


        for(int i = 0; i < 5; i++) {
            System.out.print(" " + i + "  ");
        }
        System.out.print("\n");

        for (int i = 5; i >=0; i--) {
            for (int j = 0; j < 5; j++) {
                String ANSI_RESET = "\u001B[0m";
                if (!shelf.getShelf()[i][j].isFree()) {
                    System.out.print(getColorCode(shelf.getShelf()[i][j].getAssignedTile().colour()) + "*** " + ANSI_RESET);
                }else System.out.print(getColorCode(ColourTile.FREE) + "*** " + ANSI_RESET);
            }
            System.out.print("\n");
        }



        System.out.print("Hai fatto: "+ utils.groupScore(shelf));
    }

}