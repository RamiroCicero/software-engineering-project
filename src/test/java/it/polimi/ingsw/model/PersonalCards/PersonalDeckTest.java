package it.polimi.ingsw.model.PersonalCards;

import org.junit.jupiter.api.Test;

public class PersonalDeckTest {
    @Test
    void personalDeckTest() {
        PersonalDeck personalDeck = new PersonalDeck(2);
        System.out.println(personalDeck.getPersonalDeck().get(1).personalCardTiles()[0].colourTile());
        System.out.println(personalDeck.getPersonalDeck().get(1).id());
    }

}