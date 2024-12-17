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

/**
 * A thread that simulates the behavior of the machine player in the Uno game.
 * The thread is responsible for making the machine player take its turn by choosing a card to play and performing necessary actions, such as drawing cards and updating the game state.
 */
public class ThreadPlayMachine extends Thread implements ShiftEventListener {
    private Table table;  // The table on which the game is played.
    private Player machinePlayer;  // The machine player.
    private ImageView tableImageView;  // The ImageView representing the table.
    private volatile boolean hasPlayerPlayed;  // A flag to check if the player has played.
    private GameUno gameUno;  // The Uno game instance.
    private GameUnoController gameUnoController;  // The Uno game controller.

    /**
     * Constructor to initialize the thread with required parameters.
     *
     * @param table The table object where the game is played.
     * @param machinePlayer The machine player.
     * @param tableImageView The ImageView to represent the table.
     * @param gameUno The Uno game instance.
     * @param gameUnoController The Uno game controller.
     */
    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, GameUno gameUno, GameUnoController gameUnoController) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.gameUno = gameUno;
        this.gameUnoController = gameUnoController;
    }

    /**
     * Called when the turn changes and the machine player gets its turn.
     *
     * @param eventType The type of event (on turn or off turn).
     */
    @Override
    public void onTurnUpdate(String eventType) {
        System.out.println("Machine is on turn");
        machinePlayer.setOnTurn(true);
    }

    /**
     * Called when the turn changes and the machine player loses its turn.
     *
     * @param eventType The type of event (on turn or off turn).
     */
    @Override
    public void offTurnUpdate(String eventType) {
        System.out.println("Machine is not on turn");
        machinePlayer.setOnTurn(false);
    }

    /**
     * A placeholder for the update method from the ShiftEventListener interface.
     * Called when the machine has played a card and the turn continues.
     *
     * @param eventType The event type.
     */
    public void update(String eventType){
        System.out.println("Machine has played, continues player");
    }

    /**
     * This method is the main game loop for the machine's turn. The machine will choose a card and play it.
     * It also handles special situations like when the machine needs to declare "UNO".
     */
    public void run() {
        while (true){

            if(hasPlayerPlayed && !(gameUno.isGameOver())){  // If the player has played and the game is not over
                try{
                    Thread.sleep(2000);  // Wait before the machine makes a move
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Logic for playing the card
                if(gameUno.isHumanSingUno() && (machinePlayer.getCardsPlayer().size() == 1)){
                    gameUno.passTurnWhenUnoSung();
                    hasPlayerPlayed = false;
                } else {
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

    /**
     * Chooses the most frequent color among the machine player's cards.
     *
     * @return The chosen color.
     */
    private String chooseColorForMachine(){
        int blueCounter = 0, yellowCounter = 0, greenCounter = 0, redCounter = 0;
        String colorChoosen;
        int maxNumber;

        // Count the number of cards of each color
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

        // Determine the most frequent color
        maxNumber = blueCounter;
        colorChoosen = "BLUE";
        if(yellowCounter > maxNumber) colorChoosen = "YELLOW";
        if(greenCounter > maxNumber) colorChoosen = "GREEN";
        if(redCounter > maxNumber) colorChoosen = "RED";

        System.out.println(colorChoosen);

        return colorChoosen;
    }

    /**
     * Attempts to play a card from the machine player's hand. If no valid card is found, the machine draws a card.
     *
     * @return True if the machine has played a card, false otherwise.
     * @throws GameException If there is an error during card play.
     */
    private boolean putCardOnTheTable() throws GameException {
        if(!machinePlayer.getCardsPlayer().isEmpty()){
            System.out.println("Machine is choosing cards");
            int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
            Card card = machinePlayer.getCard(index);
            card = chooseAllowedCard(card);  // Choose a valid card to play
            if (!(card == null)) {
                gameUno.playCard(card);
                tableImageView.setImage(card.getImage());  // Update the table with the card played
                machinePlayer.removeCard(machinePlayer.getCardsPlayer().indexOf(card));  // Remove the played card from the hand
                gameUnoController.animateMachineCardToCenter(card);  // Animate the card being played
            }

            System.out.println("Machine cards after playing " + machinePlayer.getStringOfOwnCards());
            if (machinePlayer.getCardsPlayer().isEmpty()){
                System.out.println("Machine won");
                gameUno.setWinStatus(2);  // Set the win status to indicate the machine won
                gameUnoController.updateWinStatus();  // Update the win status in the controller
            }

            return machinePlayer.isOnTurn();
        }
        return true;
    }

    /**
     * Chooses a valid card from the machine player's hand based on the current game state.
     * If no valid card is found, the machine will draw a card.
     *
     * @param card The initial card to check.
     * @return The valid card to play, or null if no card is valid.
     * @throws GameException If there is an error during card validation.
     */
    private Card chooseAllowedCard(Card card) throws GameException {
        boolean isChoosenCardAllowed = false;
        boolean haveDrawedACard = false;
        int indexOfCardChoosen = machinePlayer.getCardsPlayer().indexOf(card);
        int searchingInDeckCounter = 0;
        int initialOwnCards = machinePlayer.getCardsPlayer().size();
        Card returnCard = card;

        do{
            table.verifyCardTypeOnTable(returnCard);  // Verify if the card is allowed to be played

            // If the card is not allowed, draw a new card
            if(searchingInDeckCounter == initialOwnCards){
                System.out.println("Machine draws a card");
                machinePlayer.drawsCard(gameUno.getDeck(), 1);
                returnCard = machinePlayer.getCard(machinePlayer.getCardsPlayer().size()-1);
                table.verifyCardTypeOnTable(returnCard);
                haveDrawedACard = true;
            }

            // Check if the chosen card can be played
            if(gameUno.canThrowCard()){
                isChoosenCardAllowed = true;
            } else {
                // If the card can't be played, look for another one
                if(!(searchingInDeckCounter == initialOwnCards)){
                    indexOfCardChoosen++;
                    searchingInDeckCounter++;
                    returnCard = machinePlayer.getCardsPlayer().get(indexOfCardChoosen);
                }
            }

            // If the machine drew a card and couldn't play, pass the turn
            if(haveDrawedACard && !isChoosenCardAllowed){
                gameUno.events.notifyShiftEvent("onturn");
                gameUno.events.notifyShiftToController("turnChangerController");
                returnCard = null;
                isChoosenCardAllowed = true;
            }
        } while(!isChoosenCardAllowed);

        return returnCard;
    }

    /**
     * Sets the flag indicating if the player has played.
     *
     * @param hasPlayerPlayed True if the player has played, false otherwise.
     */
    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    /**
     * Gets the machine player.
     *
     * @return The machine player.
     */

    public Player getMachinePlayer() {
        return machinePlayer;
    }
}