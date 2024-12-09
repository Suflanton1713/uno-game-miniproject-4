package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.observers.listeners.ShiftEventListener;
import org.example.eiscuno.model.table.Table;

public class ThreadPlayMachine extends Thread implements ShiftEventListener {
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;
    private GameUno gameUno;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, GameUno gameUno) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.gameUno = gameUno;
    }

    @Override
    public void onTurnUpdate(String eventType) {
        System.out.println("Machine is on turn");
        machinePlayer.setOnTurn(true);
    }

    @Override
    public void offTurnUpdate(String eventType) {
        System.out.println("Machine is not on turn");
        machinePlayer.setOnTurn(false);
    }

    public void update(String eventType){
        System.out.println("Machine has played, continues player");
    };

    public void run() {
        while (true){
            if(hasPlayerPlayed){
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Aqui iria la logica de colocar la carta
                putCardOnTheTable();
                hasPlayerPlayed = false;
            }
        }
    }

    private void putCardOnTheTable(){
        int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        Card card = machinePlayer.getCard(index);
        gameUno.playCard(card);
        tableImageView.setImage(card.getImage());
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    public Player getMachinePlayer() {
        return machinePlayer;
    }
}