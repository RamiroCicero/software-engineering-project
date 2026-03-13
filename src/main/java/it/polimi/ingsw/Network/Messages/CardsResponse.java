package it.polimi.ingsw.Network.Messages;

import it.polimi.ingsw.model.CommonCards.CardCommonTarget;
import it.polimi.ingsw.model.PersonalCards.CardPersonalTarget;

import java.util.ArrayList;

/**
 * The CardsResponse class represents a response containing the available cards information.
 */
public class CardsResponse extends Message{
    private final ArrayList<CardCommonTarget> commonTargets;
    private final CardPersonalTarget cardPersonalTarget;
    private String typeMessage ;
    /**
     * Constructs a CardsResponse object with the specified game ID, unique identifier,
     * list of common targets, and personal target card.
     *
     * @param gameID             The ID of the game.
     * @param UID                The unique identifier of the client.
     * @param commonTargets      An ArrayList of CardCommonTarget objects representing the available common targets.
     * @param cardPersonalTarget A CardPersonalTarget object representing the personal target card.
     */
    public CardsResponse(int gameID, Long UID, ArrayList<CardCommonTarget> commonTargets, CardPersonalTarget cardPersonalTarget ) {
        super(gameID, UID);
        this.commonTargets = commonTargets;
        this.cardPersonalTarget = cardPersonalTarget;
        this.typeMessage = "CardsResponse";
    }
    /**
     * Returns the personal target card.
     *
     * @return The personal target card.
     */
    public CardPersonalTarget getCardPersonalTarget() {
        return cardPersonalTarget;
    }

    /**
     * Returns the list of common targets.
     *
     * @return An ArrayList of CardCommonTarget objects representing the available common targets.
     */
    public ArrayList<CardCommonTarget> getCommonTargets() {
        return commonTargets;
    }

    @Override
    public String typeMessage() {
        return "CardsResponse";
    }
}
