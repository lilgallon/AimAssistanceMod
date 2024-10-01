package dev.gallon.motorassistance.common.mixins;

import dev.gallon.motorassistance.common.event.RenderEvent;
import dev.gallon.motorassistance.common.event.SingleEventBus;
import dev.gallon.motorassistance.common.event.TickEvent;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {

    @Inject(method = "runTick(Z)V", at = @At("HEAD"))
    private void renderEvent(boolean tick, CallbackInfo ci) {
        SingleEventBus.publish(new RenderEvent());
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void tickEvent(CallbackInfo info) {
        SingleEventBus.publish(new TickEvent());
    }
}
