package org.example.eiscuno.model.observers.listeners;

public interface ShiftEventListener {
    void onTurnUpdate(String eventType);
    void offTurnUpdate(String eventType);
    void update(String eventType);
}
