package org.example.eiscuno.controller;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.observers.listeners.ShiftEventListener;
import org.example.eiscuno.model.observers.listeners.ShiftEventListenerAdaptor;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


    private ThreadSingUNOMachine threadSingUNOMachine;
    private ThreadPlayMachine threadPlayMachine;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initVariables();
        this.gameUno.startGame();

        Card initialCard = deck.takeCard();
        table.addCardOnTheTable(initialCard);
        if (Objects.equals(table.getCurrentCardOnTheTable().getColor(), "MULTICOLOR")) {
            int randomNum = (int) (Math.random() * 4) ;
            if (randomNum == 0) {table.getCurrentCardOnTheTable().setColor("RED");
                System.out.println("El color es Rojo");}
            else if (randomNum == 1) {table.getCurrentCardOnTheTable().setColor("BLUE");
                System.out.println("El color es Azul");}
            else if (randomNum == 2) {table.getCurrentCardOnTheTable().setColor("YELLOW");
                System.out.println("El color es Amarillo");}
            else if (randomNum == 3) {table.getCurrentCardOnTheTable().setColor("GREEN");
                System.out.println("El color es verde");}
        }
        tableImageView.setImage(initialCard.getImage());
        printCardsHumanPlayer();
        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer(), this.deck, this, this.gameUno, this.machinePlayer.getCardsPlayer());
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
    public void printCardsHumanPlayer() {
        if (isAnimating) {
            return; // No actualizar mientras se realiza una animación
        }

        Platform.runLater(() -> {
            // Limpia el GridPane para evitar duplicados
            gridPaneCardsPlayer.getChildren().clear();

            // Obtiene las cartas actuales visibles del jugador
            Card[] currentVisibleCardsHumanPlayer = gameUno.getCurrentVisibleCardsHumanPlayer(posInitCardToShow);

            for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
                Card card = currentVisibleCardsHumanPlayer[i];
                ImageView cardImageView = card.getCard();

                // Configura el evento de clic para las cartas
                cardImageView.setOnMouseClicked((MouseEvent event) -> {
                    table.verifyCardTypeOnTable(card);
                    if (gameUno.canThrowCard()) {
                        animateCardToCenterWithRotation(cardImageView, card);
                        gameUno.playCard(card);
                        tableImageView.setImage(card.getImage());
                        humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                        printCardsHumanPlayer();

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

        // Si es el turno de la máquina, inicia su jugada
        if (threadPlayMachine.getMachinePlayer().isOnTurn()) {
            System.out.println("La máquina ya está jugando");
            threadPlayMachine.setHasPlayerPlayed(true);
        }
    }

    /**
     * Handles the action of saying "Uno".
     *
     */
    @FXML
    void onHandleUno() {
        if(humanPlayer.getCardsPlayer().size()==1){gameUno.setSingUno(true);}
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

    private void createButtonsForChangeColor(){

        // Crear los botones
        Button btnAzul = new Button("Azul");
        Button btnRojo = new Button("Rojo");
        Button btnAmarillo = new Button("Amarillo");
        Button btnVerde = new Button("Verde");

        // Posicionar los botones
        btnAzul.setLayoutX(20);
        btnAzul.setLayoutY(20);

        btnRojo.setLayoutX(100);
        btnRojo.setLayoutY(20);

        btnAmarillo.setLayoutX(180);
        btnAmarillo.setLayoutY(20);

        btnVerde.setLayoutX(260);
        btnVerde.setLayoutY(20);

        // Asociar acciones a los botones
        btnAzul.setOnAction(e -> handleChangerColorButtonClick("BLUE"));
        btnRojo.setOnAction(e -> handleChangerColorButtonClick("RED"));
        btnAmarillo.setOnAction(e -> handleChangerColorButtonClick("YELLOW"));
        btnVerde.setOnAction(e -> handleChangerColorButtonClick("GREEN"));

        // Agregar los botones al pane
        centralPane.getChildren().addAll(btnAzul, btnRojo, btnAmarillo, btnVerde);


    }

    private void handleChangerColorButtonClick(String color) {
        System.out.println("Se presionó el botón: " + color);
        table.getCurrentCardOnTheTable().setColor(color);
        System.out.println(table.getCurrentCardOnTheTable().getColor());

        List<Node> botonesAEliminar = new ArrayList<>();

        for (var nodo : centralPane.getChildren()) {
            if (nodo instanceof Button) {
                botonesAEliminar.add((Button) nodo);
            }
        }
        centralPane.getChildren().removeAll(botonesAEliminar);
        gameUno.setHasToChangeColor(false);
        gameUno.events.notifyShiftEvent("onturn");
        gameUno.events.notifyShiftToController("turnChangerController");
        if(threadPlayMachine.getMachinePlayer().isOnTurn()){
            System.out.println("La maquina ya está jugando");

            threadPlayMachine.setHasPlayerPlayed(true);
        }
    }
    public void updateWinStatus(){

        Platform.runLater(() -> {System.out.println("Entro al update winStatus");
                    if(gameUno.getWinStatus()==1){
                        winMessageLabel.setText("Ganaste");
                        gridPaneCardsPlayer.setDisable(true);
                        gridPaneCardsMachine.setDisable(true);
                        deckButton.setDisable(true);
                        oneButton.setDisable(true);

                    }
                    else if(gameUno.getWinStatus()==2){
                        winMessageLabel.setText("Perdiste");
                        gridPaneCardsPlayer.setDisable(true);
                        gridPaneCardsMachine.setDisable(true);
                        deckButton.setDisable(true);
                        oneButton.setDisable(true);

                    }; } );


    }
}
