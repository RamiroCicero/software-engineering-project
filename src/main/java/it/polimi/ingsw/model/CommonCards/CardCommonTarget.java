package it.polimi.ingsw.model.CommonCards;

import java.io.Serializable;
import java.util.Stack;

/**
 * class that represents the type of common Target based on the number of players
 */
public class CardCommonTarget implements Serializable {
    /**
     *  assignedCommonCard: identifies the first or second common card of the board
     */

    private final int assignedCommonCard;

    /**
     * CommonList typeOfCommonCard: identifies the goal of the common card
     */

    private final CommonList commonType;

    /**
     * Stack ScoringToken stackToken : stack of scoring tokens
     */
    private final Stack<ScoringToken> stackToken;
    /**
     * int for tracking the highest token
     */
    private int highestToken=8;



    /**
     * constructor that assign to the card:
     *
     * @param commonType         enum value of the common card
     * @param assignedCommonCard which one of the two card the scoring token is assigned to
     */
    public CardCommonTarget(CommonList commonType, int assignedCommonCard, int numOfPlayers) {


        stackToken = new Stack<>();

        this.commonType = commonType;

        this.assignedCommonCard = assignedCommonCard;


        if (numOfPlayers == 2) {

            stackToken.push(new ScoringToken(assignedCommonCard, 6));
            stackToken.push(new ScoringToken(assignedCommonCard, 8));
        }


        if (numOfPlayers == 3) {

            stackToken.push(new ScoringToken(assignedCommonCard, 4));
            stackToken.push(new ScoringToken(assignedCommonCard, 6));
            stackToken.push(new ScoringToken(assignedCommonCard, 8));
        }


        if (numOfPlayers == 4) {

            stackToken.push(new ScoringToken(assignedCommonCard, 2));
            stackToken.push(new ScoringToken(assignedCommonCard, 4));
            stackToken.push(new ScoringToken(assignedCommonCard, 6));
            stackToken.push(new ScoringToken(assignedCommonCard, 8));
        }
    }

    /**
     * getter of the CommonList value (enum) of the Scoring token  -> also removes the scoringToken from the stack
     *
     * @return stackToken.pop().getValueToken()  ->  scoringToken
     */
    public int getScoringToken() {
        highestToken=highestToken-2;
        return stackToken.pop().getValueToken();
    }

    /**
     * getter of the value of assignedCommonCard
     *
     * @return assignedCommonCard
     */
    public int getAssignedCommonCard() {
        return assignedCommonCard;
    }

    /**
     * getter of the type of the commonCard
     *
     * @return commonType
     */

    public CommonList getCommonType() {
        return commonType;
    }

    /**
     * @return the highest token value
     */
    public int getHighestToken() {
        return highestToken;
    }
}
