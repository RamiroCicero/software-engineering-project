package it.polimi.ingsw.model.PersonalCards;


import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.view.cli.ColorCodes.getColorCode;


public class CardPersonalTargetTest {
    private static final String TEXT_BLACK = "\u001B[30m";
    private final String ANSI_RESET = "\u001B[0m";
    @Test
    public void printPersonalTest(){
        PersonalDeck personalDeck=new PersonalDeck(4);
        for (CardPersonalTarget cardPersonalTarget:personalDeck.getPersonalDeck()){
            System.out.println(cardPersonalTarget.id());
            System.out.println("\n");
            printPersonalTargets(cardPersonalTarget);
            System.out.println("\n");
        }
    }

    public void printPersonalTargets(CardPersonalTarget cardPersonalTarget) {
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 5; j++) {
                int found = 0;
                for (int z = 0; z < 6; z++) {
                    if (cardPersonalTarget.personalCardTiles()[z].coordinates().getRow() == i &&
                            cardPersonalTarget.personalCardTiles()[z].coordinates().getColumn() == j) {
                        System.out.print(getColorCode(cardPersonalTarget.personalCardTiles()[z].colourTile()) + "*** " + ANSI_RESET);
                        found = 1;
                    }
                }
                if (found == 0) {
                    System.out.print(TEXT_BLACK + "*** " + ANSI_RESET);

                }

            }
            System.out.print("\n");

        }
    }
}