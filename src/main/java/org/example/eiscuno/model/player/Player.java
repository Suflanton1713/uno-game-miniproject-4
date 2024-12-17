package org.example.eiscuno.model.player;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.observers.listeners.ShiftEventListener;

import java.util.ArrayList;

/**
 * Represents a player in the Uno game.
 */
public class Player implements IPlayer, ShiftEventListener {
    private ArrayList<Card> cardsPlayer; // List of cards the player holds
    private String typePlayer; // Type of player (e.g., human, AI)
    private boolean isOnTurn; // Indicates if the player is currently on turn

    /**
     * Constructs a new Player object with an empty hand of cards.
     *
     * @param typePlayer The type of the player (e.g., human, AI).
     */
    public Player(String typePlayer) {
        this.cardsPlayer = new ArrayList<Card>();
        this.typePlayer = typePlayer;
        isOnTurn = false;
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card The card to be added to the player's hand.
     */
    @Override
    public void addCard(Card card) {
        cardsPlayer.add(card);
    }

    /**
     * Retrieves all cards currently held by the player.
     *
     * @return An ArrayList containing all cards in the player's hand.
     */
    @Override
    public ArrayList<Card> getCardsPlayer() {
        return cardsPlayer;
    }

    /**
     * Removes a card from the player's hand based on its index.
     *
     * @param index The index of the card to remove.
     */
    @Override
    public void removeCard(int index) {
        cardsPlayer.remove(index);
    }

    /**
     * Retrieves a card from the player's hand based on its index.
     *
     * @param index The index of the card to retrieve.
     * @return The card at the specified index in the player's hand.
     */
    @Override
    public Card getCard(int index) {
        return cardsPlayer.get(index);
    }

    /**
     * Retrieves the type of the player.
     *
     * @return The type of the player (e.g., human, AI).
     */
    public String getTypePlayer() {
        return typePlayer;
    }

    /**
     * Checks if the player is currently on turn.
     *
     * @return true if the player is on turn; false otherwise.
     */
    public boolean isOnTurn() {
        return isOnTurn;
    }

    /**
     * Sets the player's turn status.
     *
     * @param onTurn The status to set, true if the player is on turn, false otherwise.
     */
    public void setOnTurn(boolean onTurn) {
        isOnTurn = onTurn;
    }

    /**
     * Draws a specified number of cards from the deck and adds them to the player's hand.
     *
     * @param deck The deck from which the cards are drawn.
     * @param Amount The number of cards to draw.
     */
    @Override
    public void drawsCard(Deck deck, int Amount) {
        for (int i = 0; i < Amount; i++) {
            addCard(deck.takeCard());
        }
    }

    /**
     * Called when the player is on turn. Updates the player's turn status.
     *
     * @param eventType The type of event.
     */
    @Override
    public void onTurnUpdate(String eventType) {
        System.out.println("Player is on turn");
        isOnTurn = true;
    }

    /**
     * Called when the player is not on turn. Updates the player's turn status.
     *
     * @param eventType The type of event.
     */
    @Override
    public void offTurnUpdate(String eventType) {
        System.out.println("Player is not on turn");
        isOnTurn = false;
    }

    /**
     * Called when the player performs an action, such as playing a card. Updates the player's status.
     *
     * @param eventType The type of event.
     */
    @Override
    public void update(String eventType) {
        System.out.println("Player has played, continues machine");
    }

    /**
     * Returns a string representation of the player's cards, including card values and colors.
     *
     * @return A string representing the player's hand of cards.
     */
    public String getStringOfOwnCards() {
        String result = "[";
        for (Card card : cardsPlayer) {
            result = result + card.getValue() + card.getColor() + " ,";
        }
        result = result + "]";
        return result;
    }
}


