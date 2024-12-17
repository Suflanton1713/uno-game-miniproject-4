package org.example.eiscuno.model.cardfactory;

import org.example.eiscuno.model.card.Card;

public interface ICardFactory {
    Card produceCard(String url, String EnumName);
}
