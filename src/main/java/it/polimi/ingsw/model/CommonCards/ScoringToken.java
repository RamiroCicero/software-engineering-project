package it.polimi.ingsw.model.CommonCards;

import java.io.Serializable;

public class ScoringToken implements Serializable {
    /**
     * boolean assignedCommonCard: identifies the first or second common card of the board
     */
    int assignedCommonCard;
    /**
     * int valueToken : value of the ScoringToken (2 or 4 or 6 or 8)
     */
    int valueToken;

    /**
     * constructor of ScoringToken that assign the parameters to assignedCommonCard and valueToken
     *
     * @param assignedCommonCard identifies the first or second common card of the board
     * @param valueToken         value of the ScoringToken
     */
    public ScoringToken(int assignedCommonCard, int valueToken) {
        this.assignedCommonCard = assignedCommonCard;
        this.valueToken = valueToken;
    }

    /**
     * getter of ValueToken
     *
     * @return valueToken
     */

    public int getValueToken() {
        return valueToken;
    }
}
