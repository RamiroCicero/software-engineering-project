package it.polimi.ingsw.model.PersonalCards;

import java.io.Serializable;

/**
 * class (record) that contains personal card information
 * @param personalCardTiles -> vector of six containing the coordinates with their respective colors
 * @param id unique identifier of the card
 */

public record CardPersonalTarget(PersonalCardTile[] personalCardTiles, int id) implements Serializable {

}
