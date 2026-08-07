package dev.gallon.motorassistance.forge.adapters

import com.mrcrayfish.controllable.Controllable
import com.mrcrayfish.controllable.client.binding.ButtonBindings
import dev.gallon.motorassistance.common.domain.ButtonPressTracker
import dev.gallon.motorassistance.common.domain.CONTROLLABLE_MOD_ID
import dev.gallon.motorassistance.common.interfaces.Input
import dev.gallon.motorassistance.forge.utils.whenModLoaded
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.lwjgl.glfw.GLFW

class ForgeInputAdapter : Input {
    private var moved = false
    private var leftClicked = false
    private var prevX = -1.0
    private var prevY = -1.0
    private val controllerAttack = ButtonPressTracker()

    @SubscribeEvent
    fun onClientTick(clientTickEvent: TickEvent.ClientTickEvent) {
        checkForMoveInput()
        val controllerAttackClicked = whenModLoaded(CONTROLLABLE_MOD_ID) {
            controllerAttack.update(ButtonBindings.ATTACK.isButtonDown)
        } ?: false
        leftClicked = leftClicked || controllerAttackClicked
    }

    @SubscribeEvent
    fun onMouseButtonClicked(mouseEvent: InputEvent.MouseButton.Post) {
        if (mouseEvent.button == GLFW.GLFW_MOUSE_BUTTON_LEFT && mouseEvent.action == GLFW.GLFW_PRESS) {
            leftClicked = true
        }
    }

    private fun checkForMoveInput() {
        val currX: Double = if (isControllerUsed()) {
            Controllable.getController()?.rThumbStickXValue?.toDouble() ?: 0.0
        } else {
            Minecraft.getInstance().mouseHandler.xpos()
        }

        val currY: Double = if (isControllerUsed()) {
            Controllable.getController()?.rThumbStickYValue?.toDouble() ?: 0.0
        } else {
            Minecraft.getInstance().mouseHandler.ypos()
        }

        if (prevX != -1.0 && prevY != -1.0 && !moved) {
            moved = prevX != currX || prevY != currY
        }
        prevX = currX
        prevY = currY
    }

    override fun wasAttackClicked(): Boolean = leftClicked.also { leftClicked = false }

    override fun wasMoved(): Boolean = moved.also { moved = false }

    override fun isControllerUsed(): Boolean = whenModLoaded(CONTROLLABLE_MOD_ID) {
        Controllable.getInput().isControllerInUse
    } ?: false
}
