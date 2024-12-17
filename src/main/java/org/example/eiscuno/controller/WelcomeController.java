package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
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
}
