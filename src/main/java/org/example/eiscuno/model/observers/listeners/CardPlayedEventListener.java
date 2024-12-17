package org.example.eiscuno.model.observers.listeners;

import org.example.eiscuno.model.card.Card;

/**
 * The CardPlayedEventListener interface defines the contract for listeners that handle events
 * related to card playing actions. Implementing classes should provide specific behavior
 * when these events occur during a card game.
 */
public interface CardPlayedEventListener {

    /**
     * Called when a player is allowed to play a card.
     *
     * @param eventType The type of the event (e.g., "canPlayCard").
     */
    void canPlayCardUpdate(String eventType);

    /**
     * Called when a player is not allowed to play a card.
     *
     * @param eventType The type of the event (e.g., "cantPlayCard").
     * @param card The card that the player attempted to play.
     */
    void cantPlayCardUpdate(String eventType, Card card);

    /**
     * Called when a player draws a specified number of cards.
     *
     * @param eventType The type of the event (e.g., "drawCards").
     * @param amount The number of cards drawn.
     */
    void drawCardUpdate(String eventType, int amount);

    /**
     * Called when a player skips their turn.
     *
     * @param eventType The type of the event (e.g., "skipTurn").
     */
    void skipTurnUpdate(String eventType);

    /**
     * Called when the direction of the turns is reversed.
     *
     * @param eventType The type of the event (e.g., "reverseTurn").
     */
    void reverseTurnUpdate(String eventType);

    /**
     * Called when a wild card is played.
     *
     * @param eventType The type of the event (e.g., "wildCard").
     */
    void wildCardUpdate(String eventType);
}
