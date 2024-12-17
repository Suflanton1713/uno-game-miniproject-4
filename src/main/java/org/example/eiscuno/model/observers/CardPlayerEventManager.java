package org.example.eiscuno.model.observers;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.observers.listeners.CardPlayedEventListener;

import java.util.*;

/**
 * Clase que maneja los eventos relacionados con el jugador de cartas.
 * Permite registrar, eliminar y notificar a los oyentes sobre diversos eventos
 * que ocurren durante el juego, como la posibilidad de jugar una carta,
 * el no poder jugar una carta, o el realizar acciones como robar cartas o saltar turno.
 */
public class CardPlayerEventManager {

    /**
     * Mapa que almacena los oyentes de eventos para cada tipo de operación.
     * La clave es el tipo de evento y el valor es la lista de oyentes para ese evento.
     */
    Map<String, List<CardPlayedEventListener>> listeners = new HashMap<>();

    /**
     * Constructor que inicializa el administrador de eventos con un conjunto de operaciones.
     * Cada operación tiene su propia lista de oyentes.
     *
     * @param operations Operaciones para las cuales se deben registrar los oyentes.
     */
    public CardPlayerEventManager(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    /**
     * Registra un oyente para un evento específico.
     *
     * @param eventType El tipo de evento al cual el oyente debe suscribirse.
     * @param listener El oyente que responderá al evento.
     */
    public void belongToCardEvent(String eventType, CardPlayedEventListener listener) {
        List<CardPlayedEventListener> users = listeners.get(eventType);
        users.add(listener);
    }

    /**
     * Elimina un oyente de un evento específico.
     *
     * @param eventType El tipo de evento del cual el oyente debe desuscribirse.
     * @param listener El oyente que será eliminado.
     */
    public void leaveCardEvent(String eventType, CardPlayedEventListener listener) {
        List<CardPlayedEventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    /**
     * Notifica a todos los oyentes que el jugador puede jugar una carta.
     *
     * @param eventType El tipo de evento que indica la posibilidad de jugar una carta.
     */
    public void notifyCanPlayCard(String eventType) {
        System.out.println("Estos son los listeners" + getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.canPlayCardUpdate(eventType);
        }
    }

    /**
     * Notifica a todos los oyentes que el jugador no puede jugar una carta.
     *
     * @param eventType El tipo de evento que indica que no se puede jugar una carta.
     * @param card La carta que no puede jugarse.
     */
    public void notifyCantPlayCard(String eventType, Card card) {
        System.out.println("No se puede tirar carta");
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.cantPlayCardUpdate(eventType, card);
        }
    }

    /**
     * Notifica a todos los oyentes que deben robar cartas.
     *
     * @param eventType El tipo de evento que indica que se debe robar cartas.
     * @param amount La cantidad de cartas que se deben robar.
     */
    public void notifyDrawCards(String eventType, int amount) {
        System.out.println("Arroje carta");
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        System.out.println(users);
        for (CardPlayedEventListener listener : users) {
            listener.drawCardUpdate(eventType, amount);
        }
    }

    /**
     * Notifica a todos los oyentes que el turno debe ser saltado.
     *
     * @param eventType El tipo de evento que indica que se debe saltar el turno.
     */
    public void notifySkipTurn(String eventType) {
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.skipTurnUpdate(eventType);
        }
    }

    /**
     * Notifica a todos los oyentes que el turno debe invertirse.
     *
     * @param eventType El tipo de evento que indica que se debe invertir el turno.
     */
    public void notifyReverseTurn(String eventType) {
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.reverseTurnUpdate(eventType);
        }
    }

    /**
     * Notifica a todos los oyentes que se ha jugado una carta comodín.
     *
     * @param eventType El tipo de evento que indica que se ha jugado una carta comodín.
     */
    public void notifyWildCard(String eventType) {
        System.out.println(getListeners());
        List<CardPlayedEventListener> users = listeners.get(eventType);
        for (CardPlayedEventListener listener : users) {
            listener.wildCardUpdate(eventType);
        }
    }

    /**
     * Obtiene el mapa de oyentes registrados por tipo de evento.
     *
     * @return Mapa que asocia los tipos de evento con sus respectivos oyentes.
     */
    public Map<String, List<CardPlayedEventListener>> getListeners() {
        return listeners;
    }
}
