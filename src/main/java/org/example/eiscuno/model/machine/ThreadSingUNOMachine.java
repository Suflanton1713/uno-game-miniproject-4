package org.example.eiscuno.model.machine;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable{
    private ArrayList<Card> cardsPlayer;
    private Deck deck;
    private GameUnoController gameUnoController;
    private GameUno gameUno;

    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, Deck deck, GameUnoController gameUnoController, GameUno gameUno) {
        this.cardsPlayer = cardsPlayer;
        this.deck = deck;
        this.gameUnoController = gameUnoController;
        this.gameUno=gameUno;
    }

    @Override
    public void run(){
        double i=0;
        while (true){
            try {
                i=Math.random() * 5000;
                Thread.sleep((long) (i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hasOneCardTheHumanPlayer();
            System.out.println("El tiempo fue de "+i );
        }
    }

    private void hasOneCardTheHumanPlayer(){
        if(cardsPlayer.size() == 1 && !gameUno.getSingUno()){
            System.out.println("UNO");
            for (int i=0; i<2;i++){
                cardsPlayer.add(deck.takeCard());
            }
            gameUnoController.printCardsHumanPlayer();

        }
    }
}