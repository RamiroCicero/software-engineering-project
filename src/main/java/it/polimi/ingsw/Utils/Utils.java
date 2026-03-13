package it.polimi.ingsw.Utils;

import it.polimi.ingsw.model.CommonCards.CardCommonTarget;
import it.polimi.ingsw.model.PersonalCards.CardPersonalTarget;
import it.polimi.ingsw.model.PersonalCards.PersonalCardTile;
import it.polimi.ingsw.model.Player.Shelf;
import it.polimi.ingsw.model.Tile.ColourTile;
import it.polimi.ingsw.model.Tile.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Utils implements Serializable {
    public static final int MAX_SHELF_COLUMNS = 5;
    private static final int MAX_SHELF_ROWS = 6;
    private boolean[][] visited = new boolean[6][5];

    /**
     * method that compares the player's shelf and the personalCard, and returns the number of the completed objectives (0,...,6)
     * the if statement is true if the shelf's TileSlot is NOT EMPTY and true if the tile in the TileSlot is the same color as the Personal Card (in position i)
     *
     * @param shelf              Shelf of the player
     * @param cardPersonalTarget his personal target card
     * @return completedGoals
     */


    public int checkPersonalTarget(Shelf shelf, CardPersonalTarget cardPersonalTarget) {
        int completedGoals = 0;


        for (int i = 0; i < 6; i++)
            if (!(shelf.getShelf()[cardPersonalTarget.personalCardTiles()[i].coordinates().getRow()][cardPersonalTarget.personalCardTiles()[i].coordinates().getColumn()].isFree()) && cardPersonalTarget.personalCardTiles()[i].colourTile() == shelf.getShelf()[cardPersonalTarget.personalCardTiles()[i].coordinates().getRow()][cardPersonalTarget.personalCardTiles()[i].coordinates().getColumn()].getAssignedTile().colour()) {
                completedGoals++;
            }

        return completedGoals;
    }

    /**
     * method that compares each shelf and the common targets to check if anyone got the points
     *
     * @param shelf     shelf of the player
     * @param commonCard common target cardd
     * @return
     */
    public boolean checkCommonTarget(Shelf shelf, CardCommonTarget commonCard) {

        TileSlot[][] shelfMatrix = shelf.getShelf();
        switch (commonCard.getCommonType()) {
            case SIX_GROUPS_OF_TWO -> {
                TileSlot[][] copy = copy(shelfMatrix);

                int found = 0;
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (checkGroupsOfTwo(copy[i][j], copy[i][j + 1])) {
                            found++;
                            if (found == 6) return true;
                        }
                        if (checkGroupsOfTwo(copy[i][j], copy[i + 1][j])) {
                            found++;
                            if (found == 6) return true;
                        }

                    }
                }
                for (int i = 0; i < 5; i++) {
                    if (checkGroupsOfTwo(copy[i][4], copy[i + 1][4])) {
                        found++;
                        if (found == 6) return true;
                    }

                }
                for (int j = 0; j < 4; j++) {
                    if (checkGroupsOfTwo(copy[5][j], copy[5][j + 1])) {
                        found++;
                        if (found == 6) return true;
                    }
                }
                return false;


            }
            case FOUR_EQUALS_ANGLES -> {
                if ((!shelfMatrix[0][0].isFree()) && (!shelfMatrix[MAX_SHELF_ROWS - 1][0].isFree()) && (!shelfMatrix[MAX_SHELF_ROWS - 1][MAX_SHELF_COLUMNS - 1].isFree()) && (!shelfMatrix[0][MAX_SHELF_COLUMNS - 1].isFree())) {
                    return (shelfMatrix[0][0].getAssignedTile().colour() == shelfMatrix[MAX_SHELF_ROWS - 1][0].getAssignedTile().colour()) && (shelfMatrix[0][MAX_SHELF_COLUMNS - 1].getAssignedTile().colour() == shelfMatrix[MAX_SHELF_ROWS - 1][MAX_SHELF_COLUMNS - 1].getAssignedTile().colour())
                            && (shelfMatrix[0][MAX_SHELF_COLUMNS - 1].getAssignedTile().colour() == shelfMatrix[MAX_SHELF_ROWS - 1][0].getAssignedTile().colour());
                }
            }

            case FOUR_GROUPS_OF_FOUR -> {
                TileSlot[][] copy = copy(shelfMatrix);
                int found = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 2; j++) {
                        if (checkGroupsOfFour(copy[i][j], copy[i][j + 1], copy[i][j + 2], copy[i][j + 3])) {
                            found++;
                            if (found == 4) return true;
                        }
                        if (checkGroupsOfFour(copy[i][j], copy[i + 1][j], copy[i + 2][j], copy[i + 3][j])) {
                            found++;
                            if (found == 4) return true;
                        }

                    }
                }
                for (int i = 3; i < 6; i++) {
                    for (int j = 0; j < 2; j++) {
                        if (checkGroupsOfFour(copy[i][j], copy[i][j + 1], copy[i][j + 2], copy[i][j + 3])) {
                            found++;
                            if (found == 4) return true;
                        }
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 2; j < 5; j++) {
                        if (checkGroupsOfFour(copy[i][j], copy[i + 1][j], copy[i + 2][j], copy[i + 3][j])) {
                            found++;
                            if (found == 4) return true;
                        }
                    }
                }
                for (int i = 0; i < MAX_SHELF_ROWS - 1; i++) {
                    for (int j = 0; j < MAX_SHELF_COLUMNS - 1; j++) {
                        if (checkGroupsOfFour(copy[i][j], copy[i + 1][j], copy[i][j + 1], copy[i + 1][j + 1])) {
                            found++;
                            if (found == 4) return true;
                        }
                    }
                }
                return false;
            }

            case TWO_GROUPS_IN_SQUARE -> {
                HashSet<ColourTile> differentColours = new HashSet<ColourTile>();
                TileSlot[][] copy = copy(shelfMatrix);

                ColourTile colour = null;
                for (int i = 0; i < MAX_SHELF_ROWS - 1; i++) {
                    for (int j = 0; j < MAX_SHELF_COLUMNS - 1; j++) {
                        if (!copy[i][j].isFree()) {
                            colour = copy[i][j].getAssignedTile().colour();
                        }
                        if (checkGroupsOfFour(copy[i][j], copy[i + 1][j], copy[i][j + 1], copy[i + 1][j + 1])) {
                            if (differentColours.isEmpty() && colour != null) differentColours.add(colour);
                            else {
                                if (differentColours.contains(colour)) return true;
                                else differentColours.add(colour);
                            }
                        }
                    }
                }
                return false;
            }

            case THREE_COLUMNS_THREE_DIFFERENT_TYPES -> {
                int found = 0;
                for (int i = 0; i < MAX_SHELF_COLUMNS; i++) {
                    TileSlot[] temp = new TileSlot[MAX_SHELF_ROWS];
                    for (int j = 0; j < MAX_SHELF_ROWS; j++) {
                        temp[j] = shelfMatrix[j][i];
                    }
                    int numbDifferent = checkAllDifferent(temp, "COLUMN");
                    if ((numbDifferent > 0) && (numbDifferent < 4)) {
                        found++;

                        if (found > 2) {
                            return true;
                        }
                    }
                }

            }
            case EIGHT_EQUALS -> {

                for (int x = 0; x < MAX_SHELF_ROWS; x++) {
                    for (int i = 0; i < MAX_SHELF_COLUMNS; i++) {

                        int count = 0;

                        for (int k = x; k < MAX_SHELF_ROWS; k++) {
                            for (int j = i; j < MAX_SHELF_COLUMNS; j++) {
                                if (((!shelfMatrix[x][i].isFree()) && (!shelfMatrix[k][j].isFree())) && (shelfMatrix[x][i].getAssignedTile().colour() == shelfMatrix[k][j].getAssignedTile().colour())) {
                                    count++;
                                    if (count == 8) return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }

            case FIVE_IN_DIGONAL -> {
                Coordinates firstDiagonal = new Coordinates(0, 0);
                Coordinates secondDiagonal = new Coordinates(1, 0);
                Coordinates thirdDiagonal = new Coordinates(1, 4);
                Coordinates fourthDiagonal = new Coordinates(0, 4);
                return ((checkDiagonal(shelfMatrix, firstDiagonal)) || (checkDiagonal(shelfMatrix, secondDiagonal)) || (checkDiagonal(shelfMatrix, thirdDiagonal)) || (checkDiagonal(shelfMatrix, fourthDiagonal)));
            }
            case FOUR_ROWS_THREE_DIFFERENT_TYPES -> {
                int found = 0;

                for (int i = 0; i < MAX_SHELF_ROWS; i++) {

                    int numbDifferent = checkAllDifferent(shelfMatrix[i], "ROW");
                    if ((numbDifferent > 0) && (numbDifferent < 4)) {
                        found++;

                        if (found > 3) {
                            return true;
                        }
                    }
                }


            }


            case TWO_COLUMNS_ALL_DIFFERENT -> {
                int found = 0;
                for (int i = 0; i < MAX_SHELF_COLUMNS; i++) {
                    TileSlot[] temp = new TileSlot[MAX_SHELF_ROWS];
                    for (int j = 0; j < MAX_SHELF_ROWS; j++) {
                        temp[j] = shelfMatrix[j][i];
                    }
                    if (checkAllDifferent(temp, "COLUMN") == MAX_SHELF_ROWS) {
                        found++;

                        if (found > 1) {
                            return true;
                        }
                    }
                }


            }
            case TWO_ROWS_ALL_DIFFERENT -> {
                int found = 0;
                for (int i = 0; i < MAX_SHELF_ROWS; i++) {
                    if (checkAllDifferent(shelfMatrix[i], "ROW") == MAX_SHELF_COLUMNS) {
                        found++;

                        if (found > 1) {
                            return true;
                        }
                    }
                }

            }


            case FIVE_IN_A_X -> {

                for (int i = 1; i < 4; i++) {
                    for (int j = 1; j < MAX_SHELF_COLUMNS - 1; j++) {
                        if (((!shelfMatrix[i][j].isFree()) && (!shelfMatrix[i + 1][j + 1].isFree()) && (!shelfMatrix[i - 1][j - 1].isFree()) && (!shelfMatrix[i + 1][j - 1].isFree()) && (!shelfMatrix[i - 1][j + 1].isFree())) &&
                                (shelfMatrix[i][j].getAssignedTile().colour() == shelfMatrix[i + 1][j + 1].getAssignedTile().colour()
                                        && shelfMatrix[i][j].getAssignedTile().colour() == shelfMatrix[i - 1][j - 1].getAssignedTile().colour()
                                        && shelfMatrix[i][j].getAssignedTile().colour() == shelfMatrix[i - 1][j + 1].getAssignedTile().colour()
                                        && shelfMatrix[i][j].getAssignedTile().colour() == shelfMatrix[i + 1][j - 1].getAssignedTile().colour())) {
                            return true;
                        }
                    }
                }


                return false;


            }
            case IN_DESCENDING_ORDER -> {

                int first = 1;

                if ((!shelfMatrix[0][0].isFree() && shelfMatrix[1][0].isFree()) || (!shelfMatrix[1][0].isFree() && shelfMatrix[2][0].isFree())) {
                    first = 0;
                } else if ((!shelfMatrix[0][4].isFree() && shelfMatrix[1][4].isFree()) || (!shelfMatrix[1][4].isFree() && shelfMatrix[2][4].isFree())) {
                    first = 4;
                }


                if (first == 1) return false;


                if (first == 0) {
                    for (int j = 0; j < MAX_SHELF_COLUMNS - 1; j++) {
                        if (countFull(shelfMatrix, j) + 1 != countFull(shelfMatrix, j + 1)) return false;
                    }
                } else {
                    for (int j = 4; j > 0; j--) {
                        if (countFull(shelfMatrix, j) + 1 != countFull(shelfMatrix, j - 1)) return false;
                    }
                }
                return true;
            }

            default -> throw new IllegalStateException("Unexpected value: " + commonCard.getCommonType());
        }
        return false;
    }

    /**
     * method that, given a column j, counts if the column is full
     *
     * @param shelfMatrix player's shelf
     * @param j given column
     * @return numbers of full rows in the column
     */
    private int countFull(TileSlot[][] shelfMatrix, int j) {
        int i = 0;
        while (i < 6 && !shelfMatrix[i][j].isFree()) i++;
        return i;
    }

    /**
     * method that checks if the tiles in a row or column are all different ( used in checkCommonTarget )
     *
     * @param shelfMatrix player's shelf
     * @param type row or column
     * @return numColours
     */
    public int checkAllDifferent(TileSlot[] shelfMatrix, String type) {

        int numColours = 0;

        HashSet<ColourTile> differentColours = new HashSet<ColourTile>();

        if (type.equals("ROW")) {

            for (int i = 0; i < MAX_SHELF_COLUMNS; i++) {
                if (shelfMatrix[i].isFree()) return 0;
                differentColours.add(shelfMatrix[i].getAssignedTile().colour());
            }

            numColours = differentColours.size();

            return numColours;

        }


        if (type.equals("COLUMN")) {

            for (int i = 0; i < MAX_SHELF_ROWS; i++) {
                if (shelfMatrix[i].isFree()) return 0;
                differentColours.add(shelfMatrix[i].getAssignedTile().colour());
            }

            numColours = differentColours.size();

            return numColours;

        }

        return 0;
    }


    /**
     * method that checks, for all four possible diagonals, if the tiles are the same colour
     *
     * @param shelfMatrix player's shelf
     * @param coordinates coordinates of the first tile of the diagonal
     * @return true if all tiles are different
     */
    public boolean checkDiagonal(TileSlot[][] shelfMatrix, Coordinates coordinates) {


        if (coordinates.getRow() == 0 && coordinates.getColumn() == 0) {
            for (int i = 0; i < MAX_SHELF_COLUMNS - 1; i++) {
                if ((shelfMatrix[i][i].isFree()) || (shelfMatrix[i + 1][i + 1].isFree()) || (shelfMatrix[i][i].getAssignedTile().colour() != shelfMatrix[i + 1][i + 1].getAssignedTile().colour())) {
                    return false;
                }
            }
            return true;
        }


        if (coordinates.getRow() == 1 && coordinates.getColumn() == 0) {

            for (int i = 0; i < MAX_SHELF_COLUMNS - 1; i++) {
                if ((shelfMatrix[i + 1][i].isFree()) || (shelfMatrix[i + 2][i + 1].isFree()) || (shelfMatrix[i + 1][i].getAssignedTile().colour() != shelfMatrix[i + 2][i + 1].getAssignedTile().colour())) {
                    return false;
                }
            }
            return true;
        }


        if (coordinates.getRow() == 1 && coordinates.getColumn() == 4) {
            for (int i = 1; i < MAX_SHELF_ROWS - 1; i++) {
                if (((!shelfMatrix[i][coordinates.getColumn()].isFree()) && (!shelfMatrix[i + 1][coordinates.getColumn() - 1].isFree())) &&
                        (shelfMatrix[i][coordinates.getColumn()].getAssignedTile().colour() == shelfMatrix[i + 1][coordinates.getColumn() - 1].getAssignedTile().colour())) {

                    coordinates.setColumn(coordinates.getColumn() - 1);

                } else {
                    return false;
                }
            }
            return true;
        }


        if (coordinates.getRow() == 0 && coordinates.getColumn() == 4) {
            for (int i = 0; i < MAX_SHELF_COLUMNS - 1; i++) {
                if (((!shelfMatrix[i][coordinates.getColumn()].isFree()) && (!shelfMatrix[i + 1][coordinates.getColumn() - 1].isFree())) &&
                        (shelfMatrix[i][coordinates.getColumn()].getAssignedTile().colour() == (shelfMatrix[i + 1][coordinates.getColumn() - 1].getAssignedTile().colour()))) {

                    coordinates.setColumn(coordinates.getColumn() - 1);

                } else {

                    return false;
                }
            }
            return true;
        }

        return false;


    }


    /**
     * method that checks if four adjacent tiles are the same colour
     *
     * @param tileSlot1
     * @param tileSlot2
     * @param tileSlot3
     * @param tileSlot4
     * @return true if they are all the same colour
     */
    public boolean checkGroupsOfFour(TileSlot tileSlot1, TileSlot tileSlot2, TileSlot tileSlot3, TileSlot tileSlot4) {
        if (!(tileSlot1.isFree()) && !(tileSlot2.isFree()) && !(tileSlot3.isFree()) && !(tileSlot4.isFree()) &&
                tileSlot1.getAssignedTile().colour() == tileSlot2.getAssignedTile().colour() &&
                tileSlot1.getAssignedTile().colour() == tileSlot3.getAssignedTile().colour() &&
                tileSlot1.getAssignedTile().colour() == tileSlot4.getAssignedTile().colour()) {

            tileSlot1.removeAssignedTile();
            tileSlot2.removeAssignedTile();
            tileSlot3.removeAssignedTile();
            tileSlot4.removeAssignedTile();
            return true;
        }
        return false;
    }

    /**
     * method that checks if two adjacent tiles are the same colour
     *
     * @param tileSlot1
     * @param tileSlot2
     * @return true if they are all the same colour
     */
    public boolean checkGroupsOfTwo(TileSlot tileSlot1, TileSlot tileSlot2) {
        if (!(tileSlot1.isFree()) && !(tileSlot2.isFree()) &&
                tileSlot1.getAssignedTile().colour() == tileSlot2.getAssignedTile().colour()) {

            tileSlot1.removeAssignedTile();
            tileSlot2.removeAssignedTile();
            return true;
        }
        return false;
    }

    /**
     * method that counts how many points each player got with the common target cards
     *
     * @param shelf player's shelf
     * @return player's points
     */
    public int groupScore(Shelf shelf) {

        ArrayList<Integer> points = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            for(int j = 0; j< 5; j++){
                if(!visited[i][j] && !shelf.getShelf()[i][j].isFree()){
                    points.add(goRight(j+1,i, shelf.getShelf()[i][j].getAssignedTile().colour(), shelf.getShelf())
                               + goUp(j,i,shelf.getShelf()[i][j].getAssignedTile().colour(), shelf.getShelf())
                               + goDown(j,i-1,shelf.getShelf()[i][j].getAssignedTile().colour(), shelf.getShelf())
                               + goLeft(j-1,i,shelf.getShelf()[i][j].getAssignedTile().colour(), shelf.getShelf()));
                }

            }

        }
        int RealPoints = 0;
        for (Integer point : points) {
            if (point == 3) RealPoints = RealPoints + 2;
            if (point == 4) RealPoints = RealPoints + 3;
            if (point == 5) RealPoints = RealPoints + 5;
            if (point > 5) RealPoints = RealPoints + 8;
        }

        System.out.println("i punti della group score sono " + RealPoints);

        return RealPoints ;
    }

    private int goUp(int x, int y, ColourTile colour, TileSlot[][] shelf) {
        return condition(x, y, colour, shelf);
    }

    private int goRight(int x, int y, ColourTile colour, TileSlot[][] shelf) {
        return condition(x, y, colour, shelf);
    }


    private int goDown(int x, int y, ColourTile colour, TileSlot[][] shelf) {
        return condition(x, y, colour, shelf);
    }

    private int goLeft(int x, int y, ColourTile colour, TileSlot[][] shelf) {
        return condition(x, y, colour, shelf);
    }

    private int condition(int x, int y, ColourTile colour, TileSlot[][] shelf) {
        if( x>4 || y >5 || x<0 || y <0 || shelf[y][x].isFree() || colour != shelf[y][x].getAssignedTile().colour() || visited[y][x]){
            return 0;
        }
        visited[y][x] = true;
        return 1 + goRight(x+1, y, colour, shelf) +
                + goDown(x, y-1, colour, shelf) +
                + goLeft(x-1, y, colour, shelf) +
                + goUp(x, y+1, colour, shelf) ;
    }


//
    /**
     * method created for testing
     *
     * @param shelf
     * @param personalCardTiles
     */
    public void shelfDebug(Shelf shelf, PersonalCardTile[] personalCardTiles) {
        for (PersonalCardTile personalCardTile : personalCardTiles) {
            shelf.getShelf()[personalCardTile.coordinates().getRow()][personalCardTile.coordinates().getColumn()].assignTile(new Tile(personalCardTile.colourTile()));
        }
    }

    /**
     * Creates a copy of the given shelf matrix.
     *
     * @param shelfMatrix The shelf matrix to copy.
     * @return The copied shelf matrix.
     */
    public TileSlot[][] copy(TileSlot[][] shelfMatrix) {
        TileSlot[][] copy = new TileSlot[6][5];

        for (int i = 0; i < MAX_SHELF_ROWS; i++) {
            for (int j = 0; j < MAX_SHELF_COLUMNS; j++) {
                copy[i][j] = new TileSlot();
            }
        }
        for (int i = 0; i < MAX_SHELF_ROWS; i++) {
            for (int j = 0; j < MAX_SHELF_COLUMNS; j++) {
                if (!shelfMatrix[i][j].isFree())
                    copy[i][j].assignTile(new Tile(shelfMatrix[i][j].getAssignedTile().colour()));
            }
        }
        return copy;
    }

}

