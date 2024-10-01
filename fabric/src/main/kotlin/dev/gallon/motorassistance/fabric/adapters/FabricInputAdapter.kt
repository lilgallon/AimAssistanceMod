package dev.gallon.motorassistance.fabric.adapters

import com.mrcrayfish.controllable.Controllable
import dev.gallon.motorassistance.common.domain.CONTROLLABLE_MOD_ID
import dev.gallon.motorassistance.common.interfaces.Input
import dev.gallon.motorassistance.fabric.events.LeftMouseClickEvent
import dev.gallon.motorassistance.fabric.events.MouseMoveEvent
import dev.gallon.motorassistance.fabric.events.SingleEventBus
import dev.gallon.motorassistance.fabric.events.TickEvent
import dev.gallon.motorassistance.fabric.utils.whenModLoaded

class FabricInputAdapter : Input {

    private var moved = false
    private var leftClicked = false
    private var prevX = -1.0
    private var prevY = -1.0
    private var lastTriggerValue = 0.0

    init {
        SingleEventBus.register<MouseMoveEvent> { moved = true }
        SingleEventBus.register<LeftMouseClickEvent> { leftClicked = true }
        SingleEventBus.register<TickEvent> {
            // Check controller move input
            val currX: Double = if (isControllerUsed()) {
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
                moved = prevX != currX || prevY != currY
            }

            // Check controller trigger click
            if (isControllerUsed() && !leftClicked) {
                leftClicked = lastTriggerValue == 0.0 &&
                    Controllable.getController()?.run { rTriggerValue > 0.0F } ?: false
                lastTriggerValue = Controllable.getController()?.rTriggerValue?.toDouble() ?: 0.0
            }
        }
    }

    override fun wasAttackClicked(): Boolean = leftClicked.also { leftClicked = false }

    override fun wasMoved(): Boolean = moved.also { moved = false }

    override fun isControllerUsed(): Boolean = whenModLoaded(CONTROLLABLE_MOD_ID) {
        Controllable.getInput().isControllerInUse
    } ?: false
}
