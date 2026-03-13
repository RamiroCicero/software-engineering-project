package it.polimi.ingsw.model.CommonCards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class CommonDeck implements Serializable {
    /**
     * ArrayList CardCommonTarget commonDeck : ArrayList of commonCard (length -> 2)
     */
    private final ArrayList<CardCommonTarget> commonDeck;


    /**
     * Method for create two CommonTargetCards randomly picked from the ones that are available
     *
     * @param numOfPlayers used only for passing it in the common target constructor to decide the number of scoring token on each card
     */
    public CommonDeck(int numOfPlayers) {

        commonDeck = new ArrayList<>();
        Random random = new Random();
        int num1 = random.nextInt(12);
        int num2;
        do {
            num2 = random.nextInt(12);
        }
        while (num2 == num1);

        String[] commonList = {"SIX_GROUPS_OF_TWO",
                "FOUR_EQUALS_ANGLES",
                "FOUR_GROUPS_OF_FOUR",
                "TWO_GROUPS_IN_SQUARE",
                "THREE_COLUMNS_THREE_DIFFERENT_TYPES",
                "EIGHT_EQUALS",
                "FIVE_IN_DIGONAL",
                "FOUR_ROWS_THREE_DIFFERENT_TYPES",
                "TWO_COLUMNS_ALL_DIFFERENT",
                "TWO_ROWS_ALL_DIFFERENT",
                "FIVE_IN_A_X",
                "IN_DESCENDING_ORDER"};

        commonDeck.add(new CardCommonTarget(CommonList.valueOf(commonList[num1]), 0, numOfPlayers));
        commonDeck.add(new CardCommonTarget(CommonList.valueOf(commonList[num2]), 1, numOfPlayers));
    }

    /**
     * getter of the CommonCards Arraylist
     *
     * @return commonDeck
     */
    public ArrayList<CardCommonTarget> getCommonDeck() {
        return commonDeck;
    }
}
