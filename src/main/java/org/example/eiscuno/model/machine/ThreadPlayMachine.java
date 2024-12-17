package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.exception.GameException;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.observers.listeners.ShiftEventListener;
import org.example.eiscuno.model.table.Table;

import java.util.ArrayList;
import java.util.List;

public class ThreadPlayMachine extends Thread implements ShiftEventListener {
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;
    private GameUno gameUno;
    private GameUnoController gameUnoController;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, GameUno gameUno, GameUnoController gameUnoController) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.gameUno = gameUno;
        this.gameUnoController = gameUnoController;
    }

    @Override
    public void onTurnUpdate(String eventType) {
        System.out.println("Machine is on turn");
        machinePlayer.setOnTurn(true);
    }

    @Override
    public void offTurnUpdate(String eventType) {
        System.out.println("Machine is not on turn");
        machinePlayer.setOnTurn(false);
    }

    public void update(String eventType){
        System.out.println("Machine has played, continues player");
    };

    public void run() {
        while (true){

            if(hasPlayerPlayed && !(gameUno.isGameOver())){
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Aqui iria la logica de colocar la carta

                if(gameUno.isHumanSingUno() && (machinePlayer.getCardsPlayer().size() == 1)){
                    gameUno.passTurnWhenUnoSung();
                    hasPlayerPlayed = false;
                }else{
                    try {
                        hasPlayerPlayed = putCardOnTheTable();
                    } catch (GameException e) {
                        throw new RuntimeException(e);
                    }
                    if(gameUno.HasToChangeColor()){
                        table.setColorForTable(chooseColorForMachine());
                        gameUno.setHasToChangeColor(false);

                    }
                }



            }
        }

    }

    private String chooseColorForMachine(){
        int blueCounter = 0, yellowCounter = 0, greenCounter = 0, redCounter = 0;
        String colorChoosen;
        int maxNumber;
        for(Card card : machinePlayer.getCardsPlayer() ){
            switch (card.getColor()){
                case "BLUE":
                    blueCounter++;
                    break;
                case "YELLOW":
                    yellowCounter++;
                    break;
                case "GREEN":
                    greenCounter++;
                    break;
                case "RED":
                    redCounter++;
                    break;
                default:
                    break;
            }

        }

        maxNumber = blueCounter;
        colorChoosen = "BLUE";

        if(yellowCounter > maxNumber) colorChoosen = "YELLOW";
        if(greenCounter > maxNumber) colorChoosen = "GREEN";
        if(redCounter > maxNumber) colorChoosen = "RED";
        System.out.println(colorChoosen);

        return colorChoosen;

    }

    private boolean putCardOnTheTable() throws GameException {
        if(!machinePlayer.getCardsPlayer().isEmpty()){
            System.out.println("Machine is choosing cards");
            int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
            Card card = machinePlayer.getCard(index);
            card = chooseAllowedCard(card);
            if (!(card == null)) {
                gameUno.playCard(card);
                tableImageView.setImage(card.getImage());
                machinePlayer.removeCard(machinePlayer.getCardsPlayer().indexOf(card));
                gameUnoController.animateMachineCardToCenter(card);

            }
            System.out.println("Machine cards after playing " + machinePlayer.getStringOfOwnCards());
            System.out.println("La maquina gano ");
            System.out.println(machinePlayer.getCardsPlayer().isEmpty());
            if (machinePlayer.getCardsPlayer().isEmpty()){
                System.out.println("Gano la maquina");
                gameUno.setWinStatus(2);
                System.out.println("El winstatus es " + gameUno.getWinStatus());
                gameUnoController.updateWinStatus();

            }

            return machinePlayer.isOnTurn();
        }
        return true;
    }

    private Card chooseAllowedCard(Card card) throws GameException {
        boolean isChoosenCardAllowed = false;
        boolean haveDrawedACard = false;
        int indexOfCardChoosen = machinePlayer.getCardsPlayer().indexOf(card);
        int searchingInDeckCounter = 0;
        int initialOwnCards = machinePlayer.getCardsPlayer().size();
        Card returnCard = card;



        do{
            table.verifyCardTypeOnTable(returnCard);

            if(searchingInDeckCounter == initialOwnCards){
                System.out.println("Machine draws a card");
                System.out.println(machinePlayer.getStringOfOwnCards());
                machinePlayer.drawsCard(gameUno.getDeck(), 1);
                returnCard = machinePlayer.getCard(machinePlayer.getCardsPlayer().size()-1);
                table.verifyCardTypeOnTable(returnCard);
                haveDrawedACard = true;
            }

                if(gameUno.canThrowCard()){
                    System.out.println("Actual card can be throwed");
                    isChoosenCardAllowed = true;
                }else {

                    if(!(searchingInDeckCounter == initialOwnCards)){
                        System.out.println("Machine searches a card xd");
                        System.out.println("hi");
                        System.out.println(machinePlayer.getStringOfOwnCards());
                        System.out.println("machine index of card choosen" + machinePlayer.getCardsPlayer().indexOf(returnCard));
                        System.out.println("initial own cards - 1 " + (initialOwnCards - 1));
                        if((machinePlayer.getCardsPlayer().indexOf(returnCard) == (initialOwnCards - 1))){
                            indexOfCardChoosen = 0;
                            searchingInDeckCounter++;
                        } else {
                            indexOfCardChoosen++;
                            searchingInDeckCounter++;
                        }
                        System.out.println("indexOfCardChoosen" + (indexOfCardChoosen));
                        returnCard = machinePlayer.getCardsPlayer().get((indexOfCardChoosen));
                        System.out.println("Machine is looking at " + returnCard.getValue() + returnCard.getColor());

                    }

                }

            System.out.println("Is choosen card allowed");
            System.out.println(!isChoosenCardAllowed);

            if(haveDrawedACard && !isChoosenCardAllowed){
                gameUno.events.notifyShiftEvent("onturn");
                gameUno.events.notifyShiftToController("turnChangerController");
                returnCard = null;
                isChoosenCardAllowed = true;
            }

            }while(!isChoosenCardAllowed);



        return returnCard;
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    public Player getMachinePlayer() {
        return machinePlayer;
    }
}