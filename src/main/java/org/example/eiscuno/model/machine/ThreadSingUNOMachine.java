package org.example.eiscuno.model.machine;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;

/**
 * A thread that simulates the behavior of the machine player in the Uno game.
 * This thread periodically checks the state of the game and takes actions based on the number of cards the human and machine players have.
 */
public class ThreadSingUNOMachine implements Runnable{
    private ArrayList<Card> cardsPlayer;  // The list of cards held by the human player.
    private GameUno gameUno;  // The game instance.
    private ArrayList<Card> cardsMachine;  // The list of cards held by the machine player.

    /**
     * Constructs a new ThreadSingUNOMachine to simulate the behavior of the machine player.
     *
     * @param cardsPlayer The list of cards held by the human player.
     * @param deck The deck of cards used in the game.
     * @param gameUnoController The controller for the Uno game.
     * @param gameUno The game instance.
     * @param cardsMachine The list of cards held by the machine player.
     * @param humanPlayer The human player.
     * @param machinePlayer The machine player.
     */
    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, Deck deck, GameUnoController gameUnoController, GameUno gameUno, ArrayList<Card> cardsMachine, Player humanPlayer, Player machinePlayer) {
        this.cardsPlayer = cardsPlayer;
        this.gameUno = gameUno;
        this.cardsMachine = cardsMachine;
    }

    /**
     * This method runs in a separate thread to simulate the machine player's behavior during the game.
     * It randomly pauses for a period of time and checks if the game conditions are met for the machine to declare "UNO".
     */
    @Override
    public void run(){
        double i = 0;  // A random time multiplier for the machine's behavior.
        while (true){
            try {
                // Generate a random number between 0 and 5000 to simulate varying time intervals.
                i = Math.random() * 5000;
                // The machine waits for the generated random time before continuing.
                Thread.sleep((long) (i));

                // Check if either the human player or machine player has only one card left and the human player hasn't called "UNO" yet.
                if((cardsPlayer.size() == 1 || cardsMachine.size() == 1) && !(gameUno.getHumanSingUno() == true) ) {
                    // If the condition is met, wait for an additional second before proceeding.
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // If the condition is met (one player has only one card left), the machine declares "UNO".
            if((cardsPlayer.size() == 1 || cardsMachine.size() == 1) && !(gameUno.getHumanSingUno() == true) ){
                System.out.println("UNO MARDITO MAMAGUEVOOOOOO");  // Prints a message when the machine declares "UNO".
                gameUno.setMachineSingUno(true);  // Sets the machine's "UNO" flag to true.
            }
        }
    }
}