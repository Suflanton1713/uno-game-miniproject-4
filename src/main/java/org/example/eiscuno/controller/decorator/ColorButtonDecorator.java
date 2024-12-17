package org.example.eiscuno.controller.decorator;

import javafx.scene.control.Button;

/**
 * The ColorButtonDecorator class is a concrete implementation of the ButtonDecorator interface.
 * It is used to decorate a button with a specified color and additional styling properties.
 */
public class ColorButtonDecorator implements ButtonDecorator {

    // The color to be applied to the button's background.
    private String color;

    /**
     * Constructor to initialize the ColorButtonDecorator with a specific color.
     *
     * @param color The color that will be applied to the button's background.
     */
    public ColorButtonDecorator(String color) {
        this.color = color;
    }

    /**
     * Applies the decoration (color and style) to the provided button.
     * This method sets the background color, minimum width, minimum height, and background radius of the button.
     *
     * @param button The button to which the decoration will be applied.
     */
    @Override
    public void decorate(Button button) {
        button.setStyle("-fx-background-color: " + color + "; -fx-min-width: 60px; -fx-min-height: 60px; -fx-background-radius: 30px;");
    }
}
