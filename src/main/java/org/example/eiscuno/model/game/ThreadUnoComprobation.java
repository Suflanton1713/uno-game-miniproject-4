package org.example.eiscuno.model.game;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;

/**
 * Clase que maneja la comprobación del estado del juego en un hilo separado.
 * Verifica las condiciones del juego y actualiza la interfaz cuando un jugador o la máquina
 * tiene una sola carta y puede cantar "UNO".
 */
public class ThreadUnoComprobation extends GameUnoAdaptor implements Runnable {
    private ArrayList<Card> cardsPlayer;
    private Deck deck;
    private GameUnoController gameUnoController;
    private GameUno gameUno;
    private ArrayList<Card> cardsMachine;
    private boolean canPlayerSingUno;
    private boolean canMachineSingUno;

    /**
     * Constructor de la clase ThreadUnoComprobation.
     *
     * @param cardsPlayer Lista de cartas del jugador humano.
     * @param deck El mazo de cartas del juego.
     * @param gameUnoController Controlador del juego Uno.
     * @param gameUno Instancia del juego Uno.
     * @param cardsMachine Lista de cartas de la máquina.
     * @param humanPlayer Jugador humano.
     * @param machinePlayer Jugador máquina.
     */
    public ThreadUnoComprobation(ArrayList<Card> cardsPlayer, Deck deck, GameUnoController gameUnoController, GameUno gameUno, ArrayList<Card> cardsMachine, Player humanPlayer, Player machinePlayer) {
        this.cardsPlayer = cardsPlayer;
        this.deck = deck;
        this.gameUnoController = gameUnoController;
        this.gameUno = gameUno;
        this.cardsMachine = cardsMachine;
        canPlayerSingUno = true;
        canMachineSingUno = true;
    }

    /**
     * Método que ejecuta la comprobación periódica del estado del juego en un hilo separado.
     * Verifica si un jugador o la máquina tienen solo una carta y si deben cantar "UNO".
     * Deshabilita o habilita los controles del juego según sea necesario.
     */
    @Override
    public void run() {
        gameUnoController.getOneButton().setDisable(true); // Deshabilita el botón de "UNO"

        while (true) {
            try {
                Thread.sleep(100); // Pausa el hilo por 100 ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Si el jugador tiene 2 o más cartas, habilita la opción de cantar UNO
            if (cardsPlayer.size() >= 2) {
                canMachineSingUno = true;
                gameUnoController.getOneButton().setDisable(true);
            }

            // Si la máquina tiene 2 o más cartas, habilita la opción de cantar UNO
            if (cardsMachine.size() >= 2) {
                canPlayerSingUno = true;
                gameUnoController.getOneButton().setDisable(true);
            }

            // Verifica si alguno de los jugadores o la máquina tiene una sola carta
            if (cardsPlayer.size() == 1 || cardsMachine.size() == 1) {
                if ((cardsPlayer.size() == 1 && canMachineSingUno) || (cardsMachine.size() == 1 && canPlayerSingUno)) {
                    System.out.println("(cardsPlayer.size() == 1 && canMachineSingUno)");
                    System.out.println((cardsPlayer.size() == 1 && canMachineSingUno));
                    System.out.println("(cardsMachine.size() == 1 && canPlayerSingUno)");
                    System.out.println((cardsMachine.size() == 1 && canPlayerSingUno));
                    gameUnoController.getOneButton().setDisable(false); // Habilita el botón de "UNO"
                }

                // Llama a los métodos para verificar si el jugador o la máquina tienen solo una carta
                hasOneCardTheHumanPlayer();
                machinePlayerOnlyHasOneCard();
            }
        }
    }

    /**
     * Verifica si el jugador humano tiene una sola carta y si puede cantar "UNO".
     * Si es así, agrega dos cartas al jugador y deshabilita el botón de "UNO".
     */
    private void hasOneCardTheHumanPlayer() {
        if (cardsPlayer.size() == 1 && !gameUno.getHumanSingUno() && gameUno.isMachineSingUno() && canMachineSingUno) {
            System.out.println("Entro al hasOneCardTheHumanPlayer");
            System.out.println("UNO");
            // El jugador recibe dos cartas adicionales
            for (int i = 0; i < 2; i++) {
                cardsPlayer.add(deck.takeCard());
            }
            gameUnoController.printCardsHumanPlayer(); // Actualiza la interfaz con las cartas del jugador

            canMachineSingUno = false; // Deshabilita la opción para que la máquina cante "UNO"
            gameUnoController.getOneButton().setDisable(true); // Deshabilita el botón de "UNO"
        } else if (cardsPlayer.size() == 1 && gameUno.getHumanSingUno()) {
            System.out.println("yo cante mardita cerda dejame quieto");
            canMachineSingUno = false;
            gameUnoController.getOneButton().setDisable(true);
        }
    }

    /**
     * Verifica si la máquina tiene una sola carta y si puede cantar "UNO".
     * Si es así, agrega dos cartas a la máquina y deshabilita el botón de "UNO".
     */
    private void machinePlayerOnlyHasOneCard() {
        if (cardsMachine.size() == 1 && !gameUno.isMachineSingUno() && gameUno.getHumanSingUno() && canPlayerSingUno) {
            System.out.println("UNO");
            // La máquina recibe dos cartas adicionales
            for (int i = 0; i < 2; i++) {
                cardsMachine.add(deck.takeCard());
            }
            gameUnoController.printCardsHumanPlayer(); // Actualiza la interfaz con las cartas de la máquina

            canPlayerSingUno = false; // Deshabilita la opción para que el jugador cante "UNO"
            gameUnoController.getOneButton().setDisable(true); // Deshabilita el botón de "UNO"
        } else if (cardsMachine.size() == 1 && gameUno.isMachineSingUno()) {
            canPlayerSingUno = false;
            gameUnoController.getOneButton().setDisable(true);
        }
    }
}
