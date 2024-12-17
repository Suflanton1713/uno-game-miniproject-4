package org.example.eiscuno.model.game;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;

public class ThreadUnoComprobation implements Runnable{
    private ArrayList<Card> cardsPlayer;
    private Deck deck;
    private GameUnoController gameUnoController;
    private GameUno gameUno;
    private ArrayList<Card> cardsMachine;
    private Player humanPlayer;
    private Player machinePlayer;
    private boolean cardsAddedToMachine;
    private boolean cardsAddedToPlayer;
    private int actualTurnStatus;
    private int turnWhenCardsAdded;


    public ThreadUnoComprobation(ArrayList<Card> cardsPlayer, Deck deck, GameUnoController gameUnoController, GameUno gameUno, ArrayList<Card> cardsMachine, Player humanPlayer, Player machinePlayer) {
        this.cardsPlayer = cardsPlayer;
        this.deck = deck;
        this.gameUnoController = gameUnoController;
        this.gameUno=gameUno;
        this.cardsMachine = cardsMachine;
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        cardsAddedToMachine = false;
        cardsAddedToPlayer = false;
        actualTurnStatus = 1;
        turnWhenCardsAdded = 0;
    }

    @Override
    public void run(){
        double i=0;
        while (true){
            try {
                i=Math.random() * 2000;
                Thread.sleep((long) (i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if(cardsPlayer.size()==1 || cardsMachine.size()==1){
                hasOneCardTheHumanPlayer();
                machinePlayerOnlyHasOneCard();
            }
        }
    }

    private void hasOneCardTheHumanPlayer(){
        if(cardsPlayer.size() == 1 && !gameUno.getHumanSingUno() && gameUno.isMachineSingUno()){
            System.out.println("Entro al hasOneCardTheHumanPlayer");
            System.out.println("UNO");
            for (int i=0; i<2;i++){
                cardsPlayer.add(deck.takeCard());
            }
            gameUnoController.printCardsHumanPlayer();

            cardsAddedToPlayer = true;

            turnWhenCardsAdded = 1;


        }
    }
    private void machinePlayerOnlyHasOneCard(){
        if(cardsMachine.size() == 1 && !gameUno.isMachineSingUno() && gameUno.getHumanSingUno()){
            System.out.println("UNO");
            for (int i=0; i<2;i++){
                cardsMachine.add(deck.takeCard());
            }
            gameUnoController.printCardsHumanPlayer();
            cardsAddedToMachine = true;

            turnWhenCardsAdded = 2;

        }
    }
}