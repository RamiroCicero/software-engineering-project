package it.polimi.ingsw.controller;

import it.polimi.ingsw.Exception.*;
import it.polimi.ingsw.Utils.Coordinates;
import it.polimi.ingsw.Utils.TileSlot;
import it.polimi.ingsw.model.CommonCards.CardCommonTarget;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.PersonalCards.CardPersonalTarget;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.model.Tile.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class that Manages a Game
 */
public class GameController implements Serializable {
    /**
     * List of the player in this game
     */
    private List<Player> players;
    /**
     * the associated Game
     */
    private final Game game;
    /**
     * Int used to travel across the player list to determine which is the current player
     */
    private int turnChanger;
    /**
     * the player that is currently playing
     */
    private Player currentPlayer;
    private int numOfPlayers;
    private int maxPlayers;


    /**
     * Constructor of the GameController, that initialize an empty game, and also has a reference to the ChatController of the Game
     */
    GameController() {
        this.players = new ArrayList<>();
        this.game = new Game();
        this.turnChanger = 0;
        this.maxPlayers = -1;
    }


    /**
     * Method that adds a Player to the Players list, if possible
     *
     * @param nickname the nickname of the new player
     * @throws UsernameException  throw when the nickname is already taken and the player is still in game
     * @throws GameAlreadyStarted throw when the game is already started
     * @throws MaxPlayerException throw when the Game is already full
     */

    public void login(String nickname) throws UsernameException, GameAlreadyStarted, MaxPlayerException {
        Player newPlayer = new Player(nickname);

        if (game.getGameState() != GameState.WAITING_PLAYERS) throw new GameAlreadyStarted("Game already started");

        if (!players.isEmpty()) {
            if (isPlayerPresent(nickname)) throw new UsernameException("Username already taken");
        }

        players.add(newPlayer);

        this.numOfPlayers = players.size();
    }


    /**
     * Method to initialize the game when the players are ready and in sufficient number
     *
     * @throws GameAlreadyStarted    throw when the game is already started
     * @throws GameNotReadyException throw when the game has not sufficient players
     */
    public void initGame() throws GameNotReadyException, GameAlreadyStarted {
        if (!game.getGameState().equals(GameState.WAITING_PLAYERS))
            throw new GameAlreadyStarted("Game already started");

        pickFirstPlayer(players);
        game.GameInit(players);

        game.setGameState(GameState.IN_GAME);
        currentPlayer = players.get(turnChanger);
    }

    public TileSlot[][] getShelf(String nickname){
        return getPlayerByNickname(nickname).getPersonalShelf().getShelf();
    }

    /**
     * method to randomly select a player to start the placeInShelf and redefine the turns order
     */
    private void pickFirstPlayer(List<Player> players) {
        int first = (new Random()).nextInt(players.size());
        players.get(first).setFirstPlayer();

        ArrayList<Player> playerList = new ArrayList<>();

        for (int i = first; i < players.size(); ++i) {
            playerList.add(players.get(i));
        }

        for (int i = 0; i < first; ++i) {
            playerList.add(players.get(i));
        }

        this.players = playerList;
    }

    /**
     * Main method of the class, manages all the aspect of the current Player turn,adding the selected tiles in the shelf, removing them from the board and adjusting the score
     *
     * @throws EmptySlotException        throw if the selected slot on the board is empty
     * @throws InvalidPositionsException throw if the selected positions are invalid
     * @throws InvalidSlotException      throw if the slot on the board selected by the player is invalid
     * @throws NoSpaceInColumnException  throw when the selected column has less space than needed
     * @throws GameAlreadyStarted        throw when the game is already started
     * @throws EndGameException          if the Game is Ended
     * @throws SoldOutTilesException     if the Tiles in the Bag are ended
     */
    public void turn(Tile[] tilesToAdd, int column) throws EmptySlotException, InvalidPositionsException, InvalidSlotException, NoSpaceInColumnException, SoldOutTilesException, GameAlreadyStarted, EndGameException {
        if (!game.getGameState().equals(GameState.IN_GAME)) throw new GameAlreadyStarted("Game already started");

        game.addInShelf(tilesToAdd, currentPlayer, column);
        game.checkCommonTarget(currentPlayer);

        game.isShelfFull(currentPlayer);
        nextTurn();
        game.refillBoard();
    }

    public Tile[] remove(ArrayList<Coordinates> positions) throws EmptySlotException, InvalidPositionsException, InvalidSlotException {
        return game.remove(positions);
    }

    /**
     * Method that at the end of the game,updates the players score and chooses the winner
     *
     * @return the winner
     */
    public Player endGame() {

        for (Player player : players) {
            player.groupScore();
            player.checkPersonalTarget();
        }
        return chooseWinner();
    }

    /**
     * Method to scroll through the players list, making the turns progress
     *
     * @throws EndGameException when the game is ended
     */
    public void nextTurn() throws EndGameException {
        turnChanger = (turnChanger + 1) % players.size();
        currentPlayer = players.get(turnChanger);
        if (isLastTurn() && currentPlayer.isFirstPlayer()) {
            game.setGameState(GameState.END_GAME);
            throw new EndGameException();
        }

    }

    /**
     * Method to if search a player is present by nickname
     *
     * @param nickname the nickname of the searched player
     * @return the nickname or null if not found
     */
    public Player getPlayerByNickname(String nickname) {
        for (Player player : players) {
            if (player.getNickname().equals(nickname)) return player;
        }
        return null;
    }

    public boolean isPlayerPresent(String nickname){
        return getPlayerByNickname(nickname) != null;
    }

    public Player chooseWinner() {
        Player winner = null;
        int max = 0;

        for (Player player : players) {
            if (player.getScore() > max){
                max = player.getScore();
                winner = player;
            }
        }

        return winner;
    }

    /**
     * Method to check if it's the last turn
     */
    public boolean isLastTurn() {
        return game.isLastTurn();
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }
    public TileSlot[][] getBoard(){
        return game.getBoard();
    }

    public CardPersonalTarget getCardPersonalTarget(String nickname){
        for (Player player : players) {
            if (player.getNickname().equals(nickname)) {
                return player.getCardPersonalTarget();
            }
        }
        return null;
    }

    public ArrayList<CardCommonTarget> getCommonTargets(){
        return game.getCommonTargets();
    }

    /**
     * Method to set the max players of the game
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public boolean isEndGameTokenTaken(){
        return game.isEndGameTokenTaken();
    }

}
