package org.example.eiscuno.model.player;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.observers.listeners.ShiftEventListener;

import java.util.ArrayList;

/**
 * Represents a player in the Uno game.
 */
public class Player implements IPlayer, ShiftEventListener {
    private ArrayList<Card> cardsPlayer;
    private String typePlayer;
    private boolean isOnTurn;


    /**
     * Constructs a new Player object with an empty hand of cards.
     */
    public Player(String typePlayer){
        this.cardsPlayer = new ArrayList<Card>();
        Card card1 =  new Card("/org/example/eiscuno/cards-uno/wild.png", "Wild", "MULTICOLOR");

        cardsPlayer.add(card1);

        this.typePlayer = typePlayer;
        isOnTurn = false;
    };

    /**
     * Adds a card to the player's hand.
     *
     * @param card The card to be added to the player's hand.
     */
    @Override
    public void addCard(Card card){
        cardsPlayer.add(card);
    }


    /**
     * Retrieves all cards currently held by the player.
     *
     * @return An ArrayList containing all cards in the player's hand.
     */
    @Override
    public ArrayList<Card> getCardsPlayer() {
        return cardsPlayer;
    }

    /**
     * Removes a card from the player's hand based on its index.
     *
     * @param index The index of the card to remove.
     */
    @Override
    public void removeCard(int index) {
        cardsPlayer.remove(index);
    }

    /**
     * Retrieves a card from the player's hand based on its index.
     *
     * @param index The index of the card to retrieve.
     * @return The card at the specified index in the player's hand.
     */
    @Override
    public Card getCard(int index){
        return cardsPlayer.get(index);
    }

    public String getTypePlayer() {
        return typePlayer;
    }

    public boolean isOnTurn() {
        return isOnTurn;
    }

    public void setOnTurn(boolean onTurn) {
        isOnTurn = onTurn;
    }

    @Override
    public void drawsCard(Deck deck, int Amount){
        for(int i = 0; i<Amount; i++){
            addCard(deck.takeCard());
        }
    }

    @Override
    public void onTurnUpdate(String eventType) {
        System.out.println("Player is on turn");
        isOnTurn = true;

    }

    @Override
    public void offTurnUpdate(String eventType) {
        System.out.println("Player is not on turn");
        isOnTurn = false;
    }

    @Override
    public void update(String eventType) {
        System.out.println("Player has played, continues machine");

    }

    public String getStringOfOwnCards(){
        String result = "[";
        for(Card card :cardsPlayer){
            result = result + card.getValue() + card.getColor() + " ,";
        }
        result = result + "]";
        return result;
    }


}