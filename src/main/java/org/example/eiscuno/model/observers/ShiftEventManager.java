package org.example.eiscuno.model.observers;

import org.example.eiscuno.model.observers.listeners.ShiftEventListener;

import java.util.*;

/**
 * Manages shift events for players in the Uno game.
 * This class handles subscribing, unsubscribing, and notifying listeners about events like "on turn" and "off turn."
 */
public class ShiftEventManager {

    // Map that holds event listeners for different event types
    Map<String, List<ShiftEventListener>> listeners = new HashMap<>();

    /**
     * Constructs a new ShiftEventManager and initializes listeners for each operation.
     *
     * @param operations Event types (like "onturn", "offturn") to initialize listeners for.
     */
    public ShiftEventManager(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    /**
     * Adds a listener to a specified event type.
     *
     * @param eventType The event type (e.g., "onturn", "offturn").
     * @param listener The listener to be added to the event type.
     */
    public void belongToShiftEvent(String eventType, ShiftEventListener listener) {
        List<ShiftEventListener> users = listeners.get(eventType);
        users.add(listener);
    }

    /**
     * Removes a listener from a specified event type.
     *
     * @param eventType The event type (e.g., "onturn", "offturn").
     * @param listener The listener to be removed from the event type.
     */
    public void leaveShiftEvent(String eventType, ShiftEventListener listener) {
        List<ShiftEventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    /**
     * Notifies all listeners of a specified event type.
     *
     * @param eventType The event type to notify listeners about (e.g., "onturn").
     */
    public void notifyShiftEvent(String eventType) {
        ShiftEventListener actualListenerOnTurn;
        List<ShiftEventListener> users = listeners.get(eventType);
        for (ShiftEventListener listener : users) {
            listener.update("eventType");
            actualListenerOnTurn = listener;
            // If the event type is "onturn", change the turn to the next listener
            if (Objects.equals(eventType, "onturn")) {
                changeShiftListener(actualListenerOnTurn);
            }
        }
    }

    /**
     * Notifies all listeners of a "turnShifted" event.
     *
     * @param eventType The event type (turn shift event).
     */
    public void notifyShiftToController(String eventType) {
        List<ShiftEventListener> users = listeners.get(eventType);
        for (ShiftEventListener listener : users) {
            listener.update("turnShifted");
        }
    }

    /**
     * Changes the turn between two listeners (players), moving the listener from "onturn" to "offturn".
     * The listener who is on turn will have their status updated to off-turn, while the listener off turn will get the turn.
     *
     * @param listener The listener whose turn is being changed.
     */
    public void changeShiftListener(ShiftEventListener listener) {
        // Get the listener who is off-turn
        ShiftEventListener listenerOffTurn = listeners.get("offturn").get(0);

        // Notify both listeners of the turn change
        listener.offTurnUpdate("loseTurn");
        listenerOffTurn.onTurnUpdate("getsTurn");

        // Move the listener from onturn to offturn and vice versa
        leaveShiftEvent("onturn", listener);
        belongToShiftEvent("offturn", listener);

        leaveShiftEvent("offturn", listenerOffTurn);
        belongToShiftEvent("onturn", listenerOffTurn);

        // Print the current state of listeners
        System.out.println(listeners);
    }

    /**
     * Retrieves the map of listeners for each event type.
     *
     * @return A map containing event types as keys and lists of listeners as values.
     */
    public Map<String, List<ShiftEventListener>> getListeners() {
        return listeners;
    }
}
