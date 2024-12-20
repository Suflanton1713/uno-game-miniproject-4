package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Represents a card in the Uno game.
 */
public class Card {
    private String url;
    private String value;
    private String color;
    private Image image;
    private ImageView cardImageView;

    /**
     * Constructs a Card with the specified image URL and name.
     *
     * @param url the URL of the card image
     * @param value of the card
     */
    public Card(String url, String value, String color) {
        this.url = url;
        this.value = value;
        this.color = color;
        this.image = new Image(String.valueOf(getClass().getResource(url)));
        this.cardImageView = createCardImageView();
    }

    public Card( String value, String color) {
        this.value = value;
        this.color = color;
    }



    /**
     * Creates and configures the ImageView for the card.
     *
     * @return the configured ImageView of the card
     */
    private ImageView createCardImageView() {
        ImageView card = new ImageView(this.image);
        card.setY(16);
        card.setFitHeight(90);
        card.setFitWidth(70);
        return card;
    }

    /**
     * Gets the ImageView representation of the card.
     *
     * @return the ImageView of the card
     */
    public ImageView getCard() {
        return cardImageView;
    }

    /**
     * Gets the image of the card.
     *
     * @return the Image of the card
     */
    public Image getImage() {
        return image;
    }

    public String getValue() {
        return value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        if(Objects.equals(color, "GREEN")){
            this.url = "/org/example/eiscuno/cards-uno/wild_green.png";
            this.image = new Image(String.valueOf(getClass().getResource(url)));
            this.cardImageView = createCardImageView();

        }else if(Objects.equals(color, "BLUE")){
            this.url = "/org/example/eiscuno/cards-uno/wild_blue.png";
            this.image = new Image(String.valueOf(getClass().getResource(url)));
            this.cardImageView = createCardImageView();

        }else if(Objects.equals(color, "RED")){
            this.url = "/org/example/eiscuno/cards-uno/wild_red.png";
            this.image = new Image(String.valueOf(getClass().getResource(url)));
            this.cardImageView = createCardImageView();
        }else{
            this.url = "/org/example/eiscuno/cards-uno/wild_yellow.png";
            this.image = new Image(String.valueOf(getClass().getResource(url)));
            this.cardImageView = createCardImageView();
        }
    }
}
