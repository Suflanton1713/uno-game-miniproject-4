package org.example.eiscuno.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/welcome-view.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            // Re-throwing the caught IOException
            throw new IOException("Error while loading FXML file", e);
        }
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/org/example/eiscuno/styles/styleWelcome.css").toExternalForm());
        // Configuring the stage
        setTitle("EISC Uno"); // Sets the title of the stage
        setScene(scene); // Sets the scene for the stage
        setResizable(false); // Disallows resizing of the stage
        show(); // Displays the stage
        getIcons().add(new Image(getClass().getResourceAsStream("/org/example/eiscuno/images/favicon.png")));
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
     * Retrieves the singleton instance ofWelcomeStage.
     *
     * @return the singleton instance ofWelcomeStage.
     * @throws IOException if an error occurs while creating the instance.
     */
    public static WelcomeStage getInstance() throws IOException {
        return WelcomeStage.WelcomeStageHolder.INSTANCE != null ?
               WelcomeStage.WelcomeStageHolder.INSTANCE :
                (WelcomeStage.WelcomeStageHolder.INSTANCE = new WelcomeStage());
    }

    /**
     * Holder class for the singleton instance ofWelcomeStage.
     * This class ensures lazy initialization of the singleton instance.
     */
    private static class WelcomeStageHolder {
        private static WelcomeStage INSTANCE;
    }
}

