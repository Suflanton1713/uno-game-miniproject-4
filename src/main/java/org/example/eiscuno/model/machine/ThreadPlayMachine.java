/**
 * Representa un hilo que controla las acciones de una máquina (jugador bot) en el juego Uno.
 * Implementa el patrón observador para escuchar eventos de cambio de turno.
 */
package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.exception.GameException;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.observers.listeners.ShiftEventListener;
import org.example.eiscuno.model.table.Table;

/**
 * Clase que extiende Thread y maneja las acciones de la máquina durante su turno en el juego Uno.
 */
import java.util.List;

public class ThreadPlayMachine extends Thread implements ShiftEventListener {
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;
    private GameUno gameUno;
    private GameUnoController gameUnoController;

    /**
     * Constructor de la clase.
     *
     * @param table             La mesa del juego.
     * @param machinePlayer     El jugador de la máquina.
     * @param tableImageView    La vista de imagen de la mesa.
     * @param gameUno           El juego Uno.
     * @param gameUnoController El controlador del juego Uno.
     */
    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, GameUno gameUno, GameUnoController gameUnoController) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.gameUno = gameUno;
        this.gameUnoController = gameUnoController;
    }

    /**
     * Método invocado cuando la máquina está en turno.
     *
     * @param eventType Tipo de evento de turno.
     */
    @Override
    public void onTurnUpdate(String eventType) {
        System.out.println("Machine is on turn");
        machinePlayer.setOnTurn(true);
    }

    /**
     * Método invocado cuando la máquina está fuera de turno.
     *
     * @param eventType Tipo de evento de turno.
     */
    @Override
    public void offTurnUpdate(String eventType) {
        System.out.println("Machine is not on turn");
        machinePlayer.setOnTurn(false);
    }

    /**
     * Método para actualizar el estado después de que la máquina haya jugado.
     *
     * @param eventType Tipo de evento que ocurrió.
     */
    public void update(String eventType) {
        System.out.println("Machine has played, continues player");
    }

    /**
     * Lógica principal del hilo, que se ejecuta mientras el juego no haya terminado.
     */
    public void run() {
        while (true) {

            if (hasPlayerPlayed && !(gameUno.isGameOver())) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Lógica para colocar la carta
                if (gameUno.isHumanSingUno() && (machinePlayer.getCardsPlayer().size() == 1)) {
                    gameUno.passTurnWhenUnoSung();
                    hasPlayerPlayed = false;
                } else {
                    try {
                        hasPlayerPlayed = putCardOnTheTable();
                    } catch (GameException e) {
                        throw new RuntimeException(e);
                    }
                    if (gameUno.HasToChangeColor()) {
                        table.setColorForTable(chooseColorForMachine());
                        tableImageView.setImage(table.getCurrentCardOnTheTable().getImage());
                        gameUno.setHasToChangeColor(false);
                    }
                }

            }
        }

    }

    /**
     * Método para que la máquina elija el color más frecuente en su mano.
     *
     * @return El color elegido como String.
     */
    private String chooseColorForMachine() {
        int blueCounter = 0, yellowCounter = 0, greenCounter = 0, redCounter = 0;
        String colorChoosen;
        int maxNumber;
        for (Card card : machinePlayer.getCardsPlayer()) {
            switch (card.getColor()) {
                case "BLUE":
                    blueCounter++;
                    break;
                case "YELLOW":
                    yellowCounter++;
                    break;
                case "GREEN":
                    greenCounter++;
                    break;
                case "RED":
                    redCounter++;
                    break;
                default:
                    break;
            }
        }
        maxNumber = blueCounter;
        colorChoosen = "BLUE";

        if (yellowCounter > maxNumber) colorChoosen = "YELLOW";
        if (greenCounter > maxNumber) colorChoosen = "GREEN";
        if (redCounter > maxNumber) colorChoosen = "RED";

        return colorChoosen;

    }

    /**
     * Método para que la máquina coloque una carta en la mesa.
     *
     * @return true si la máquina está en turno, false de lo contrario.
     * @throws GameException Si ocurre un error al jugar la carta.
     */
    private boolean putCardOnTheTable() throws GameException {
        if (!machinePlayer.getCardsPlayer().isEmpty()) {
            System.out.println("Machine is choosing cards");
            int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
            Card card = machinePlayer.getCard(index);
            card = chooseAllowedCard(card);
            if (!(card == null)) {
                gameUno.playCard(card);
                tableImageView.setImage(card.getImage());
                machinePlayer.removeCard(machinePlayer.getCardsPlayer().indexOf(card));
                gameUnoController.animateMachineCardToCenter(card);
            }
            System.out.println("Machine cards after playing " + machinePlayer.getStringOfOwnCards());

            if (machinePlayer.getCardsPlayer().isEmpty()) {
                System.out.println("Gano la maquina");
                gameUno.setWinStatus(2);
                gameUnoController.updateWinStatus();
            }

            return machinePlayer.isOnTurn();
        }
        return true;
    }

    /**
     * Método para seleccionar una carta válida que la máquina pueda jugar.
     *
     * @param card La carta inicial seleccionada.
     * @return La carta que puede ser jugada, o null si no hay ninguna válida.
     * @throws GameException Si ocurre un error al validar la carta.
     */
    private Card chooseAllowedCard(Card card) throws GameException {
        boolean isChoosenCardAllowed = false;
        boolean haveDrawedACard = false;
        int indexOfCardChoosen = machinePlayer.getCardsPlayer().indexOf(card);
        int searchingInDeckCounter = 0;
        int initialOwnCards = machinePlayer.getCardsPlayer().size();
        Card returnCard = card;

        do {
            table.verifyCardTypeOnTable(returnCard);

            if (searchingInDeckCounter == initialOwnCards) {
                System.out.println("Machine draws a card");
                machinePlayer.drawsCard(gameUno.getDeck(), 1);
                returnCard = machinePlayer.getCard(machinePlayer.getCardsPlayer().size() - 1);
                table.verifyCardTypeOnTable(returnCard);
                haveDrawedACard = true;
            }

            if (gameUno.canThrowCard()) {
                isChoosenCardAllowed = true;
            } else {
                if (searchingInDeckCounter < initialOwnCards) {
                    indexOfCardChoosen = (indexOfCardChoosen + 1) % initialOwnCards;
                    returnCard = machinePlayer.getCardsPlayer().get(indexOfCardChoosen);
                    searchingInDeckCounter++;
                }
            }

            if (haveDrawedACard && !isChoosenCardAllowed) {
                gameUno.events.notifyShiftEvent("onturn");
                gameUno.events.notifyShiftToController("turnChangerController");
                returnCard = null;
                isChoosenCardAllowed = true;
            }

        } while (!isChoosenCardAllowed);

        return returnCard;
    }

    /**
     * Establece si el jugador humano ya jugó su turno.
     *
     * @param hasPlayerPlayed true si el jugador ha jugado, false de lo contrario.
     */
    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    /**
     * Obtiene el jugador de la máquina.
     *
     * @return El jugador de la máquina.
     */
    public Player getMachinePlayer() {
        return machinePlayer;
    }
}
