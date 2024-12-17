package org.example.eiscuno.model.deck;

import jdk.incubator.vector.VectorOperators;
import org.example.eiscuno.model.cardfactory.CardFactory;
import org.example.eiscuno.model.exception.GameException;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.example.eiscuno.model.card.Card;

import java.util.Collections;
import java.util.Stack;

/**
 * Represents a deck of Uno cards.
 */
public class Deck {
    private Stack<Card> deckOfCards;
    private CardFactory cardFactory;

    /**
     * Constructs a new deck of Uno cards and initializes it.
     */
    public Deck() {
        deckOfCards = new Stack<>();
        cardFactory = new CardFactory();
        initializeDeck();
    }


    /**
     * Constructs a new deck of Uno cards in test environment and don't initialize it.
     *  @param TestDiferentiation for differentiate constructor when testing
     */
    public Deck(boolean TestDiferentiation) {
        deckOfCards = new Stack<>();
        cardFactory = new CardFactory();
    }

    /**
     * Initializes the deck with cards based on the EISCUnoEnum values.
     */
    private void initializeDeck() {
        for (EISCUnoEnum cardEnum : EISCUnoEnum.values()) {
            if (cardEnum.name().startsWith("GREEN_") ||
                    cardEnum.name().startsWith("YELLOW_") ||
                    cardEnum.name().startsWith("BLUE_") ||
                    cardEnum.name().startsWith("RED_") ||
                    cardEnum.name().startsWith("SKIP_") ||
                    cardEnum.name().startsWith("RESERVE_") ||
                    cardEnum.name().startsWith("TWO_WILD_DRAW_") ||
                    cardEnum.name().equals("FOUR_WILD_DRAW") ||
                    cardEnum.name().equals("WILD")) {
                Card card = cardFactory.produceCard(cardEnum.getFilePath(), cardEnum.name());
                deckOfCards.push(card);
            }
        }
        Collections.shuffle(deckOfCards);

    }


    /**
     * Takes a card from the top of the deck.
     *
     * @return the card from the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card takeCard() {
            try{
                if (deckOfCards.isEmpty()){

                    throw new GameException.OutOfCardsInDeck("Se acaba la partida.");
                }else{
                return deckOfCards.pop();
                }

            }catch( GameException.OutOfCardsInDeck e){
                System.out.println(e.getMessage());
                return null;
    }

    }


    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return deckOfCards.isEmpty();
    }

    public Stack<Card> getDeckOfCards() {
        return deckOfCards;
    }
}
