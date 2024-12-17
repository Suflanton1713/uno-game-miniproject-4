package org.example.eiscuno.controller;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameUnoControllerTest {
    Deck deck;
    GameUnoController controller;

    @BeforeEach
    public void setUp() {
        controller = new GameUnoController();
        deck = new Deck(true);
        controller.setDeck(deck);
    }


    @Test
    public void initialCardWhenThereAreNoCardsInDeck(){

        assertThrows(RuntimeException.class, ()->{
            controller.initialCard();
        });

    }

    @Test
    public void initialCardWhenThereAreMulticolorCardsInDeck(){
        Card card = new Card("Wild", "MULTICOLOR");
        controller.getDeck().getDeckOfCards().push(card);
        assertSame(card, controller.initialCard());

    }

    @Test
    public void initialCardWhenThereIsANormalCardInDeck(){
        Card card = new Card("9", "GREEN");
        controller.getDeck().getDeckOfCards().push(card);
        assertSame(card, controller.initialCard());
    }

    @Test
    public void forcingErrorWhenThereisMulticolorCardInDeck(){
        Card cardPushed = new Card("Wild", "MULTICOLOR");
        Card card = new Card("9", "GREEN");
        controller.getDeck().getDeckOfCards().push(cardPushed);
        assertSame(card, controller.initialCard());
    }

    @Test
    public void watchingColorChangesWhenMulticolorCard(){
        Card cardPushed = new Card("Wild", "MULTICOLOR");
        Card card1 = new Card("9", "GREEN");
        Card card2 = new Card("9", "BLUE");
        Card card3 = new Card("9", "RED");
        Card card4 = new Card("9", "YELLOW");
        assertAll("Verifying all color changes",
                () -> {
                    controller.getDeck().getDeckOfCards().push(cardPushed);
                    assertEquals(card1.getColor(), controller.initialCard().getColor());
                },
                () -> {
                    controller.getDeck().getDeckOfCards().push(cardPushed);
                    assertEquals(card2.getColor(), controller.initialCard().getColor());
                },
                () -> {
                    controller.getDeck().getDeckOfCards().push(cardPushed);
                    assertEquals(card3.getColor(), controller.initialCard().getColor());
                },
                () -> {
                    controller.getDeck().getDeckOfCards().push(cardPushed);
                    assertEquals(card4.getColor(), controller.initialCard().getColor());
                }
        );
    }


}