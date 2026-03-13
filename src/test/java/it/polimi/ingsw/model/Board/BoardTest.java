package it.polimi.ingsw.model.Board;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.Exception.EmptySlotException;
import it.polimi.ingsw.Exception.InvalidPositionsException;
import it.polimi.ingsw.Exception.InvalidSlotException;
import it.polimi.ingsw.Exception.SoldOutTilesException;
import it.polimi.ingsw.Utils.Coordinates;
import it.polimi.ingsw.model.Tile.ColourTile;
import it.polimi.ingsw.model.Tile.Tile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * Unit test for simple App.
 */
public class BoardTest {
    @Test
    public void twoPlayersCorrectTilesDisposition() throws SoldOutTilesException {
        boolean[][] twoPlayersTiles =
                {       {false,false,false, false, false, false, false, false, false, false, false},
                        {false,false, false, false, false, false, false, false, false, false,false},
                        {false,false, false, false, true, true, false, false, false, false,false},
                        {false,false, false, false, true, true, true, false, false, false,false},
                        {false,false, false, true, true, true, true, true, true, false,false},
                        {false,false, true, true, true, true, true, true, true, false,false},
                        {false,false, true, true, true, true, true, true, false, false,false},
                        {false,false, false, false, true, true, true, false, false, false,false},
                        {false,false, false, false, false, true, true, false, false, false,false},
                        {false,false, false, false, false, false, false, false, false, false,false},
                        {false,false, false, false, false, false, false, false, false, false,false},
                };
        Board board = new Board(2);
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j <11; j++) {
                if (twoPlayersTiles[i][j]) assertFalse(board.getBoard()[i][j].isFree());
                else assertTrue(board.getBoard()[i][j].isFree());
            }
        }
    }
    @Test
    public void threePlayersCorrectTilesDisposition() throws SoldOutTilesException {
         boolean[][] threePlayersTiles =
                {       {false,false,false, false, false, false, false, false, false, false, false},
                        {false,false, false, false, true, false, false, false, false, false,false},
                        {false,false, false, false, true, true, false, false, false, false,false},
                        {false,false, false, true, true, true, true, true, false, false,false},
                        {false,false, false, true, true, true, true, true, true, true,false},
                        {false,false, true, true, true, true, true, true, true, false,false},
                        {false,true, true, true, true, true, true, true, false, false,false},
                        {false,false, false, true, true, true, true, true, false, false,false},
                        {false,false, false, false, false, true, true, false, false, false,false},
                        {false,false, false, false, false, false, true, false, false, false,false},
                        {false,false,false, false, false, false, false, false, false, false, false}
                };

        Board board = new Board(3);
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (threePlayersTiles[i][j]) assertFalse(board.getBoard()[i][j].isFree());
                else assertTrue(board.getBoard()[i][j].isFree());
            }
        }
    }
    @Test
    public void fourPlayersCorrectTilesDisposition() throws SoldOutTilesException {
        boolean[][] fourPlayersTiles =
                {       {false,false,false, false, false, false, false, false, false, false, false},
                        {false,false, false, false, true, true, false, false, false, false,false},
                        {false,false, false, false, true, true, true, false, false, false,false},
                        {false,false, false, true, true, true, true, true, false, false,false},
                        {false,false, true, true, true, true, true, true, true, true,false},
                        {false,true, true, true, true, true, true, true, true, true,false},
                        {false,true, true, true, true, true, true, true, true, false,false},
                        {false,false, false, true, true, true, true, true, false, false,false},
                        {false,false, false, false, true, true, true, false, false, false,false},
                        {false,false, false, false, false, true, true, false, false, false,false},
                        {false,false,false, false, false, false, false, false, false, false, false}};

        Board board = new Board(4);
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (fourPlayersTiles[i][j]) assertFalse(board.getBoard()[i][j].isFree());
                else assertTrue(board.getBoard()[i][j].isFree());
            }
        }
    }

    @Test
    public void emptySlotOnBoard() throws EmptySlotException, InvalidSlotException, SoldOutTilesException{
        Board board = new Board(2);
        Tile[] tiles = new Tile[1];

        ArrayList<Coordinates> positions = new ArrayList<>();
        positions.add(new Coordinates(0,0));
        try {
            tiles = board.removeCardFromBoard(positions);

        } catch(EmptySlotException e) {
            System.out.println("EmptySlotException");
          } catch (InvalidPositionsException e) {
            System.out.println("InvalidPositionsException");
        }
    }


    @Test

    public void invalidSlotOnBoard() throws EmptySlotException, InvalidSlotException , SoldOutTilesException, InvalidPositionsException{
        Board board = new Board(2);
        Tile[] tiles = new Tile[1];

        ArrayList<Coordinates> positions = new ArrayList<>();
        positions.add(new Coordinates(5,5));
        try {
            tiles = board.removeCardFromBoard(positions);

        } catch(InvalidSlotException e) {
            System.out.println("InvalidSlotException");
        }
    }




    @Test


    public void validPositionsInBoard() throws InvalidPositionsException, InvalidSlotException, EmptySlotException, SoldOutTilesException{
        Board board = new Board(4);
        Tile[] tiles;

        ArrayList<Coordinates> positions = new ArrayList<>();
        positions.add(new Coordinates(1,8));
        positions.add(new Coordinates(0,4));
        try{
            tiles = board.removeCardFromBoard(positions);
        }
         catch (InvalidSlotException e){
            System.out.println("invalidSlot");
         }

         catch(EmptySlotException e){
            System.out.println("emptySlot");
         }

         catch(InvalidPositionsException e){
            System.out.println("invalidPositions");
         }

        System.out.println("validPositions");
    }

    @Test
    public void getNullTile(){
        try {
            Board board=new Board(2);
            ColourTile colourTile = board.getBoard()[0][0].getAssignedTile().colour();
        } catch (NullPointerException e) {
            System.out.println("Exception!");
        }

    }
}
