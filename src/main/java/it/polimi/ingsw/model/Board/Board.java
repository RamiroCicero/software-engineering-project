package it.polimi.ingsw.model.Board;

import it.polimi.ingsw.Exception.EmptySlotException;
import it.polimi.ingsw.Exception.InvalidPositionsException;
import it.polimi.ingsw.Exception.InvalidSlotException;
import it.polimi.ingsw.Exception.SoldOutTilesException;
import it.polimi.ingsw.Utils.Coordinates;
import it.polimi.ingsw.Utils.TileSlot;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.TileDeck;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class that represents the gaming board
 */
public class Board implements Serializable {
    public static final int MAX_BOARD_ROWS = 11;
    public static final int MAX_BOARD_COLUMNS = 11;
    /**
     * A TileSlot Matrix, big enough for all the number of players, with extra space to make it a square for simplify operation like refilling
     */
    private final TileSlot[][] board;
    /**
     * The bag that contains all the tiles of the game, used by the board to initialize and refill itself
     */
    private final TileDeck bag;
    private final boolean[][] boardMask;
    /**
     * The token that begins the final turn
     */
    private final EndGameToken endGameToken;


    /**
     * the constructor of this class, that uses the boolean masks to fill the "true" marked spots
     *
     * @param numOfPlayers number of players at the start of the game,used for switch cases
     */
    public Board(int numOfPlayers) {

        this.endGameToken=new EndGameToken();

        this.bag = new TileDeck();
        this.board = new TileSlot[MAX_BOARD_ROWS][MAX_BOARD_COLUMNS];

        BoardMaskParser boardMaskParser;
        try {
            boardMaskParser = new BoardMaskParser();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.boardMask = boardMaskParser.getBoardMask().getTiles(numOfPlayers);

        for (int i = 0; i < MAX_BOARD_ROWS; i++) {
            for (int j = 0; j < MAX_BOARD_COLUMNS; j++) {
                board[i][j] = new TileSlot();
            }
        }
        for (int j = 0; j < MAX_BOARD_ROWS; j++) {
            for (int k = 0; k < MAX_BOARD_COLUMNS; k++) {
                if (boardMask[j][k]) {
                    try {
                        board[j][k].assignTile(bag.randomDraw());
                    } catch (SoldOutTilesException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    /**
     * method for refilling the board if necessary,leaving the already filled TileSlots untouched
     */
    public void refillBoard() throws SoldOutTilesException {
        for (int j = 0; j < MAX_BOARD_ROWS; j++) {
            for (int k = 0; k < MAX_BOARD_COLUMNS; k++) {
                if ((boardMask[j][k]) && (board[j][k].isFree())) board[j][k].assignTile(bag.randomDraw());
            }
        }
    }

    /**
     * Method for removing from the board the player's selected tiles,first controlling if they are valid
     *
     * @param positions an array of Coordinates,an Integer Pair with the coordinates of the selected tiles
     * @return Selected tile is an array of Tiles, later used by the Player to fill his Shelf
     * @throws EmptySlotException   exception for managing the selection of a free space on the board
     * @throws InvalidSlotException exception for managing the selection of a Tile with no free spaces around
     */

    public Tile[] removeCardFromBoard(ArrayList<Coordinates> positions) throws EmptySlotException, InvalidSlotException, InvalidPositionsException {

        for (int i = 1; i < positions.size(); i++) {
            if (!Objects.equals(positions.get(i - 1).getRow(), positions.get(i).getRow()) && !Objects.equals(positions.get(i-1).getColumn(), positions.get(i).getColumn())) {
                throw new InvalidPositionsException("The selected positions are invalid");
            }
        }

        Tile[] selectedTile = new Tile[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            Coordinates position = positions.get(i);

            if (board[position.getRow()][position.getColumn()].isFree())
                throw new EmptySlotException("This slot is Empty");
            if (!boardMask[position.getRow()][position.getColumn()])
                throw new InvalidSlotException("This slot is invalid");

            if ((board[(position.getRow()) + 1][position.getColumn()].isFree()) || (board[(position.getRow()) - 1][position.getColumn()].isFree()) || (board[(position.getRow())][position.getColumn() + 1].isFree()) || (board[(position.getRow())][position.getColumn() - 1].isFree())) {
                selectedTile[i] = board[position.getRow()][position.getColumn()].getAssignedTile();
            } else throw new InvalidSlotException("this slot is invalid");
        }
        for (Coordinates position : positions) {
            board[position.getRow()][position.getColumn()].removeAssignedTile();
        }
        return selectedTile;
    }

    /**
     * method to see if on the board are only tiles with free spaces near them
     */
    public boolean refillIsNecessary() {
        for (int i = 1; i < MAX_BOARD_ROWS; i++) {
            for (int j = 1; j < MAX_BOARD_COLUMNS; j++) {
                if(boardMask[i][j]){
                    if ((boardMask[i][j])&&(!board[i][j].isFree()) && ((!(board[i + 1][j].isFree())) || (!(board[i - 1][j].isFree())) || (!(board[i][j + 1].isFree())) || (!(board[i][j - 1].isFree()))))
                        return false;
                }
            }
        }
        return true;
    }


    /**
     * getter of the board
     * @return board
     */
    public TileSlot[][] getBoard() {
        return this.board;
    }

    /**
     * sets as taken the EndGameToken
     */
    public void takeEndGameToken() {
        endGameToken.setTaken();
    }

    /**
     * to check if EndGameToken is taken
     * @return if token is taken
     */
    public boolean isEndGameTokenTaken(){
        return endGameToken.isTaken();
    }
}
