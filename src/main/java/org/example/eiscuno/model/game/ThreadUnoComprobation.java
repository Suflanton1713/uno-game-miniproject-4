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
    private boolean canPlayerSingUno;
    private boolean canMachineSingUno;




    public ThreadUnoComprobation(ArrayList<Card> cardsPlayer, Deck deck, GameUnoController gameUnoController, GameUno gameUno, ArrayList<Card> cardsMachine, Player humanPlayer, Player machinePlayer) {
        this.cardsPlayer = cardsPlayer;
        this.deck = deck;
        this.gameUnoController = gameUnoController;
        this.gameUno=gameUno;
        this.cardsMachine = cardsMachine;
        canPlayerSingUno = true;
        canMachineSingUno = true;
    }

    @Override
    public void run(){
        gameUnoController.getOneButton().setDisable(true);
        while (true){
            try {
                Thread.sleep((long) (100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(cardsPlayer.size()>=2){
                canMachineSingUno = true;
                gameUnoController.getOneButton().setDisable(true);
            }

            if(cardsMachine.size()>=2){
                canPlayerSingUno = true;
                gameUnoController.getOneButton().setDisable(true);
            }






            if(cardsPlayer.size()==1 || cardsMachine.size()==1){

                if((cardsPlayer.size()==1 && canMachineSingUno) || (cardsMachine.size()==1 && canPlayerSingUno) ){
                    System.out.println("(cardsPlayer.size()==1 && canMachineSingUno)");
                    System.out.println((cardsPlayer.size()==1 && canMachineSingUno));
                    System.out.println("(cardsMachine.size()==1 && canPlayerSingUno)");
                    System.out.println((cardsMachine.size()==1 && canPlayerSingUno));
                    gameUnoController.getOneButton().setDisable(false);
                }

                hasOneCardTheHumanPlayer();
                machinePlayerOnlyHasOneCard();
            }
        }
    }

    private void hasOneCardTheHumanPlayer(){
        if(cardsPlayer.size() == 1 && !gameUno.getHumanSingUno() && gameUno.isMachineSingUno() && canMachineSingUno){
            System.out.println("Entro al hasOneCardTheHumanPlayer");
            System.out.println("UNO");
            for (int i=0; i<2;i++){
                cardsPlayer.add(deck.takeCard());
            }
            gameUnoController.printCardsHumanPlayer();

            canMachineSingUno = false;
            gameUnoController.getOneButton().setDisable(true);

        }else if(cardsPlayer.size() == 1 && gameUno.getHumanSingUno()){
            System.out.println("yo cante mardita cerda dejame quieto");

            canMachineSingUno = false;
            gameUnoController.getOneButton().setDisable(true);
        }
    }
    private void machinePlayerOnlyHasOneCard(){
        if(cardsMachine.size() == 1 && !gameUno.isMachineSingUno() && gameUno.getHumanSingUno() &&canPlayerSingUno){
            System.out.println("UNO");
            for (int i=0; i<2;i++){
                cardsMachine.add(deck.takeCard());
            }
            gameUnoController.printCardsHumanPlayer();

            canPlayerSingUno = false;
            gameUnoController.getOneButton().setDisable(true);

        }else if(cardsMachine.size() == 1 && gameUno.isMachineSingUno()){
            canPlayerSingUno = false;
            gameUnoController.getOneButton().setDisable(true);
        }
    }
}