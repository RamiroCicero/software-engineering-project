package it.polimi.ingsw.model;

import it.polimi.ingsw.Exception.*;
import it.polimi.ingsw.Utils.Coordinates;
import it.polimi.ingsw.Utils.TileSlot;
import it.polimi.ingsw.Utils.Utils;
import it.polimi.ingsw.model.Board.Board;
import it.polimi.ingsw.model.CommonCards.CardCommonTarget;
import it.polimi.ingsw.model.CommonCards.CommonDeck;
import it.polimi.ingsw.model.PersonalCards.CardPersonalTarget;
import it.polimi.ingsw.model.PersonalCards.PersonalDeck;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.model.Tile.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * class that manage the logic of the game, receiving messages from the controller to evolve the game
 */
public class Game implements Serializable {
    private final Utils utils;
    private Board board;
    private GameState gameState = GameState.WAITING_PLAYERS;
    private boolean isLastTurn;
    private ArrayList<CardCommonTarget> commonDeck;


    /**
     * Constructor of The Game class
     */
    public Game() {
        this.isLastTurn = false;
        this.utils = new Utils();
    }


    /**
     * method to initialize effectively the Game, knowing the number of players
     */
    public void GameInit(List<Player> players) {
        commonDeck = new CommonDeck(players.size()).getCommonDeck();
        ArrayList<CardPersonalTarget> personalDeck = new PersonalDeck(players.size()).getPersonalDeck();
        board = new Board(players.size());
        setGameState(GameState.GAME_INIT);


        for (int i = 0; i < players.size(); i++) {
            players.get(i).setPersonalCard(personalDeck.get(i));
        }

    }


    /**
     * method to refill the Board if it's necessary
     */
    public void refillBoard() throws SoldOutTilesException {
        if (board.refillIsNecessary()) {
            board.refillBoard();
        }
    }

    /**
     * Method that manages the removing of the selected Tiles of currentPlayer from the board
     *
     * @param positions     array of the selected tiles coordinates
     */
    public Tile[] remove(ArrayList<Coordinates> positions) throws InvalidPositionsException, EmptySlotException, InvalidSlotException {
        Tile[] removedTile;

        removedTile = board.removeCardFromBoard(positions);
        return removedTile;

    }

    /**
     * method that manages places them in the shelf in the selected column.
     *
     * @param tilesToAdd     tiles to add to the shelf
     * @param currentPlayer  player currently playing
     * @param selectedColumn the selected column
     * @throws NoSpaceInColumnException throw when the colum has not enough space
     */
    public void addInShelf(Tile[] tilesToAdd, Player currentPlayer, int selectedColumn) throws NoSpaceInColumnException {
        currentPlayer.addTilesInLibrary(selectedColumn, tilesToAdd);
    }

    /**
     * Method that check if the player has completed the two Common objective and if it has already completed them before,
     * and proceeds to add the value of the ScoringToken to the player score, removing it from the card.
     *
     * @param currentPlayer the player that is currently playing his turn
     */
    public void checkCommonTarget(Player currentPlayer) {
        for (CardCommonTarget cardCommonTarget : commonDeck) {
            if (!(currentPlayer.isCompleted(cardCommonTarget.getAssignedCommonCard())) && (utils.checkCommonTarget(currentPlayer.getPersonalShelf(), cardCommonTarget))) {

                currentPlayer.setCompleted(cardCommonTarget.getAssignedCommonCard());
                currentPlayer.addScore(cardCommonTarget.getScoringToken());
            }
        }
    }
    /**
     * method that checks if the player shelf is full,and if it is, begins the last turn and adds the score of the EndGameToken to the player score
     *
     * @param currentPlayer The player currently playing
     */
    public void isShelfFull(Player currentPlayer) {
        if (currentPlayer.isShelfFull()) {
            setLastTurn(true);
            board.takeEndGameToken();
            currentPlayer.addScore(1);

        }
    }

    /**
     * Retrieves the game board.
     *
     * @return The 2D array representing the game board.
     */
    public TileSlot[][] getBoard(){
        return board.getBoard();
    }


    /**
     * Retrieves the list of common targets.
     *
     * @return The ArrayList of CardCommonTarget cards
     */
    public ArrayList<CardCommonTarget> getCommonTargets(){
        return commonDeck;
    }



    /**
     * Retrieves the current game state.
     *
     * @return The GameState object representing the current game state.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Sets the game state.
     *
     * @param gameState The GameState object representing the new game state.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Checks if it is the last turn.
     *
     * @return true if it is the last turn, false otherwise.
     */
    public boolean isLastTurn() {
        return isLastTurn;
    }

    /**
     * Sets the last turn status.
     *
     * @param lastTurn true if it is the last turn, false otherwise.
     */
    public void setLastTurn(boolean lastTurn) {
        isLastTurn = lastTurn;
    }

    /**
     * Checks if the end game token has been taken.
     *
     * @return true if the end game token has been taken, false otherwise.
     */
    public boolean isEndGameTokenTaken(){
        return board.isEndGameTokenTaken();
    }
}

