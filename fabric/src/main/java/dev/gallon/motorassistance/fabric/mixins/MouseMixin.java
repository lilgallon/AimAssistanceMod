package dev.gallon.motorassistance.fabric.mixins;

import dev.gallon.motorassistance.fabric.events.LeftMouseClickEvent;
import dev.gallon.motorassistance.fabric.events.MouseMoveEvent;
import dev.gallon.motorassistance.fabric.events.SingleEventBus;
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
        SingleEventBus.INSTANCE.send(new MouseMoveEvent(x, y));
    }

    @Inject(method = "onPress(JIII)V", at = @At("TAIL"))
    private void mouseClicked(long window, int button, int action, int mods, CallbackInfo info) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
            SingleEventBus.INSTANCE.send(LeftMouseClickEvent.INSTANCE);
        }
    }
}
