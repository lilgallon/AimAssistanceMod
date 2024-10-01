package dev.gallon.motorassistance.fabric.mixins;

import dev.gallon.motorassistance.fabric.events.RenderEvent;
import dev.gallon.motorassistance.fabric.events.SingleEventBus;
import dev.gallon.motorassistance.fabric.events.TickEvent;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {

    @Inject(method = "runTick(Z)V", at = @At("HEAD"))
    private void renderEvent(boolean tick, CallbackInfo ci) {
        SingleEventBus.INSTANCE.send(RenderEvent.INSTANCE);
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void tickEvent(CallbackInfo info) {
        SingleEventBus.INSTANCE.send(TickEvent.INSTANCE);
    }
}
