package it.polimi.ingsw.model.PersonalCards;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class PersonalDeck implements Serializable {
    /**
     * ArrayList CardPersonalTarget personalDeck : arrayList that contains (numOfPlayers) personalCards
     */
    private final ArrayList<CardPersonalTarget> personalDeck;
    /**
     * parser (JSON) of personalCards
     */
    PersonalParser personalParser;

    /**
     * constructor of PersonalDeck that extracts the (numOfPlayers) personalCards
     */
    public PersonalDeck(int numOfPlayers) {
        try {
            personalParser = new PersonalParser();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        personalDeck = new ArrayList<>();

        Random random = new Random();

        //extraction of 4 different casual numbers

        int[] numbers = new int[4];
        numbers[0] = random.nextInt(12);
        numbers[1] = random.nextInt(12);

        while (numbers[0] == numbers[1]) numbers[1] = random.nextInt(12);
        numbers[2] = random.nextInt(12);

        while (numbers[2] == numbers[0] || numbers[2] == numbers[1]) numbers[2] = random.nextInt(12);
        numbers[3] = random.nextInt(12);

        while (numbers[3] == numbers[0] || numbers[3] == numbers[1] || numbers[3] == numbers[2])
            numbers[3] = random.nextInt(12);

        for (int i = 0; i < numOfPlayers; i++) {
            personalDeck.add(personalParser.getCardPersonalTargets()[numbers[i]]);
        }


    }

    /**
     * getter of the personalDeck
     *
     * @return personalDeck
     */
    public ArrayList<CardPersonalTarget> getPersonalDeck() {
        return personalDeck;
    }

}
