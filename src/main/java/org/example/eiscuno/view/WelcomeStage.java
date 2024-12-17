package org.example.eiscuno.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class WelcomeStage extends Stage {

    /**
     * Constructs a new WelcomeStage instance.
     * Loads the FXML layout, applies a stylesheet, and configures the stage.
     *
     * @throws IOException if an error occurs while loading the FXML file or stylesheet.
     */
    public WelcomeStage() throws IOException {
        // Load the FXML file for the welcome view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/welcome-view.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            // Re-throw the caught IOException with additional context
            throw new IOException("Error while loading FXML file", e);
        }

        // Create a scene with the loaded FXML root
        Scene scene = new Scene(root);
        // Load and apply the stylesheet for the welcome view
        scene.getStylesheets().add(getClass().getResource("/org/example/eiscuno/styles/styleWelcome.css").toExternalForm());

        // Configure the stage properties
        setTitle("EISC Uno"); // Set the title of the window
        setScene(scene);      // Attach the scene to the stage
        setResizable(false);  // Disable resizing of the window
        show();               // Display the stage
    }

    /**
     * Deletes the singleton instance of WelcomeStage.
     * This method closes the stage and sets the instance reference to null.
     */
    public static void deleteInstance() {
        WelcomeStage.WelcomeStageHolder.INSTANCE.close();
        WelcomeStage.WelcomeStageHolder.INSTANCE = null;
    }

    /**
     * Retrieves the singleton instance of WelcomeStage.
     * If the instance does not exist, it creates a new one.
     *
     * @return the singleton instance of WelcomeStage.
     * @throws IOException if an error occurs while creating the WelcomeStage instance.
     */
    public static WelcomeStage getInstance() throws IOException {
        return WelcomeStage.WelcomeStageHolder.INSTANCE != null ?
                WelcomeStage.WelcomeStageHolder.INSTANCE :
                (WelcomeStage.WelcomeStageHolder.INSTANCE = new WelcomeStage());
    }

    /**
     * Static nested holder class for the singleton instance of WelcomeStage.
     * This class ensures that the instance is lazily initialized and thread-safe.
     */
    private static class WelcomeStageHolder {
        private static WelcomeStage INSTANCE; // Singleton instance of WelcomeStage
    }
}

