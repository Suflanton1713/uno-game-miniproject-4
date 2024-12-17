package org.example.eiscuno.controller;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.exception.GameException;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.game.ThreadUnoComprobation;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.observers.listeners.ShiftEventListener;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.GameUnoStage;
import org.example.eiscuno.view.WelcomeStage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Controller class for the Uno game.
 */
public class GameUnoController implements ShiftEventListener {



    @FXML
    private GridPane gridPaneCardsMachine;

    @FXML
    private GridPane gridPaneCardsPlayer;

    @FXML
    private ImageView tableImageView;

    @FXML
    private Button deckButton;

    @FXML
    private Pane centralPane;
    @FXML
    private Label winMessageLabel;
    @FXML
    private Button oneButton;





    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;
    private boolean isAnimating = false;
    @FXML
    private Pane PaneCentral;


    private ThreadSingUNOMachine threadSingUNOMachine;
    private ThreadPlayMachine threadPlayMachine;
    private ThreadUnoComprobation threadUnoComprobation;

    @FXML
    private ImageView personaje;  // ImageView para el personaje
    @FXML
    private ImageView character;  // ImageView para el character

    // Arreglos de rutas de las imágenes para cada uno
    private String[] personajeImages = {
            "/org/example/eiscuno/images/personaje1.png",
            "/org/example/eiscuno/images/personaje2.png",
            "/org/example/eiscuno/images/personaje3.png",
            "/org/example/eiscuno/images/personaje4.png"
    };

    private String[] characterImages = {
            "/org/example/eiscuno/images/character1.png",
            "/org/example/eiscuno/images/character2.png",
            "/org/example/eiscuno/images/character3.png",
            "/org/example/eiscuno/images/character4.png"
    };

    /**
     * Método que se ejecuta para mostrar imágenes aleatorias
     */
    public void displayRandomImages() {
        Random rand = new Random();

        // Seleccionar una imagen aleatoria para 'personaje'
        int randomPersonajeIndex = rand.nextInt(personajeImages.length);
        Image personajeImage = new Image(getClass().getResourceAsStream(personajeImages[randomPersonajeIndex]));
        personaje.setImage(personajeImage);

        // Seleccionar una imagen aleatoria para 'character'
        int randomCharacterIndex = rand.nextInt(characterImages.length);
        Image characterImage = new Image(getClass().getResourceAsStream(characterImages[randomCharacterIndex]));
        character.setImage(characterImage);
    }

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        displayRandomImages();
        initVariables();
        this.gameUno.startGame();

        Card initialCard = initialCard();

        table.addCardOnTheTable(initialCard);

        tableImageView.setImage(initialCard.getImage());

        printCardsHumanPlayer();

        threadUnoComprobation = new ThreadUnoComprobation(this.humanPlayer.getCardsPlayer(), this.deck, this, this.gameUno, this.machinePlayer.getCardsPlayer(), this.humanPlayer, this.machinePlayer);
        Thread t1 = new Thread(threadUnoComprobation, "ThreadUnoComprobation");
        t1.start();

        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer(), this.deck, this, this.gameUno, this.machinePlayer.getCardsPlayer(), this.humanPlayer, this.machinePlayer);
        updateMachineDeckDisplay();

        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this.gameUno, this);
        threadPlayMachine.start();
        gameUno.setMachineThread(threadPlayMachine);
        gameUno.setListenersForShiftEvents(this);
        table.setListenerForCardEvent(gameUno);
        System.out.println(threadPlayMachine);

    }

    public Card initialCard (){

        try{
            Card initialCard = deck.takeCard();
            if(initialCard == null){
                throw new NullPointerException("There is no card to start the uno game.");
            }


            if (Objects.equals(initialCard.getColor(), "MULTICOLOR")) {
                int randomNum = (int) (Math.random() * 4) ;
                if (randomNum == 0) {initialCard.setColor("RED");
                    System.out.println("El color es Rojo");}
                else if (randomNum == 1) {initialCard.setColor("BLUE");
                    System.out.println("El color es Azul");}
                else if (randomNum == 2) {initialCard.setColor("YELLOW");
                    System.out.println("El color es Amarillo");}
                else if (randomNum == 3) {initialCard.setColor("GREEN");
                    System.out.println("El color es verde");}
            }

            return initialCard;


        } catch (NullPointerException e) {
            throw new RuntimeException("The deck was wrongly initialized", e);
            //System.out.println(e.getMessage());
            //Card card =  new Card("/org/example/eiscuno/cards-uno/9_green.png", "9", "GREEN");
            //return card;
        }
    }

    /**
     * Initializes the variables for the game.
     */
    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table);
        this.posInitCardToShow = 0;
    }

    /**
     * Prints the machine player's cards on the grid pane.
     */
    /**
     * Updates the machine's card display, showing one generic card for each card in its hand.
     */
    public void updateMachineDeckDisplay() {
        Platform.runLater(() -> {
            // Limpia el GridPane para evitar duplicados
            this.gridPaneCardsMachine.getChildren().clear();

            // Obtiene la cantidad de cartas que tiene la máquina
            int machineCardCount = this.machinePlayer.getCardsPlayer().size();

            for (int i = 0; i < machineCardCount; i++) {
                // Crea un ImageView con el dorso de la carta
                ImageView cardBackImageView = new ImageView(new Image(getClass().getResourceAsStream(("/org/example/eiscuno/cards-uno/card_uno.png"))));
                cardBackImageView.setFitHeight(90);
                cardBackImageView.setFitWidth(70);

                // Añade el ImageView al GridPane
                this.gridPaneCardsMachine.add(cardBackImageView, i, 0);
            }
        });
    }


    /**
     * Prints the human player's cards on the grid pane.
     */

    public void printCardsHumanPlayer() throws RuntimeException {
        Platform.runLater(() -> {
            // Limpia el GridPane para evitar duplicados
            gridPaneCardsPlayer.getChildren().clear();

            // Obtiene las cartas actuales visibles del jugador
            Card[] currentVisibleCardsHumanPlayer = gameUno.getCurrentVisibleCardsHumanPlayer(posInitCardToShow);

            for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
                Card card = currentVisibleCardsHumanPlayer[i];
                ImageView cardImageView = card.getCard();

                cardImageView.setOnMouseClicked((MouseEvent event) -> {
                    try {
                        table.verifyCardTypeOnTable(card);
                    } catch (GameException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(gameUno.canThrowCard());
                    if (gameUno.canThrowCard()) {
                        System.out.println("Tiro carta");
                        animateCardToCenterWithRotation(cardImageView, card);

                        // Aqui deberian verificar si pueden en la tabla jugar esa carta
                        System.out.println(card.getValue());
                        System.out.println(card.getColor());
                        gameUno.playCard(card);
                        tableImageView.setImage(card.getImage());
                        humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                        printCardsHumanPlayer();
                        // Configura el evento de clic para las cartas

                        if (humanPlayer.getCardsPlayer().isEmpty()) {
                            gameUno.setWinStatus(1);
                            updateWinStatus();
                        }

                        if (gameUno.HasToChangeColor()) {
                            createButtonsForChangeColor();
                        } else if (threadPlayMachine.getMachinePlayer().isOnTurn()) {
                            threadPlayMachine.setHasPlayerPlayed(true);
                        }
                    }
                });

                // Añade la carta al GridPane
                gridPaneCardsPlayer.add(cardImageView, i, 0);
            }
        });
    }


    /**
     * Finds the position of a specific card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Handles the "Back" button action to show the previous set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the "Next" button action to show the next set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {
        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            System.out.println("El tamaño de la baraja es :"+humanPlayer.getCardsPlayer().size());
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the action of taking a card.
     *
     * @param event the action event
     */
    @FXML
    void onHandleTakeCard(ActionEvent event) {
        if (machinePlayer.isOnTurn()) {
            Card newCard = deck.takeCard();
            ImageView cardImageView = newCard.getCard();

            animateCardToGridPane(cardImageView, newCard, gridPaneCardsMachine, machinePlayer);

            machinePlayer.drawsCard(deck, 1);
        } else {
            Card newCard = deck.takeCard();
            ImageView cardImageView = newCard.getCard();

            // Animar la carta hacia el jugador
            animateCardToGridPane(cardImageView, newCard, gridPaneCardsPlayer, humanPlayer);
        }

        printCardsHumanPlayer();

        gameUno.events.notifyShiftEvent("onturn");
        gameUno.events.notifyShiftToController("turnChangerController");
        gameUno.setHumanSingUno(false);


        if(threadPlayMachine.getMachinePlayer().isOnTurn()){
            System.out.println("La maquina ya está jugando");

            threadPlayMachine.setHasPlayerPlayed(true);
        }
    }

    /**
     * Handles the action of saying "Uno".
     *
     */
    @FXML
    void onHandleUno() {
        if(humanPlayer.getCardsPlayer().size()==1){gameUno.setHumanSingUno(true);}
        else if (machinePlayer.getCardsPlayer().size()==1) {gameUno.setHumanSingUno(true);}
    }
    public void animateMachineCardToCenter(Card card) {
        Platform.runLater(() -> {
            ImageView animatedCard = card.getCard();
            animateCardToCenterWithRotation(animatedCard, card); // Usa tu método de animación
        });
    }


    private void animateCardToGridPane(ImageView cardImageView, Card newCard, GridPane gridPaneTarget, Player targetPlayer) {
        isAnimating = true;

        // Coordenadas iniciales: desde el botón de la baraja
        Bounds deckBounds = deckButton.localToScene(deckButton.getBoundsInLocal());
        double startX = deckBounds.getMinX();
        double startY = deckBounds.getMinY();

        // Clonar el ImageView para animar
        ImageView animatedCard = new ImageView(cardImageView.getImage());
        animatedCard.setFitHeight(90);
        animatedCard.setFitWidth(70);
        animatedCard.setLayoutX(startX);
        animatedCard.setLayoutY(startY);

        // Añadir la carta animada al contenedor principal
        centralPane.getChildren().add(animatedCard);

        // Calcular las coordenadas del destino en el GridPane
        int nextColumn = gridPaneTarget.getChildren().size(); // Nueva columna basada en el número de cartas existentes
        Bounds gridBounds = gridPaneTarget.localToScene(gridPaneTarget.getBoundsInLocal());
        double targetX = gridBounds.getMinX() + (nextColumn * 75); // Espaciado horizontal entre cartas
        double targetY = gridBounds.getMinY();

        // Configurar la animación
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(animatedCard);
        transition.setDuration(javafx.util.Duration.millis(300));
        transition.setToX(targetX - startX);
        transition.setToY(targetY - startY);

        transition.setOnFinished(e -> {
            centralPane.getChildren().remove(animatedCard); // Eliminar la carta animada

            // Añadir la carta a la lista del jugador/máquina
            targetPlayer.addCard(newCard);

            // Crear un nuevo ImageView para el GridPane
            ImageView finalCardImageView = newCard.getCard();
            finalCardImageView.setFitHeight(90);
            finalCardImageView.setFitWidth(70);

            // Añadir la carta a la posición correcta en el GridPane
            Platform.runLater(() -> gridPaneTarget.add(finalCardImageView, nextColumn, 0));

            isAnimating = false;
        });

        // Iniciar la animación
        transition.play();
    }

    private void animateCardToCenterWithRotation(ImageView cardImageView, Card newCard) {
        isAnimating = true;

        // Obtener las coordenadas iniciales de la carta en el mazo del jugador
        Bounds cardBounds = cardImageView.localToScene(cardImageView.getBoundsInLocal());
        double startX = cardBounds.getMinX();
        double startY = cardBounds.getMinY();

        // Crear una copia de la carta para la animación
        ImageView animatedCard = new ImageView(cardImageView.getImage());
        animatedCard.setFitHeight(90);
        animatedCard.setFitWidth(70);
        animatedCard.setLayoutX(startX);
        animatedCard.setLayoutY(startY);

        // Generar una rotación aleatoria entre -20 y 20 grados
        double rotationAngle = (Math.random() * 40) - 20;

        // Añadir la carta animada al contenedor central
        centralPane.getChildren().add(animatedCard);

        // Coordenadas dinámicas del centro del tablero
        Bounds centerBounds = tableImageView.localToScene(tableImageView.getBoundsInLocal());
        double centerX = centerBounds.getMinX() + centerBounds.getWidth() / 2;
        double centerY = centerBounds.getMinY() + centerBounds.getHeight() / 2;

        // Configurar la animación de movimiento
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(animatedCard);
        transition.setDuration(javafx.util.Duration.millis(500)); // Duración de 500 ms
        transition.setToX(centerX - startX - animatedCard.getFitWidth() / 2);
        transition.setToY(centerY - startY - animatedCard.getFitHeight() / 2);

        // Evento al finalizar la animación
        transition.setOnFinished(e -> {
            centralPane.getChildren().remove(animatedCard); // Eliminar la carta animada

            // Crear una nueva carta fija con rotación y colocarla en el centro
            ImageView finalCard = new ImageView(newCard.getImage());
            finalCard.setFitHeight(90);
            finalCard.setFitWidth(70);
            finalCard.setRotate(rotationAngle); // Mantener la rotación
            finalCard.setLayoutX(centerX - finalCard.getFitWidth() / 2);
            finalCard.setLayoutY(centerY - finalCard.getFitHeight() / 2);

            // Eliminar cualquier carta previa en el centro
            centralPane.getChildren().removeIf(node -> node instanceof ImageView && node != tableImageView);

            // Añadir la nueva carta al contenedor
            centralPane.getChildren().add(finalCard);

            // Actualizar el mazo del jugador
            Platform.runLater(() -> printCardsHumanPlayer());

            isAnimating = false;
        });

        // Iniciar la animación
        transition.play();
    }















    @Override
    public void onTurnUpdate(String eventType) {

    }

    @Override
    public void offTurnUpdate(String eventType) {

    }

    @Override
    public void update(String eventType) {
        updateMachineDeckDisplay();
        printCardsHumanPlayer();
        if(machinePlayer.isOnTurn()){
            gridPaneCardsPlayer.setDisable(true);
            gridPaneCardsMachine.setDisable(false);
            deckButton.setDisable(true);
        }else{
            gridPaneCardsPlayer.setDisable(false);
            gridPaneCardsMachine.setDisable(true);
            deckButton.setDisable(false);
        }


    }

    private void createButtonsForChangeColor() {
        // Obtener el Pane con el ID "Colors"
        Pane colorsPane = (Pane) centralPane.lookup("#Colors");

        // Crear el GridPane para organizar los botones
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(5);  // Reducir espaciado horizontal entre botones
        buttonGrid.setVgap(5);  // Reducir espaciado vertical entre botones
        buttonGrid.setLayoutX(50); // Ajustar posición dentro de Colors (si es necesario)
        buttonGrid.setLayoutY(50); // Ajustar posición dentro de Colors (si es necesario)

        // Crear los botones sin texto
        Button btnAzul = new Button();
        Button btnRojo = new Button();
        Button btnAmarillo = new Button();
        Button btnVerde = new Button();

        // Establecer los colores de fondo de los botones
        btnAzul.setStyle("-fx-background-color: BLUE; -fx-min-width: 60px; -fx-min-height: 60px; -fx-background-radius: 30px;");
        btnRojo.setStyle("-fx-background-color: RED; -fx-min-width: 60px; -fx-min-height: 60px; -fx-background-radius: 30px;");
        btnAmarillo.setStyle("-fx-background-color: YELLOW; -fx-min-width: 60px; -fx-min-height: 60px; -fx-background-radius: 30px;");
        btnVerde.setStyle("-fx-background-color: GREEN; -fx-min-width: 60px; -fx-min-height: 60px; -fx-background-radius: 30px;");

        // Añadir los botones al GridPane en posiciones correspondientes
        buttonGrid.add(btnAzul, 0, 0);    // Fila 0, Columna 0
        buttonGrid.add(btnRojo, 1, 0);    // Fila 0, Columna 1
        buttonGrid.add(btnAmarillo, 0, 1); // Fila 1, Columna 0
        buttonGrid.add(btnVerde, 1, 1);    // Fila 1, Columna 1

        // Asociar las acciones a los botones
        btnAzul.setOnAction(e -> handleChangerColorButtonClick("BLUE", colorsPane));
        btnRojo.setOnAction(e -> handleChangerColorButtonClick("RED", colorsPane));
        btnAmarillo.setOnAction(e -> handleChangerColorButtonClick("YELLOW", colorsPane));
        btnVerde.setOnAction(e -> handleChangerColorButtonClick("GREEN", colorsPane));

        // Agregar el GridPane al Pane "Colors"
        colorsPane.getChildren().add(buttonGrid);

        // Asegurar que el GridPane se muestre encima de otros elementos
        buttonGrid.setViewOrder(1);  // Establece un valor de Z-Index mayor para que se muestre encima
    }





    private void handleChangerColorButtonClick(String color, Pane colorsPane) {
        deckButton.setDisable(false);  // Habilitar el botón de la baraja

        System.out.println("Se presionó el botón: " + color);
        table.setColorForTable(color);  // Establecer el color de la mesa
        System.out.println(table.getCurrentCardOnTheTable().getColor());

        // Eliminar los botones del Pane con el ID "Colors" cuando se haga clic en uno
        colorsPane.getChildren().clear();  // Eliminar todos los botones del Pane con el ID "Colors"

        // Restablecer el estado de cambio de color
        gameUno.setHasToChangeColor(false);

        // Si es el turno de la máquina, deshabilitar el botón de la baraja
        if (machinePlayer.isOnTurn()) {
            deckButton.setDisable(true);
            threadPlayMachine.setHasPlayerPlayed(true);
        }
        else{
                deckButton.setDisable(false);
        }
    }



    @FXML
    void handleClickExit(ActionEvent event) throws IOException {
        GameUnoStage.deletedInstance();

        // Abre el WelcomeStage utilizando el Singleton
        WelcomeStage.getInstance().show();
    }




    public void updateWinStatus(){

        Platform.runLater(() -> {System.out.println("Entro al update winStatus");
                    if(gameUno.getWinStatus()==1){
                        winMessageLabel.setText("Ganaste");
                        gridPaneCardsPlayer.setDisable(true);
                        gridPaneCardsMachine.setDisable(true);
                        deckButton.setDisable(true);
                        oneButton.setDisable(true);
                        machinePlayer.setOnTurn(false);
                        humanPlayer.setOnTurn(false);

                    }
                    else if(gameUno.getWinStatus()==2){
                        winMessageLabel.setText("Perdiste");
                        gridPaneCardsPlayer.setDisable(true);
                        gridPaneCardsMachine.setDisable(true);
                        deckButton.setDisable(true);
                        oneButton.setDisable(true);
                        machinePlayer.setOnTurn(false);
                        humanPlayer.setOnTurn(false);

                    }; } );


    }

    public Button getOneButton() {
        return oneButton;
    }

    public void setOneButton(Button oneButton) {
        this.oneButton = oneButton;
    }

    public void setDeck(Deck deck){
        this.deck = deck;
    }

    public Deck getDeck(){
        return deck;
    }
}
