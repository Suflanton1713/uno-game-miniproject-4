package org.example.eiscuno.model.shiftobserver;

import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.shiftobserver.listeners.ShiftEventListener;

import java.io.File;
import java.util.*;

public class ShiftEventManager {

    Map<String, List<ShiftEventListener>> listeners = new HashMap<>();

    public ShiftEventManager(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    public void belongToShiftEvent(String eventType, ShiftEventListener listener) {
        List<ShiftEventListener> users = listeners.get(eventType);
        users.add(listener);
    }

    public void leaveShiftEvent(String eventType, ShiftEventListener listener) {
        List<ShiftEventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    public void notifyShiftEvent(String eventType) {
        ShiftEventListener actualListenerOnTurn;
        List<ShiftEventListener> users = listeners.get(eventType);
        for (ShiftEventListener listener : users) {
            listener.update(eventType);
            actualListenerOnTurn = listener;

            if(Objects.equals(eventType, "onturn")){
                changeShiftListener(actualListenerOnTurn);
            }
        }





    }

    public void changeShiftListener(ShiftEventListener listener) {
        ShiftEventListener listenerOffTurn = listeners.get("offturn").get(0);

        leaveShiftEvent("onturn", listener);
        belongToShiftEvent("offturn", listener);

        leaveShiftEvent("offturn", listenerOffTurn);
        belongToShiftEvent("onturn", listenerOffTurn);

        System.out.println(listeners);

    }

    public Map<String, List<ShiftEventListener>> getListeners() {
        return listeners;
    }
}
