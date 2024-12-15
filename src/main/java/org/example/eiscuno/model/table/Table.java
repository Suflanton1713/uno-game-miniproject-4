package org.example.eiscuno.model.table;

import javafx.scene.control.Tab;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.observers.CardPlayerEventManager;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents the table in the Uno game where cards are played.
 */
public class Table{
    private static ArrayList<Card> cardsTable;
    public CardPlayerEventManager events;


    /**
     * Constructs a new Table object with no cards on it.
     */
    public Table(){

        this.cardsTable = new ArrayList<Card>();
        this.events = new CardPlayerEventManager("threwCard", "skip", "reverse", "wild", "drawCards");
    }

    public void setListenerForCardEvent(GameUno gameUno) {
        events.belongToCardEvent("threwCard",gameUno);
        events.belongToCardEvent("skip",gameUno);
        events.belongToCardEvent("reverse",gameUno);
        events.belongToCardEvent("wild",gameUno);
        events.belongToCardEvent("drawCards",gameUno);

        System.out.println(events.getListeners());
    }

    /**
     * Adds a card to the table.
     *
     * @param card The card to be added to the table.
     */
    public void addCardOnTheTable(Card card){
        this.cardsTable.add(card);
    }

    /**
     * Retrieves the current card on the table.
     *
     * @return The card currently on the table.
     * @throws IndexOutOfBoundsException if there are no cards on the table.
     */
    public  Card getCurrentCardOnTheTable() throws IndexOutOfBoundsException {
        if (cardsTable.isEmpty()) {
            throw new IndexOutOfBoundsException("There are no cards on the table.");
        }
        return this.cardsTable.get(this.cardsTable.size()-1);
    }

    public void verifyCardTypeOnTable(Card card){
        if(itsAWildCard(card)){
            events.notifyCanPlayCard("threwCard");
        }else{
            System.out.println("had Card same color that in table");
            System.out.println(hadCardSameColor(card));
            System.out.println("had Card same value that in table");
            System.out.println(hadCardSameValue(card));
            if(hadCardSameColor(card) || hadCardSameValue(card)){

                events.notifyCanPlayCard("threwCard");
                itsASpecialCard(card);
            }else{
                events.notifyCantPlayCard("threwCard", card);
            }
        }
    }

    public boolean hadCardSameColor(Card cardAdded) {
        return Objects.equals(cardAdded.getColor(), getCurrentCardOnTheTable().getColor()) || getCurrentCardOnTheTable().getColor() == null;
    }


    public boolean hadCardSameValue(Card cardAdded){
        return Objects.equals(cardAdded.getValue(), getCurrentCardOnTheTable().getValue()) || getCurrentCardOnTheTable().getValue() == null;
    }

    public boolean itsAWildCard(Card cardAdded){
        switch (cardAdded.getValue()){
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

    public void itsASpecialCard(Card cardAdded){
        switch (cardAdded.getValue()){
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

    public void setColorForTable(String color){
        this.getCurrentCardOnTheTable().setColor(color);
    }






}
