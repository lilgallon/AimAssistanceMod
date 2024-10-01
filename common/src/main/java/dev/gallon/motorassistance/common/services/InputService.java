package dev.gallon.motorassistance.common.services;

import dev.gallon.motorassistance.common.event.LeftMouseClickEvent;
import dev.gallon.motorassistance.common.event.MouseMoveEvent;
import dev.gallon.motorassistance.common.event.SingleEventBus;

public class InputService {
    private boolean moved = false;
    private boolean leftClicked = false;
    private double prevX = -1.0;
    private double prevY = -1.0;
    private double lastTriggerValue = 0.0;

    public InputService() {
        SingleEventBus.listen(MouseMoveEvent.class, (e) -> moved = true);
        SingleEventBus.listen(LeftMouseClickEvent.class, (e) -> leftClicked = true);
        /* TODO Check controller move input
        SingleEventBus.listen(TickEvent.class, (e) -> {
            double currX = if (isControllerUsed()) {
                Controllable.getController()?.rThumbStickXValue?.toDouble() ?: 0.0
            } else {
                0.0
            }

            val currY: Double = if (isControllerUsed()) {
                Controllable.getController()?.rThumbStickYValue?.toDouble() ?: 0.0
            } else {
                0.0
            }

            if (prevX != -1.0 && prevY != -1.0 && !moved) {
                moved = prevX != currX || prevY != currY;
            }

            // Check controller trigger click
            if (isControllerUsed() && !leftClicked) {
                leftClicked = lastTriggerValue == 0.0 &&
                        Controllable.getController()?.run { rTriggerValue > 0.0F } ?: false
                lastTriggerValue = Controllable.getController()?.rTriggerValue?.toDouble() ?: 0.0
            }
        }*/
    }

    boolean wasAttackClicked() {
        if (leftClicked) {
            leftClicked = false;
            return true;
        } else {
            return false;
        }
    }

    boolean wasMoved() {
        if (moved) {
            moved = false;
            return true;
        } else {
            return false;
        }
    }

    /* TODO
    boolean isControllerUsed() {
        return whenModLoaded(CONTROLLABLE_MOD_ID) {
            Controllable.getInput().isControllerInUse
        } ?: false
    }*/
}
