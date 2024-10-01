package dev.gallon.motorassistance.common.services;

import dev.gallon.motorassistance.common.event.AttackEvent;
import dev.gallon.motorassistance.common.event.PlayerTurnEvent;
import dev.gallon.motorassistance.common.event.SingleEventBus;

public class InputService {
    private static boolean moved = false;
    private static boolean attacked = false;

    static {
        SingleEventBus.listen(PlayerTurnEvent.class, (e) -> moved = true);
        SingleEventBus.listen(AttackEvent.class, (e) -> attacked = true);
    }

    static boolean wasAttackClicked() {
        if (attacked) {
            System.out.println("attacked");
            attacked = false;
            return true;
        } else {
            return false;
        }
    }

    static boolean wasMoved() {
        if (moved) {
            moved = false;
            return true;
        } else {
            return false;
        }
    }
}
