package org.example.eiscuno.model.observers;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.observers.listeners.CardPlayedEventListener;

import java.util.*;


public class CardPlayerEventManager {

    Map<String, List<CardPlayedEventListener>> listeners = new HashMap<>();

    public CardPlayerEventManager(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    public void belongToCardEvent(String eventType, CardPlayedEventListener listener) {
        List<CardPlayedEventListener> users = listeners.get(eventType);
        users.add(listener);
    }

    public void leaveCardEvent(String eventType, CardPlayedEventListener listener) {
        List<CardPlayedEventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    public void notifyCanPlayCard(String eventType) {
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.canPlayCardUpdate(eventType);
        }
    }

    public void notifyCantPlayCard(String eventType, Card card) {
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.cantPlayCardUpdate(eventType, card);
        }
    }

    public void notifyDrawCards(String eventType, int amount) {
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        System.out.println(users);
        for (CardPlayedEventListener listener : users) {
            listener.drawCardUpdate(eventType, amount);
        }
    }

    public void notifySkipTurn(String eventType) {
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.skipTurnUpdate(eventType);
        }
    }

    public void notifyReverseTurn(String eventType) {
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.reverseTurnUpdate(eventType);
        }
    }

    public void notifyWildCard(String eventType) {
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.wildCardUpdate(eventType);
        }
    }

    public Map<String, List<CardPlayedEventListener>> getListeners() {
        return listeners;
    }
}
