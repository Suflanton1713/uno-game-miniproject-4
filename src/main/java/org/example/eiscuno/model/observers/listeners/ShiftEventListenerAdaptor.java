package org.example.eiscuno.model.observers.listeners;

public abstract class ShiftEventListenerAdaptor {
    public void onTurnUpdate(String eventType){};
    public void offTurnUpdate(String eventType){};
    public void update(String eventType){};
}
