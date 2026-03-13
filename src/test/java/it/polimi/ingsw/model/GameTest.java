package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Player.Player;
import org.junit.jupiter.api.Test;

public class GameTest {

    static Game FullGame() {
        Game game = new Game();
        Player player = new Player("Nicola");
        Player player1 = new Player("Ramiro");
        Player player2 = new Player("Margherita");
        Player player3 = new Player("Alessandra");
        return game;
    }


    @Test
    void gameInit() {
    }


    @Test
    void placeInShelf() {
    }

    @Test
    void checkCommonTarget() {
    }

    @Test
    void checkPersonalTarget() {
    }

    @Test
    void isShelfFull() {
    }

    @Test
    void getPlayers() {
    }

    @Test
    void getGameState() {
    }

    @Test
    void setGameState() {
    }

    @Test
    void isLastTurn() {
    }

    @Test
    void setLastTurn() {
    }
}