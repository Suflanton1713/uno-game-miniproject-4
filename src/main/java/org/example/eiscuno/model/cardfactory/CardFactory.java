/**
 * Clase que implementa la fábrica de cartas para el juego Uno.
 * Proporciona métodos para crear instancias de cartas basadas en nombres y valores.
 */
package org.example.eiscuno.model.cardfactory;

import org.example.eiscuno.model.card.Card;

public class CardFactory implements ICardFactory {

    /**
     * Crea una carta con una URL de imagen y un nombre enumerado que indica sus características.
     *
     * @param url      URL de la imagen asociada a la carta.
     * @param EnumName Nombre del enumerado que indica color y valor de la carta.
     * @return Una nueva instancia de {@link Card}.
     */
    @Override
    public Card produceCard(String url, String EnumName) {
        String color = getCardColor(EnumName);
        String value = getCardValue(EnumName);

        return new Card(url, value, color);
    }

    /**
     * Crea una carta utilizando solo un nombre enumerado para determinar sus características.
     *
     * @param EnumName Nombre del enumerado que indica color y valor de la carta.
     * @return Una nueva instancia de {@link Card}.
     */
    public Card produceCard(String EnumName) {
        String color = getCardColor(EnumName);
        String value = getCardValue(EnumName);

        return new Card(value, color);
    }

    /**
     * Obtiene el valor de la carta a partir de su nombre enumerado.
     *
     * @param name Nombre del enumerado.
     * @return El valor de la carta como cadena de texto.
     */
    private String getCardValue(String name) {
        if (name.endsWith("0")) {
            return "0";
        } else if (name.endsWith("1")) {
            return "1";
        } else if (name.endsWith("2")) {
            return "2";
        } else if (name.endsWith("3")) {
            return "3";
        } else if (name.endsWith("4")) {
            return "4";
        } else if (name.endsWith("5")) {
            return "5";
        } else if (name.endsWith("6")) {
            return "6";
        } else if (name.endsWith("7")) {
            return "7";
        } else if (name.endsWith("8")) {
            return "8";
        } else if (name.endsWith("9")) {
            return "9";
        } else if (name.startsWith("RESERVE_")) {
            return "Reverse";
        } else if (name.startsWith("SKIP_")) {
            return "Skip";
        } else if (name.startsWith("TWO_WILD_DRAW_")) {
            return "TwoWildDraw";
        } else if (name.equals("FOUR_WILD_DRAW")) {
            return "FourWildDraw";
        } else if (name.equals("WILD")) {
            return "Wild";
        } else {
            return null;
        }
    }

    /**
     * Obtiene el color de la carta a partir de su nombre enumerado.
     *
     * @param name Nombre del enumerado.
     * @return El color de la carta como cadena de texto o "MULTICOLOR" para cartas comodín.
     */
    private String getCardColor(String name) {
        if (name.startsWith("GREEN")) {
            return "GREEN";
        } else if (name.startsWith("YELLOW")) {
            return "YELLOW";
        } else if (name.startsWith("BLUE")) {
            return "BLUE";
        } else if (name.startsWith("RED")) {
            return "RED";
        } else if (name.startsWith("RESERVE_") || name.startsWith("TWO_WILD_DRAW_") || name.startsWith("SKIP_")) {
            if (name.endsWith("GREEN")) {
                return "GREEN";
            } else if (name.endsWith("YELLOW")) {
                return "YELLOW";
            } else if (name.endsWith("BLUE")) {
                return "BLUE";
            } else if (name.endsWith("RED")) {
                return "RED";
            }
        } else if (name.startsWith("FOUR_WILD_DRAW")) {
            return "MULTICOLOR";
        } else if (name.startsWith("WILD")) {
            return "MULTICOLOR";
        } else {
            return null;
        }
        return null;
    }
}
