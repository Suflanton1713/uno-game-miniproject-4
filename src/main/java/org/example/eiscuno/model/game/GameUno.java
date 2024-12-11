package org.example.eiscuno.model.game;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.observers.listeners.ShiftEventListenerAdaptor;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.observers.ShiftEventManager;
import org.example.eiscuno.model.observers.listeners.ShiftEventListener;
import org.example.eiscuno.model.observers.listeners.CardPlayedEventListener;
import org.example.eiscuno.model.table.Table;

/**
 * Represents a game of Uno.
 * This class manages the game logic and interactions between players, deck, and the table.
 */
public class GameUno implements IGameUno, CardPlayedEventListener {

    private Player humanPlayer;
    private Player machinePlayer;
    private Thread machineThread;
    private Deck deck;
    private Table table;
    public ShiftEventManager events;
    private boolean canThrowCard;
    private boolean hasToChangeColor;

    /**
     * Constructs a new GameUno instance.
     *
     * @param humanPlayer   The human player participating in the game.
     * @param machinePlayer The machine player participating in the game.
     * @param deck          The deck of cards used in the game.
     * @param table         The table where cards are placed during the game.
     */
    public GameUno(Player humanPlayer, Player machinePlayer, Deck deck, Table table) {
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.deck = deck;
        this.table = table;
        this.events = new ShiftEventManager("onturn", "offturn", "turnChangerController");
        this.hasToChangeColor = false;
        this.canThrowCard = true;
    }

    /**
     * Starts the Uno game by distributing cards to players.
     * The human player and the machine player each receive 10 cards from the deck.
     */
    @Override
    public void startGame() {
        for (int i = 0; i < 10; i++) {
            if (i < 5) {
                humanPlayer.addCard(this.deck.takeCard());
            } else {
                machinePlayer.addCard(this.deck.takeCard());
            }
        }


    }


    public void setListenersForShiftEvents(GameUnoController controller) {
        events.belongToShiftEvent("onturn",humanPlayer);
        System.out.println(machineThread);
        System.out.println((ShiftEventListener) machineThread);
        events.belongToShiftEvent("offturn", (ShiftEventListener) machineThread);
        events.belongToShiftEvent("turnChangerController", (ShiftEventListener) controller);

        System.out.println(events.getListeners());
    }

    /**
     * Allows a player to draw a specified number of cards from the deck.
     *
     * @param player        The player who will draw cards.
     * @param numberOfCards The number of cards to draw.
     */
    @Override
    public void eatCard(Player player, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            player.addCard(this.deck.takeCard());
        }
    }

    public void setMachineThread(Thread machineThread) {
        System.out.println("Se setea el thread " + machineThread);
        this.machineThread = machineThread;
    }

    /**
     * Places a card on the table during the game.
     *
     * @param card The card to be placed on the table.
     */
    @Override
    public void playCard(Card card) {
        this.table.addCardOnTheTable(card);
        events.notifyShiftEvent("onturn");
        events.notifyShiftToController("turnChangerController");
    }

    /**
     * Handles the scenario when a player shouts "Uno", forcing the other player to draw a card.
     *
     * @param playerWhoSang The player who shouted "Uno".
     */
    @Override
    public void haveSungOne(String playerWhoSang) {
        if (playerWhoSang.equals("HUMAN_PLAYER")) {
            machinePlayer.addCard(this.deck.takeCard());
        } else {
            humanPlayer.addCard(this.deck.takeCard());
        }
    }

    /**
     * Retrieves the current visible cards of the human player starting from a specific position.
     *
     * @param posInitCardToShow The initial position of the cards to show.
     * @return An array of cards visible to the human player.
     */
    @Override
    public Card[] getCurrentVisibleCardsHumanPlayer(int posInitCardToShow) {
        int totalCards = this.humanPlayer.getCardsPlayer().size();
        int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);
        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = this.humanPlayer.getCard(posInitCardToShow + i);
        }

        return cards;
    }

    /**
     * Checks if the game is over.
     *
     * @return True if the deck is empty, indicating the game is over; otherwise, false.
     */
    @Override
    public Boolean isGameOver() {
        return null;
    }

    @Override
    public void cantPlayCardUpdate(String eventType, Card card) {
        System.out.println("Card can't be throwed:" + card.getValue() + " " + card.getColor() );
        canThrowCard = false;

    }

    @Override
    public void canPlayCardUpdate(String eventType) {
        System.out.println("Card can be throwed");
        canThrowCard = true;

    }

    public boolean canThrowCard(){
        return canThrowCard;
    }


    public void drawCardUpdate(String eventType, int amount){
        System.out.println("Other player draw card");

        if(amount == 4){
            wildCardUpdate("Change color");
        }

        if(machinePlayer.isOnTurn()){
            humanPlayer.drawsCard(deck,amount);

        }else{
            machinePlayer.drawsCard(deck,amount);
        }

        events.notifyShiftEvent("onturn");
        events.notifyShiftToContrgitoller("turnChangerController");

    }



    public void skipTurnUpdate(String eventType){
        events.notifyShiftEvent("onturn");
        events.notifyShiftToController("turnChangerController");

        if(machinePlayer.isOnTurn()){

        }
    }

    public void reverseTurnUpdate(String eventType){
        events.notifyShiftEvent("onturn");
        events.notifyShiftToController("turnChangerController");
    }

    @Override
    public void wildCardUpdate(String eventType) {
        System.out.println("Wild card");
        hasToChangeColor = true;
    }

    public boolean HasToChangeColor() {
        return hasToChangeColor;
    }

    public void setHasToChangeColor(boolean hadToChangeColor) {
        this.hasToChangeColor = hadToChangeColor;
    }

    public Deck getDeck() {
        return deck;
    }

    public Table getTable() {
        return table;
    }
}
