package org.example.eiscuno.model.table;

import javafx.scene.control.Tab;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.exception.GameException;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.observers.CardPlayerEventManager;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents the table in the Uno game where cards are played.
 */
public class Table {
    private static ArrayList<Card> cardsTable; // List of cards placed on the table
    public CardPlayerEventManager events; // Event manager to handle card-related events

    /**
     * Constructs a new Table object with no cards on it and initializes the event manager.
     */
    public Table() {
        this.cardsTable = new ArrayList<Card>();
        this.events = new CardPlayerEventManager("threwCard", "skip", "reverse", "wild", "drawCards");
    }

    /**
     * Sets listeners for specific card-related events.
     *
     * @param gameUno The GameUno object to associate with the events.
     */
    public void setListenerForCardEvent(GameUno gameUno) {
        events.belongToCardEvent("threwCard", gameUno);
        events.belongToCardEvent("skip", gameUno);
        events.belongToCardEvent("reverse", gameUno);
        events.belongToCardEvent("wild", gameUno);
        events.belongToCardEvent("drawCards", gameUno);

        System.out.println(events.getListeners());
    }

    /**
     * Adds a card to the table.
     *
     * @param card The card to be added to the table.
     */
    public void addCardOnTheTable(Card card) {
        this.cardsTable.add(card);
    }

    /**
     * Retrieves the current card on the table (the last card added).
     *
     * @return The card currently on the table.
     * @throws IndexOutOfBoundsException if there are no cards on the table.
     */
    public Card getCurrentCardOnTheTable() throws IndexOutOfBoundsException {
        if (cardsTable.isEmpty()) {
            throw new IndexOutOfBoundsException("There are no cards on the table.");
        }
        return this.cardsTable.get(this.cardsTable.size() - 1);
    }

    /**
     * Verifies if the added card can be played based on the current card on the table.
     * It checks for wild cards, matching colors, or matching values.
     *
     * @param card The card to verify.
     * @throws GameException if the card cannot be played.
     */
    public void verifyCardTypeOnTable(Card card) throws GameException {
        if (itsAWildCard(card)) {
            events.notifyCanPlayCard("threwCard");
        } else {
            System.out.println("had Card same color that in table");
            System.out.println(hadCardSameColor(card));
            System.out.println("had Card same value that in table");
            System.out.println(hadCardSameValue(card));
            if (hadCardSameColor(card) || hadCardSameValue(card)) {
                events.notifyCanPlayCard("threwCard");
                itsASpecialCard(card);
            } else {
                events.notifyCantPlayCard("threwCard", card);
            }
        }
    }

    /**
     * Checks if the added card has the same color as the current card on the table.
     *
     * @param cardAdded The card to compare.
     * @return true if the card has the same color; false otherwise.
     * @throws GameException if the card has an invalid color.
     */
    public boolean hadCardSameColor(Card cardAdded) throws GameException {
        try {
            if (!Objects.equals(cardAdded.getColor(), "BLUE") &&
                    !Objects.equals(cardAdded.getColor(), "YELLOW") &&
                    !Objects.equals(cardAdded.getColor(), "RED") &&
                    !Objects.equals(cardAdded.getColor(), "GREEN") &&
                    !Objects.equals(cardAdded.getColor(), "MULTICOLOR")) {
                throw new GameException.IllegalCardColor(" In table when comparing cards");
            }

            return Objects.equals(cardAdded.getColor(), getCurrentCardOnTheTable().getColor()) ||
                    getCurrentCardOnTheTable().getColor() == null;
        } catch (GameException.IllegalCardColor e) {
            System.out.println(e.getMessage());
            throw new GameException(" In table when comparing cards");
        }
    }

    /**
     * Checks if the added card has the same value as the current card on the table.
     *
     * @param cardAdded The card to compare.
     * @return true if the card has the same value; false otherwise.
     * @throws GameException if the card has an invalid value.
     */
    public boolean hadCardSameValue(Card cardAdded) throws GameException {
        try {
            if (!isValidCardValue(cardAdded.getValue())) {
                throw new GameException.IllegalCardValue("In table when comparing cards: Invalid card value");
            }
            return Objects.equals(cardAdded.getValue(), getCurrentCardOnTheTable().getValue()) ||
                    getCurrentCardOnTheTable().getValue() == null;
        } catch (GameException.IllegalCardValue e) {
            System.out.println(e.getMessage());
            throw new GameException("In table when comparing cards: Invalid card value");
        }
    }

    /**
     * Validates if the card value is one of the allowed Uno card values.
     *
     * @param value The card value to validate.
     * @return true if the value is valid; false otherwise.
     */
    private boolean isValidCardValue(String value) {
        return value.matches("^(0|1|2|3|4|5|6|7|8|9|Skip|Reverse|Wild|TwoWildDraw|FourWildDraw)$");
    }

    /**
     * Checks if the added card is a wild card and triggers the corresponding event.
     *
     * @param cardAdded The card to check.
     * @return true if the card is a wild card; false otherwise.
     */
    public boolean itsAWildCard(Card cardAdded) {
        switch (cardAdded.getValue()) {
            case "Wild":
                System.out.println("Changing colors");
                events.notifyWildCard("wild");
                break;
            case "FourWildDraw":
                System.out.println("Taking four cards...");
                events.notifyDrawCards("drawCards", 4);
                break;
            default:
                System.out.println("Not a Wild card");
                break;
        }

        System.out.println(("CARD COLOR: " + cardAdded.getColor()));
        System.out.println((Objects.equals(cardAdded.getColor(), "MULTICOLOR")));

        return (Objects.equals(cardAdded.getColor(), "MULTICOLOR"));
    }

    /**
     * Checks if the added card is a special card (Skip, Reverse, or Draw cards) and triggers the corresponding event.
     *
     * @param cardAdded The card to check.
     */
    public void itsASpecialCard(Card cardAdded) {
        switch (cardAdded.getValue()) {
            case "Skip":
                System.out.println("Skipping turn...");
                events.notifySkipTurn("skip");
                break;
            case "Reverse":
                System.out.println("Reversing turn...");
                events.notifyReverseTurn("reverse");
                break;
            case "TwoWildDraw":
                System.out.println("Taking two cards...");
                events.notifyDrawCards("drawCards", 2);
                break;
            default:
                System.out.println("Just a number card...");
                break;
        }
    }

    /**
     * Sets the color for the current card on the table.
     *
     * @param color The color to set.
     */
    public void setColorForTable(String color) {
        this.getCurrentCardOnTheTable().setColor(color);
    }
}







