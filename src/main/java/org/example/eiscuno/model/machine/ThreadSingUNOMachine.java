package org.example.eiscuno.model.machine;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable{
    private ArrayList<Card> cardsPlayer;
    private GameUno gameUno;
    private ArrayList<Card> cardsMachine;



    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, Deck deck, GameUnoController gameUnoController, GameUno gameUno, ArrayList<Card> cardsMachine, Player humanPlayer, Player machinePlayer) {
        this.cardsPlayer = cardsPlayer;
        this.gameUno=gameUno;
        this.cardsMachine = cardsMachine;

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

            if((cardsPlayer.size()==1 || cardsMachine.size()==1) && !(gameUno.getHumanSingUno()==true) ){
                System.out.println("UNO MARDITO MAMAGUEVOOOOOO");
                gameUno.setMachineSingUno(true);
            }

        }
    }

}