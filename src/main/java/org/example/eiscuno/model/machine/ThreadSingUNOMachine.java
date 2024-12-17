/**
 * Clase que representa un hilo para la máquina en el juego Uno, que verifica cuando un jugador debe gritar "UNO".
 * Implementa la interfaz Runnable para ejecutarse en un hilo separado.
 */
package org.example.eiscuno.model.machine;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable {
    private ArrayList<Card> cardsPlayer;
    private GameUno gameUno;
    private ArrayList<Card> cardsMachine;

    /**
     * Constructor de la clase.
     *
     * @param cardsPlayer   Lista de cartas del jugador humano.
     * @param deck          Mazo del juego.
     * @param gameUnoController Controlador del juego Uno.
     * @param gameUno       Instancia del juego Uno.
     * @param cardsMachine  Lista de cartas de la máquina.
     * @param humanPlayer   Instancia del jugador humano.
     * @param machinePlayer Instancia de la máquina como jugador.
     */
    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, Deck deck, GameUnoController gameUnoController, GameUno gameUno, ArrayList<Card> cardsMachine, Player humanPlayer, Player machinePlayer) {
        this.cardsPlayer = cardsPlayer;
        this.gameUno = gameUno;
        this.cardsMachine = cardsMachine;
    }

    /**
     * Lógica principal del hilo. Verifica periódicamente si alguno de los jugadores tiene una sola carta
     * y si no se ha gritado "UNO", establece el estado correspondiente para la máquina.
     */
    @Override
    public void run() {
        double i = 0;
        while (true) {
            try {
                i = Math.random() * 5000; // Tiempo de espera aleatorio entre 0 y 5000 ms.
                Thread.sleep((long) i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Verifica si alguno de los jugadores tiene una carta y no se ha gritado "UNO".
            if ((cardsPlayer.size() == 1 || cardsMachine.size() == 1) && !gameUno.getHumanSingUno()) {
                gameUno.setMachineSingUno(true);
            }
        }
    }
}
