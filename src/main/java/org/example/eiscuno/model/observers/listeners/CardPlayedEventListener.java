package org.example.eiscuno.model.observers.listeners;

import org.example.eiscuno.model.card.Card;

public interface CardPlayedEventListener {
    void canPlayCardUpdate(String eventType);
    void cantPlayCardUpdate(String eventType, Card card);
    void drawCardUpdate(String eventType, int amount);
    void skipTurnUpdate(String eventType);
    void reverseTurnUpdate(String eventType);
    void wildCardUpdate(String eventType, String color);
}
