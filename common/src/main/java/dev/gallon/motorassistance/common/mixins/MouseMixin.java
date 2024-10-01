package dev.gallon.motorassistance.common.mixins;

import dev.gallon.motorassistance.common.event.LeftMouseClickEvent;
import dev.gallon.motorassistance.common.event.MouseMoveEvent;
import dev.gallon.motorassistance.common.event.SingleEventBus;
import net.minecraft.client.MouseHandler;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {

    @Inject(method = "onMove(JDD)V", at = @At("TAIL"))
    private void mouseMoved(long window, double x, double y, CallbackInfo ci) {
        SingleEventBus.publish(new MouseMoveEvent(x, y));
    }

    @Inject(method = "onPress(JIII)V", at = @At("TAIL"))
    private void mouseClicked(long window, int button, int action, int mods, CallbackInfo info) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
            SingleEventBus.publish(new LeftMouseClickEvent());
        }
    }
}
