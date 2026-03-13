package it.polimi.ingsw.model.CommonCards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;

public class CommonDeckTest {

    @Test
    void twoDifferentCards() {
        CommonDeck deck1 = new CommonDeck(2);
        CardCommonTarget carta1 = deck1.getCommonDeck().get(0);
        CardCommonTarget carta2 = deck1.getCommonDeck().get(1);
        System.out.println(carta2.getCommonType().getId());
        System.out.println(carta1.getCommonType().getId());
        assertNotSame(carta1.getCommonType(), carta2.getCommonType());
    }
}