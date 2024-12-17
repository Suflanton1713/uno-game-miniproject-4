package org.example.eiscuno.model.observers.listeners;

/**
 * The ShiftEventListener interface defines the contract for listeners that handle events
 * related to the shift or turn changes in a game. Implementing classes should provide specific
 * behavior when the turn is updated or switched.
 */
public interface ShiftEventListener {

    /**
     * Called when the player is on turn.
     *
     * @param eventType The type of the event (e.g., "onTurn").
     */
    void onTurnUpdate(String eventType);

    /**
     * Called when the player is off turn.
     *
     * @param eventType The type of the event (e.g., "offTurn").
     */
    void offTurnUpdate(String eventType);

    /**
     * Called when there is a general update regarding the shift event.
     *
     * @param eventType The type of the event (e.g., "shiftChanged").
     */
    void update(String eventType);
}
