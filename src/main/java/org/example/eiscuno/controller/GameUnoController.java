package org.example.eiscuno.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
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




    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;

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
        tableImageView.setImage(initialCard.getImage());
        printCardsHumanPlayer();

        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer());
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this.gameUno);
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
     * Prints the human player's cards on the grid pane.
     */
    private void printCardsHumanPlayer() {
        Platform.runLater(() -> {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            cardImageView.setOnMouseClicked((MouseEvent event) -> {


                table.verifyCardTypeOnTable(card);
                System.out.println(gameUno.canThrowCard());
                if(gameUno.canThrowCard()){
                    // Aqui deberian verificar si pueden en la tabla jugar esa carta
                    System.out.println(card.getValue());
                    System.out.println(card.getColor());
                    gameUno.playCard(card);
                    tableImageView.setImage(card.getImage());
                    humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                    printCardsHumanPlayer();

                    if(gameUno.HasToChangeColor()){
                        createButtonsForChangeColor();
                    }else{

                        if(threadPlayMachine.getMachinePlayer().isOnTurn()){
                            System.out.println("La maquina ya está jugando");

                            threadPlayMachine.setHasPlayerPlayed(true);
                        }
                    }

                }




            });

            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
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
        if(machinePlayer.isOnTurn()){
            machinePlayer.drawsCard(deck,1);
        }else{
            humanPlayer.drawsCard(deck,1);
        }

        printCardsHumanPlayer();

        gameUno.events.notifyShiftEvent("onturn");
        gameUno.events.notifyShiftToController("turnChangerController");

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

    }

    @Override
    public void onTurnUpdate(String eventType) {

    }

    @Override
    public void offTurnUpdate(String eventType) {

    }

    @Override
    public void update(String eventType) {
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
        deckButton.setDisable(true);
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
        table.setColorForTable(color);
        System.out.println(table.getCurrentCardOnTheTable().getColor());

        List<Node> botonesAEliminar = new ArrayList<>();

        for (var nodo : centralPane.getChildren()) {
            if (nodo instanceof Button) {
                botonesAEliminar.add((Button) nodo);
            }
        }
        centralPane.getChildren().removeAll(botonesAEliminar);
        gameUno.setHasToChangeColor(false);
        System.out.println("Es wild draw");
        System.out.println(!(table.getCurrentCardOnTheTable().getValue().equals("Wild")));
        System.out.println("Solo wild");
        System.out.println((table.getCurrentCardOnTheTable().getValue().equals("Wild")));
        System.out.println("");

        if(machinePlayer.isOnTurn()){
            deckButton.setDisable(true);
            System.out.println("La maquina ya está jugando");
            threadPlayMachine.setHasPlayerPlayed(true);
        }else{
            deckButton.setDisable(false);
        }

    }
}
