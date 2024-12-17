package org.example.eiscuno.model.observers;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.observers.listeners.CardPlayedEventListener;

import java.util.*;

/**
 * Manages events related to the cards played in the Uno game.
 * This class allows listeners to subscribe to various card-related events and notifies them when the events occur.
 */
public class CardPlayerEventManager {

    // Map that holds event listeners for different card play operations
    Map<String, List<CardPlayedEventListener>> listeners = new HashMap<>();

    /**
     * Constructs a new CardPlayerEventManager and initializes listeners for each operation.
     *
     * @param operations Event types (like "threwCard", "skip", "reverse", etc.) to initialize listeners for.
     */
    public CardPlayerEventManager(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    /**
     * Adds a listener to a specified event type related to card play events.
     *
     * @param eventType The event type (e.g., "threwCard", "skip", etc.).
     * @param listener The listener to be added to the event type.
     */
    public void belongToCardEvent(String eventType, CardPlayedEventListener listener) {
        List<CardPlayedEventListener> users = listeners.get(eventType);
        users.add(listener);
    }

    /**
     * Removes a listener from a specified event type related to card play events.
     *
     * @param eventType The event type (e.g., "threwCard", "skip", etc.).
     * @param listener The listener to be removed from the event type.
     */
    public void leaveCardEvent(String eventType, CardPlayedEventListener listener) {
        List<CardPlayedEventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    /**
     * Notifies all listeners that a card can be played for a specific event.
     *
     * @param eventType The event type (e.g., "threwCard").
     */
    public void notifyCanPlayCard(String eventType) {
        System.out.println("These are the listeners: " + getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.canPlayCardUpdate(eventType);
        }
    }

    /**
     * Notifies all listeners that a card cannot be played for a specific event.
     *
     * @param eventType The event type (e.g., "threwCard").
     * @param card The card that was attempted to be played.
     */
    public void notifyCantPlayCard(String eventType, Card card) {
        System.out.println("Card cannot be played.");
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.cantPlayCardUpdate(eventType, card);
        }
    }

    /**
     * Notifies all listeners to draw a specific number of cards.
     *
     * @param eventType The event type (e.g., "drawCards").
     * @param amount The number of cards to be drawn.
     */
    public void notifyDrawCards(String eventType, int amount) {
        System.out.println("Drawing cards...");
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.drawCardUpdate(eventType, amount);
        }
    }

    /**
     * Notifies all listeners to skip the current player's turn.
     *
     * @param eventType The event type (e.g., "skip").
     */
    public void notifySkipTurn(String eventType) {
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.skipTurnUpdate(eventType);
        }
    }

    /**
     * Notifies all listeners to reverse the current turn order.
     *
     * @param eventType The event type (e.g., "reverse").
     */
    public void notifyReverseTurn(String eventType) {
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.reverseTurnUpdate(eventType);
        }
    }

    /**
     * Notifies all listeners that a wild card has been played.
     *
     * @param eventType The event type (e.g., "wild").
     */
    public void notifyWildCard(String eventType) {
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.wildCardUpdate(eventType);
        }
    }

    /**
     * Retrieves the map of listeners for each event type.
     *
     * @return A map containing event types as keys and lists of listeners as values.
     */
    public Map<String, List<CardPlayedEventListener>> getListeners() {
        return listeners;
    }
}
