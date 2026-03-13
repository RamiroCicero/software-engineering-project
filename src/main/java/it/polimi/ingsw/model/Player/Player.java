package it.polimi.ingsw.model.Player;

import it.polimi.ingsw.Exception.NoSpaceInColumnException;
import it.polimi.ingsw.Utils.Utils;
import it.polimi.ingsw.model.PersonalCards.CardPersonalTarget;
import it.polimi.ingsw.model.Tile.Tile;

import java.io.Serializable;

/**
 * Class that represent the Player
 */
public class Player implements Serializable {
    private final String nickname;
    private final Shelf personalShelf;
    private final boolean[] completedCommon;
    private final Utils utils;
    private CardPersonalTarget cardPersonalTarget;
    private int score;
    private boolean isFirstPlayer = false;


    /**
     * Constructor that assigns to the player a nickname, personalTarget and a boolean indicating if the player is the first one
     */
    public Player(String nickname) {
        this.utils = new Utils();
        this.nickname = nickname;
        this.personalShelf = new Shelf();
        this.score = 0;
        this.completedCommon = new boolean[]{false, false};
    }

    /**
     * getter of player's nickname
     *
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * getter of personal target
     *
     * @return cardPersonalTarget
     */
    public CardPersonalTarget getCardPersonalTarget() {
        return cardPersonalTarget;
    }

    /**
     * Method that stores and modifies score value
     */
    public void addScore(int addedScore) {
        this.score = score + addedScore;
    }

    /**
     * Method that allows the player to add the selected tiles from the board to the library
     *
     * @param col          chosen column
     * @param selectedTile array of selected tiles
     */
    public void addTilesInLibrary(int col, Tile[] selectedTile) throws NoSpaceInColumnException {
        personalShelf.addCardInColumn(col, selectedTile);
    }


    /**
     * method that calls the checkPersonalTarget of utils,
     * and transforms the value returned by the latter into points, finally adds these points to score
     */
    public void checkPersonalTarget() {
        int[] points = {0, 1, 2, 4, 6, 9, 12};
        int check = utils.checkPersonalTarget(personalShelf, cardPersonalTarget);
        if (check > 0) {
            addScore(points[check]);
        }
    }

    /**
     * sets personal card
     */
    public void setPersonalCard(CardPersonalTarget personalCard) {
        this.cardPersonalTarget = personalCard;
    }

    /**
     * sets first player
     */
    public void setFirstPlayer() {
        this.isFirstPlayer = true;
    }

    /**
     * Checks if the player is the first one.
     *
     * @return true if the player is the first one, false otherwise.
     */
    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    /**
     * Checks if the shelf is full.
     *
     * @return true if the shelf is full, false otherwise.
     */
    public boolean isShelfFull() {
        return personalShelf.isFull();
    }

    /**
     * Retrieves the player's personal shelf.
     *
     * @return the personal shelf.
     */
    public Shelf getPersonalShelf() {
        return personalShelf;
    }

    /**
     * Sets the completion status of a common goal.
     *
     * @param objective the objective to set as completed.
     */
    public void setCompleted(int objective) {
        completedCommon[objective] = true;
    }

    /**
     * Checks if a common goal is completed.
     *
     * @param objective the objective to check.
     * @return true if the common goal is completed, false otherwise.
     */
    public boolean isCompleted(int objective) {
        return completedCommon[objective];
    }

    /**
     * Retrieves the score of the player.
     *
     * @return the player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Updates the player's score by adding the group score of their personal shelf.
     */
    public void groupScore() {
        addScore(utils.groupScore(personalShelf));
    }
}


