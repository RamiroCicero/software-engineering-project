package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.Network.Messages.*;

/**
 * The ClientListener interface defines methods for handling responses received from the server.
 * An implementation of this interface allows the client to receive and handle different types of messages
 * sent by the server.
 */
public interface ClientListener {
    /**
     * Called when a response to the client's login attempt is received.
     *
     * @param loginResponse The response to the client's login attempt.
     */
    void loginResponse(LoginResponse loginResponse);


    /**
     * Called when a response to the game initialization is received.
     *
     * @param initResponse The response to the game initialization.
     */
    void initResponse(InitResponse initResponse);


    /**
     * Called when a response containing an update of the game board is received.
     *
     * @param boardMessage The response containing the game board update.
     */
    void updateBoard(BoardResponse boardMessage);


    /**
     * Called when a response to the removal of elements from the board is received.
     *
     * @param removeResponse The response to the removal of elements from the board.
     */
    void removeResponse(RemoveResponse removeResponse);


    /**
     * Called when a response indicating the current turn is received.
     *
     * @param turnResponse The response indicating the current turn.
     */
    void turnResponse(TurnResponse turnResponse);


    /**
     * Called when a response indicating the end of the game is received.
     *
     * @param endGameMessage The response indicating the end of the game.
     */
    void endGame(EndMessage endGameMessage);


    /**
     * Called when a response indicating the awakening of an element in the game is received.
     *
     * @param wakeMessage The response indicating the awakening of an element in the game.
     */
    void wakeUp(WakeMessage wakeMessage);


    /**
     * Called when a response to the request for setting a value is received.
     *
     * @param setResponse The response to the request for setting a value.
     */
    void setResponse(SetResponse setResponse);


    /**
     * Called when a response to the first game turn is received.
     *
     * @param firstResponse The response to the first game turn.
     */
    void firstResponse(FirstResponse firstResponse);


    /**
     * Called when a preliminary response to the client's login attempt is received.
     *
     * @param preLoginResponse The preliminary response to the client's login attempt.
     */
    void preLoginResponse(PreLoginResponse preLoginResponse);


    /**
     * Called when an error related to the client's username is received.
     *
     * @param usernameError The error related to the client's username.
     */
    void usernameError(UsernameError usernameError);


    /**
     * Called when a response to the card information request is received.
     *
     * @param cardsResponse The response to the card information request.
     */
    void cardsResponse(CardsResponse cardsResponse);


    /**
     * Called when a response to the re-first game turn is received.
     *
     * @param reFirstResponse The response to the re-first game turn.
     */
    void reFirstResponse(ReFirstResponse reFirstResponse);

    /**
     * Called when arrives a DisconnectionMessage
     */
    void disconnectionMessage(DisconnectionMessage disconnectionMessage);

    void chatMessage(ChatMessage chatMessage);
}
