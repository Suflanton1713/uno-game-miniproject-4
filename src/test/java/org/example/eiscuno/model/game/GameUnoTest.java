package org.example.eiscuno.model.game;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameUnoTest {
    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private Card normalCard1;
    private Card normalCard2;
    private Card normalCard3;
    private Card normalCard4;
    private Card normalCard5;
    private Card specialCard1;
    private Card specialCard2;
    private Card specialCard3;
    private Card specialCard4;
    private Card specialCard5;

    @BeforeEach
    public void setUp() {
            humanPlayer = new Player("HUMAN_PLAYER");
            machinePlayer = new Player("MACHINE_PLAYER");
            deck = new Deck(true);
            table = new Table();

        gameUno = new GameUno(humanPlayer,  machinePlayer,  deck,  table);
        normalCard1 = new Card("1", "MULTICOLOR");
        normalCard2 = new Card("2", "GREEN");
        normalCard3 = new Card("3", "BLUE");
        normalCard4 = new Card("4", "RED");
        normalCard5 = new Card("5", "YELLOW");
        specialCard1 = new Card("FourWildDraw", "MULTICOLOR");
        specialCard2 = new Card("TwoWildDraw", "GREEN");
        specialCard3 = new Card("Reverse", "BLUE");
        specialCard4 = new Card("Skip", "RED");
        specialCard5 = new Card("Wild", "MULTICOLOR");
    }

    @Test
    public void verifyingWhenAPlayerEatsACardFromDeck(){
        deck.getDeckOfCards().push(normalCard1);
        deck.getDeckOfCards().push(normalCard2);
        gameUno.eatCard(humanPlayer, 2);
        assertTrue(humanPlayer.getCardsPlayer().contains(normalCard1) && humanPlayer.getCardsPlayer().contains(normalCard2));
    }

    @Test
    public void verifyingExceptionsForMultipleIllegalGameStateWhenDrawingCard(){

        assertAll("Verifying exceptions when drawing cards",
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> {
                        gameUno.drawCardUpdate("drawCard",0);
                    });
                },
                () -> {
                    assertThrows(IllegalStateException.class, () -> {
                        gameUno.drawCardUpdate("drawCard",1);
                    });
                }
        );

    }

    @Test
    public void verifyingEatingCardsFeatureAndUnstackingCardsFeatureWhenMachineSingingOne(){
        deck.getDeckOfCards().push(normalCard1);
        deck.getDeckOfCards().push(normalCard2);

        assertAll("Verifying eating cards feature when singing one",
                () -> {
                        gameUno.haveSungOne("HUMAN_PLAYER");
                        assertEquals(machinePlayer.getCardsPlayer().get(0), normalCard2);
                },
                () -> {
                        gameUno.haveSungOne("MACHINE_PLAYER");
                        assertEquals(humanPlayer.getCardsPlayer().get(0), normalCard1);
                }
        );
        
    }

    @Test
    public void gettingNullWhenThereIsNoCardInDeckWhenSingingOne(){
        deck.getDeckOfCards().push(normalCard1);

        assertAll("Verifying eating cards feature when singing one",
                () -> {
                    gameUno.haveSungOne("HUMAN_PLAYER");
                    assertEquals(machinePlayer.getCardsPlayer().get(0), normalCard1);
                },
                () -> {
                    gameUno.haveSungOne("MACHINE_PLAYER");
                    assertEquals(humanPlayer.getCardsPlayer().get(0), null);
                }
        );



    }


}