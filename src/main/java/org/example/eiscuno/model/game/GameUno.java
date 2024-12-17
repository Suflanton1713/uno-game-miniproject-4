package org.example.eiscuno.model.game;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.exception.GameException;
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
    private int winStatus;
    private boolean humanSingUno;
    private boolean machineSingUno;

    /**
     * Constructs a new GameUno instance.
     *
     * @param humanPlayer   The human player participating in the game.
     * @param machinePlayer The machine player participating in the game.
     * @param deck          The deck of cards used in the game.
     * @param table         The table where cards are placed during the game.
     */
    public GameUno(Player humanPlayer, Player machinePlayer, Deck deck, Table table) {
        winStatus = 0;// 0 normal game, 1 human win, 2 machine win
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.deck = deck;
        this.table = table;
        this.events = new ShiftEventManager("onturn", "offturn", "turnChangerController");
        this.hasToChangeColor = false;
        this.canThrowCard = true;
        this.humanSingUno = false;

    }

    /**
     * Starts the Uno game by distributing cards to players.
     * The human player and the machine player each receive 10 cards from the deck.
     */
    @Override
    public void startGame() {
        humanPlayer.setOnTurn(true);
        for (int i = 0; i < 4; i++) {
            if (i < 2) {
                humanPlayer.addCard(this.deck.takeCard());
            } else {
                machinePlayer.addCard(this.deck.takeCard());
            }
        }


    }


    public void setListenersForShiftEvents(GameUnoController controller) {
        events.belongToShiftEvent("onturn", humanPlayer);
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
        try {

            if (winStatus != 0) {
                throw new GameException.gameEndedException();
            }

            this.table.addCardOnTheTable(card);
            events.notifyShiftEvent("onturn");
            events.notifyShiftToController("turnChangerController");
            System.out.println(machinePlayer.getStringOfOwnCards());
            setHumanSingUno(false);
            setMachineSingUno(false);

        } catch (GameException.gameEndedException e) {
            System.out.println(e.getMessage());
            System.out.println("Game has ended.");
        }

    }

    public void passTurnWhenUnoSung() {
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
        int numVisibleCards = Math.min(8, totalCards - posInitCardToShow);
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
        if (winStatus != 0) {
            return true;
        }
        return false;
    }

    @Override
    public void cantPlayCardUpdate(String eventType, Card card) {
        // Notifies that the card cannot be played and logs the card's value and color.
        System.out.println("Card can't be throwed:" + card.getValue() + " " + card.getColor());
        canThrowCard = false;
    }

    @Override
    public void canPlayCardUpdate(String eventType) {
        // Notifies that the card can be played.
        System.out.println("Card can be throwed");
        canThrowCard = true;
    }

    /**
     * Returns whether a card can be thrown or not.
     *
     * @return true if the card can be thrown, false otherwise.
     */
    public boolean canThrowCard() {
        return canThrowCard;
    }

    /**
     * Updates the game state when a player draws cards.
     * It ensures valid amounts and conditions are met before allowing the card draw.
     *
     * @param eventType The type of event (not used in this method but required by the interface).
     * @param amount    The number of cards to draw.
     * @throws IllegalArgumentException if the amount is zero.
     * @throws IllegalStateException    if neither player is on turn.
     */
    public void drawCardUpdate(String eventType, int amount) {
        try {
            // Ensure the amount of cards drawn is valid.
            if (amount == 0) throw new IllegalArgumentException("Can't draw zero cards.");
            if (!machinePlayer.isOnTurn() && !humanPlayer.isOnTurn())
                throw new IllegalStateException("Illegal state of turns.");

            System.out.println("Other player draw card");

            // Handle special rules for drawing cards.
            if (amount == 4) {
                wildCardUpdate("Change color");
            }

            if (amount == 1) {
                setHumanSingUno(false);
                setMachineSingUno(false);
            }

            // Draw cards for the active player.
            if (machinePlayer.isOnTurn()) {
                humanPlayer.drawsCard(deck, amount);
            } else {
                machinePlayer.drawsCard(deck, amount);
            }

            // Notify that the turn has changed and that the controller should be updated.
            events.notifyShiftEvent("onturn");
            events.notifyShiftToController("turnChangerController");

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e + "No cards drawed");
        } catch (IllegalStateException e1) {
            throw new IllegalStateException(e1 + "No turns setted");
        }
    }

    /**
     * Updates the game state when a player's turn is skipped.
     * It notifies that the turn has changed and updates the controller.
     *
     * @param eventType The type of event (not used in this method but required by the interface).
     */
    public void skipTurnUpdate(String eventType) {
        events.notifyShiftEvent("onturn");
        events.notifyShiftToController("turnChangerController");
    }

    /**
     * Updates the game state when the direction of turns is reversed.
     * It notifies that the turn has changed and updates the controller.
     *
     * @param eventType The type of event (not used in this method but required by the interface).
     */
    public void reverseTurnUpdate(String eventType) {
        events.notifyShiftEvent("onturn");
        events.notifyShiftToController("turnChangerController");
    }

    @Override
    public void wildCardUpdate(String eventType) {
        // Notifies that a wild card has been played and the color change is required.
        System.out.println("Wild card");
        hasToChangeColor = true;
    }

    /**
     * Returns whether a color change is required after a wild card is played.
     *
     * @return true if the color change is required, false otherwise.
     */
    public boolean HasToChangeColor() {
        return hasToChangeColor;
    }

    /**
     * Sets whether a color change is required after a wild card is played.
     *
     * @param hadToChangeColor true if the color change is required, false otherwise.
     */
    public void setHasToChangeColor(boolean hadToChangeColor) {
        this.hasToChangeColor = hadToChangeColor;
    }

    public Deck getDeck() {
        return deck;
    }

    public Table getTable() {
        return table;
    }

    /**
     * Sets the win status of the game.
     *
     * @param winStatus The win status to set.
     */
    public void setWinStatus(int winStatus) {
        this.winStatus = winStatus;
    }

    /**
     * Returns the current win status of the game.
     *
     * @return The win status.
     */
    public int getWinStatus() {
        return winStatus;
    }

    /**
     * Returns whether the human player has declared "UNO".
     *
     * @return true if the human player has declared "UNO", false otherwise.
     */
    public boolean getHumanSingUno() {
        return humanSingUno;
    }

    /**
     * Sets the human player's "UNO" status.
     *
     * @param val true if the human player has declared "UNO", false otherwise.
     */
    public void setHumanSingUno(boolean val) {
        humanSingUno = val;
        System.out.println("Sing uno es:" + humanSingUno);
    }

    /**
     * Returns whether the human player has declared "UNO".
     *
     * @return true if the human player has declared "UNO", false otherwise.
     */
    public boolean isHumanSingUno() {
        return humanSingUno;
    }

    /**
     * Returns whether the machine player has declared "UNO".
     *
     * @return true if the machine player has declared "UNO", false otherwise.
     */
    public boolean isMachineSingUno() {
        return machineSingUno;
    }

    /**
     * Sets the machine player's "UNO" status.
     *
     * @param machineSingUno true if the machine player has declared "UNO", false otherwise.
     */
    public void setMachineSingUno(boolean machineSingUno) {
        this.machineSingUno = machineSingUno;
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public Player getMachinePlayer() {
        return machinePlayer;
    }
}