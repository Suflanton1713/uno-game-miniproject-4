package org.example.eiscuno.model.observers;

import org.example.eiscuno.model.observers.listeners.ShiftEventListener;

import java.util.*;

/**
 * The ShiftEventManager class manages the shift events and listeners for different event types.
 * It allows listeners to join or leave specific events, and notifies them of changes in the event state.
 */
public class ShiftEventManager {

    // A map that holds event types and their corresponding listeners.
    private Map<String, List<ShiftEventListener>> listeners = new HashMap<>();

    /**
     * Constructor to initialize the ShiftEventManager with a set of event types.
     *
     * @param operations The event types that the manager should handle.
     */
    public ShiftEventManager(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    /**
     * Adds a listener to a specific event type.
     *
     * @param eventType The event type to which the listener should be added.
     * @param listener The listener to be added to the event type.
     */
    public void belongToShiftEvent(String eventType, ShiftEventListener listener) {
        List<ShiftEventListener> users = listeners.get(eventType);
        users.add(listener);
    }

    /**
     * Removes a listener from a specific event type.
     *
     * @param eventType The event type from which the listener should be removed.
     * @param listener The listener to be removed from the event type.
     */
    public void leaveShiftEvent(String eventType, ShiftEventListener listener) {
        List<ShiftEventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    /**
     * Notifies all listeners of a specific event type about the occurrence of the event.
     *
     * @param eventType The event type that is being notified.
     */
    public void notifyShiftEvent(String eventType) {
        ShiftEventListener actualListenerOnTurn;
        List<ShiftEventListener> users = listeners.get(eventType);
        for (ShiftEventListener listener : users) {
            listener.update("eventType");  // Notify listeners with the event type
            actualListenerOnTurn = listener;
            if (Objects.equals(eventType, "onturn")) {
                // If the event is "onturn", change the shift of listeners
                changeShiftListener(actualListenerOnTurn);
            }
        }
    }

    /**
     * Notifies all listeners of a specific event type that a shift has occurred.
     *
     * @param eventType The event type indicating the shift.
     */
    public void notifyShiftToController(String eventType) {
        List<ShiftEventListener> users = listeners.get(eventType);
        for (ShiftEventListener listener : users) {
            listener.update("turnShifted");  // Notify listeners about the shift
        }
    }

    /**
     * Changes the shift of listeners, switching the "onturn" listener with the "offturn" listener.
     *
     * @param listener The listener who is currently on turn.
     */
    public void changeShiftListener(ShiftEventListener listener) {
        // Get the listener off turn
        ShiftEventListener listenerOffTurn = listeners.get("offturn").get(0);

        // Notify the current "onturn" listener that they have lost the turn
        listener.offTurnUpdate("loseTurn");

        // Notify the "offturn" listener that they are now on turn
        listenerOffTurn.onTurnUpdate("getsTurn");

        // Swap the listeners between "onturn" and "offturn"
        leaveShiftEvent("onturn", listener);
        belongToShiftEvent("offturn", listener);

        leaveShiftEvent("offturn", listenerOffTurn);
        belongToShiftEvent("onturn", listenerOffTurn);

        // Print the current state of listeners
        System.out.println(listeners);
    }

    /**
     * Gets the map of event types and their corresponding listeners.
     *
     * @return The map of event types to their listeners.
     */
    public Map<String, List<ShiftEventListener>> getListeners() {
        return listeners;
    }
}
