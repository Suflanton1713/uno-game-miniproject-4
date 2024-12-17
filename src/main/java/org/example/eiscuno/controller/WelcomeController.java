package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.eiscuno.view.GameUnoStage;

import java.io.IOException;

public class WelcomeController {

    private MediaPlayer mediaPlayer; // Reproductor de música

    @FXML
    private Button buttonSound; // Botón para detener o reanudar la música

    /**
     * Método que se ejecuta al inicializar el controlador.
     * Inicia la música de fondo.
     */
    @FXML
    public void initialize() {
        playMusic(); // Reproduce la música al iniciar el WelcomeStage
    }

    /**
     * Método para manejar el botón Play y abrir el GameUnoStage.
     *
     * @param event Acción del botón "Play".
     * @throws IOException
     */
    @FXML
    private void handleClickPlay(ActionEvent event) throws IOException {
        // Detener la música antes de cambiar a GameUnoStage
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        // Invoca la instancia singleton de GameUnoStage
        GameUnoStage.getInstance().show();

        // Cierra el WelcomeStage
        Stage welcomeStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        welcomeStage.close();
    }

    /**
     * Reproduce la música de fondo en el WelcomeStage.
     */
    private void playMusic() {
        try {
            // Ruta al archivo de música en la carpeta resources
            String musicFile = getClass().getResource("/org/example/eiscuno/music/welcome-audio.mp3").toExternalForm();
            Media media = new Media(musicFile);
            mediaPlayer = new MediaPlayer(media);

            // Configuración del reproductor
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Repetir la música indefinidamente
            mediaPlayer.setVolume(0.5); // Ajustar el volumen
            mediaPlayer.play(); // Comienza a reproducir la música
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al reproducir la música en WelcomeController.");
        }
    }

    /**
     * Método para detener o reanudar la música cuando se hace clic en el botón.
     */
    @FXML
    private void handleClickSound(ActionEvent event) {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause(); // Si la música está sonando, la pausa
            } else {
                mediaPlayer.play(); // Si la música está pausada, la reanuda
            }
        }
    }

    @FXML
    void handleClickCredits(ActionEvent event) {
        Stage creditsStage = new Stage();

        // Configure the credits stage as a modal window
        creditsStage.initModality(Modality.WINDOW_MODAL);
        creditsStage.initStyle(StageStyle.UNDECORATED);

        // Create title label for credits
        Label titleLabel = new Label("CREDITOS DEL JUEGO");
        titleLabel.setStyle("-fx-text-fill: white;" +  // White text color
                "-fx-font-size: 28px;" +  // Font size
                "-fx-font-weight: bold;" + // Bold font
                "-fx-padding: 20px 0;");    // Padding around the text

        // VBox for credit information
        VBox creditsBox = new VBox(15);
        creditsBox.setAlignment(Pos.CENTER);

        // Create a background for the text box
        VBox innerBox = new VBox(10); // Inner box for text with padding
        innerBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);" +  // Semi-transparent black background
                "-fx-padding: 20px;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;");

        // Add labels for credit information
        Label creditLabel1 = new Label("Desarrollado por: Alejandro Quintero, Daniel Andrade y Juan Rincón");
        Label creditLabel2 = new Label("Códigos: 202342032");
        Label creditLabel3 = new Label("Correo: juan.rincon.lopez@correounivalle.edu.co");
        Label creditLabel4 = new Label("Materia: Programación Orientada a Eventos - 2024-2");

        // Set styles for credit labels
        creditLabel1.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        creditLabel2.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        creditLabel3.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        creditLabel4.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        // Add credit labels to the inner box
        innerBox.getChildren().addAll(creditLabel1, creditLabel2, creditLabel3, creditLabel4);

        // Create close button for the credits stage
        Button closeButton = new Button("Cerrar");
        closeButton.setOnAction(e -> creditsStage.close());
        closeButton.setStyle(
                "-fx-background-color: linear-gradient(#ffcc00, #e6a700), " +  // Bright yellow background
                        "linear-gradient(#ffdd33, #ff9900), " +                     // Orange gradient
                        "radial-gradient(center 50% 50%, radius 100%, #ffd700, #e6a700); " + // Radial lighting
                        "-fx-background-radius: 12;" +           // Rounded borders
                        "-fx-border-radius: 12;" +               // Border radius
                        "-fx-border-width: 3;" +                 // Border thickness
                        "-fx-border-color: #664400;" +           // Dark brown border
                        "-fx-text-fill: white;" +                // White text
                        "-fx-font-weight: bold;" +               // Bold font
                        "-fx-font-size: 16px;" +                 // Font size
                        "-fx-padding: 10 20;"                    // Internal padding
        );

        // Layout with background image
        VBox layout = new VBox(20);
        layout.getChildren().addAll(titleLabel, innerBox, closeButton);
        layout.setStyle("-fx-background-image: url('" + getClass().getResource("/org/example/eiscuno/images/credits.png") + "');" +
                "-fx-background-size: cover;" +
                "-fx-background-position: center;");
        layout.setAlignment(Pos.CENTER);

        // Create scene and set it to the credits stage
        Scene scene = new Scene(layout, 600, 400);
        creditsStage.setScene(scene);

        // Show the credits stage
        creditsStage.showAndWait();
    }


}
