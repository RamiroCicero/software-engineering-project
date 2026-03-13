package it.polimi.ingsw.Utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class for a pair of Integer that represents the coordinates of a Tile in the library or on the board
 */
public class Coordinates implements Serializable {
    /**
     * Coordinate of the row
     */
    private Integer Row;
    /**
     * Coordinate of the column
     */
    private Integer Column;


    /**
     * Constructs a new Coordinates object with the specified row and column.
     *
     * @param Row    The coordinate of the row.
     * @param Column The coordinate of the column.
     */
    public Coordinates(Integer Row, Integer Column) {
        this.Row = Row;
        this.Column = Column;
    }

    /**
     * Returns the coordinate of the row.
     *
     * @return The coordinate of the row.
     */
    public Integer getRow() {
        return Row;
    }

    /**
     * Sets the coordinate of the row.
     *
     * @param row The coordinate of the row.
     */
    public void setRow(int row) {
        this.Row = row;
    }

    /**
     * Returns the coordinate of the column.
     *
     * @return The coordinate of the column.
     */
    public Integer getColumn() {
        return Column;
    }

    /**
     * Sets the coordinate of the column.
     *
     * @param column The coordinate of the column.
     */
    public void setColumn(int column) {
        this.Column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Objects.equals(this.Row, that.Row) && Objects.equals(this.Column, that.Column);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Row, Column);
    }
}
