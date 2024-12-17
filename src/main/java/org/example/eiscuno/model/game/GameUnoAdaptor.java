package org.example.eiscuno.model.game;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.player.Player;

public abstract class GameUnoAdaptor implements IGameUno{

    /**
     * Starts the Uno game.
     */
    @Override
    public void startGame() {

    }

    /**
     * Makes a player draw a specified number of cards from the deck.
     *
     * @param player        the player who will draw the cards
     * @param numberOfCards the number of cards to be drawn
     */
    @Override
    public void eatCard(Player player, int numberOfCards) {

    }

    /**
     * Plays a card in the game, adding it to the table.
     *
     * @param card the card to be played
     */
    @Override
    public void playCard(Card card) {

    }

    /**
     * Handles the action when a player shouts "Uno".
     *
     * @param playerWhoSang the identifier of the player who shouted "Uno"
     */
    @Override
    public void haveSungOne(String playerWhoSang) {

    }

    /**
     * Retrieves the current visible cards of the human player starting from a specific position.
     *
     * @param posInitCardToShow the starting position of the cards to be shown
     * @return an array of cards that are currently visible to the human player
     */
    @Override
    public Card[] getCurrentVisibleCardsHumanPlayer(int posInitCardToShow) {
        return new Card[0];
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise
     */
    @Override
    public Boolean isGameOver() {
        return null;
    }
}
