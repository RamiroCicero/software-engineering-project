package it.polimi.ingsw.model.CommonCards;

import java.io.Serializable;

/**
 * Enum for distinguish the common goal cards, used by Utils to generate the corresponding card
 */
public enum CommonList implements Serializable {

    SIX_GROUPS_OF_TWO(4),
    FOUR_EQUALS_ANGLES(8),
    FOUR_GROUPS_OF_FOUR(3),
    TWO_GROUPS_IN_SQUARE(1),
    THREE_COLUMNS_THREE_DIFFERENT_TYPES(5),
    EIGHT_EQUALS(9),
    FIVE_IN_DIGONAL(11),
    FOUR_ROWS_THREE_DIFFERENT_TYPES(7),
    TWO_COLUMNS_ALL_DIFFERENT(2),
    TWO_ROWS_ALL_DIFFERENT(6),
    FIVE_IN_A_X(10),
    IN_DESCENDING_ORDER(12);

    private final int id;

    CommonList(int id) {
        this.id=id;
    }

    /**
     * getter of the id of the CommonList
     *
     * @return CommonList id
     */
    public int getId() {
        return id;
    }
}
