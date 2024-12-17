package org.example.eiscuno.controller.decorator;
import javafx.scene.control.Button;

public class ColorButtonDecorator implements ButtonDecorator {
    private String color;

    public ColorButtonDecorator(String color) {
        this.color = color;
    }

    @Override
    public void decorate(Button button) {
        // Establecer el color de fondo del botón
        button.setStyle("-fx-background-color: " + color + "; -fx-min-width: 60px; -fx-min-height: 60px; -fx-background-radius: 30px;");
    }
}
