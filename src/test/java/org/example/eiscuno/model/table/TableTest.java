package org.example.eiscuno.model.table;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.exception.GameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    Table table;
    Card tableCard1;
    Card tableCard2;
    Card tableCard3;
    Card tableCard4;
    Card tableCard5;
    Card compareCard1;
    Card compareCard2;
    Card compareCard3;
    Card compareCard4;
    Card compareCard5;
    Card wrongColorCard;
    Card wrongValueCard;

    @BeforeEach
    public void setUp() {
        table = new Table();
        tableCard1 = new Card("Wild", "MULTICOLOR");
        tableCard2 = new Card("9", "GREEN");
        tableCard3 = new Card("Skip", "BLUE");
        tableCard4 = new Card("Reverse", "RED");
        tableCard5 = new Card("TwoWildDraw", "YELLOW");
        compareCard1 = new Card("FourWildDraw", "MULTICOLOR");
        compareCard2 = new Card("TwoWildDraw", "GREEN");
        compareCard3 = new Card("Reverse", "BLUE");
        compareCard4 = new Card("Skip", "RED");
        compareCard5 = new Card("9", "YELLOW");
        wrongColorCard = new Card("8", "PANDA");
        wrongValueCard = new Card("777", "YELLOW");
    }

    @Test
    public void comparingAllColorsWhenPuttingCardOnTable(){

        assertAll("Verifying colors of cards",
                () -> {
                    table.addCardOnTheTable(tableCard1);
                    assertTrue(table.hadCardSameColor(compareCard1));
                },
                () -> {
                    table.addCardOnTheTable(tableCard2);
                    assertTrue(table.hadCardSameColor(compareCard2));
                },
                () -> {
                    table.addCardOnTheTable(tableCard3);
                    assertTrue(table.hadCardSameColor(compareCard3));
                },
                () -> {
                    table.addCardOnTheTable(tableCard4);
                    assertTrue(table.hadCardSameColor(compareCard4));
                },
                () -> {
                    table.addCardOnTheTable(tableCard5);
                    assertTrue(table.hadCardSameColor(compareCard5));
                }
        );

    }

    @Test
    public void comparingAllValuesWhenPuttingCardOnTable(){

        assertAll("Verifying values of cards",
                () -> {
                    table.addCardOnTheTable(tableCard5);
                    System.out.println(tableCard5.getValue() + compareCard2.getValue());
                    assertTrue(table.hadCardSameValue(compareCard2));
                },
                () -> {
                    table.addCardOnTheTable(tableCard4);
                    System.out.println(tableCard4.getValue() + compareCard3.getValue());
                    assertTrue(table.hadCardSameValue(compareCard3));  // Comparar color
                },
                () -> {
                    table.addCardOnTheTable(tableCard3);
                    System.out.println(tableCard3.getValue() + compareCard4.getValue());
                    assertTrue(table.hadCardSameValue(compareCard4));  // Comparar color
                },
                () -> {
                    table.addCardOnTheTable(tableCard2);
                    System.out.println(tableCard2.getValue() + compareCard5.getValue());
                    assertTrue(table.hadCardSameValue(compareCard5));  // Comparar color
                }
        );

    }

    @Test
    public void forcingColorComparationToReturnFalse() throws GameException {
        table.addCardOnTheTable(tableCard5);
        assertFalse(table.hadCardSameColor(compareCard2));
    }

    @Test
    public void forcingValueComparationToReturnFalse() throws GameException {
        table.addCardOnTheTable(tableCard3);
        assertFalse(table.hadCardSameValue(compareCard3));

    }

    @Test
    public void forcingExceptionWhenIllegalColorAppear() throws GameException {
        table.addCardOnTheTable(tableCard1);
        System.out.println(wrongColorCard.getColor());
        assertThrows(GameException.class, () ->{
            table.hadCardSameColor(wrongColorCard);
        });

    }

    @Test
    public void forcingExceptionWhenIllegalValueAppear() throws GameException {
        table.addCardOnTheTable(tableCard1);
        System.out.println(wrongValueCard.getValue());
        assertThrows(GameException.class, () ->{
            table.hadCardSameValue(wrongValueCard);
        });

    }

    @Test
    public void checkingWhenPlayedCardIsAWildCardAndPassesWildCardComprobation() {
        assertTrue(table.itsAWildCard(tableCard1));
    }

    @Test
    public void checkingWhenPlayedCardIsAFourWildCardAndPassesWildCardComprobation() {
        assertTrue(table.itsAWildCard(compareCard1));
    }

    @Test
    public void checkingWhenPlayedCardIsNotAWildCardAndNotPassesWildCardComprobation() {
        assertFalse(table.itsAWildCard(compareCard2));
    }


}