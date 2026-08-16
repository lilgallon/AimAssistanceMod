package dev.gallon.motorassistance.common.mixins;

import dev.gallon.motorassistance.common.event.PlayerTurnEvent;
import dev.gallon.motorassistance.common.event.SingleEventBus;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "turn(DD)V", at = @At("HEAD"), remap = false)
    private void onTurn(double d, double e, CallbackInfo ci) {
        if ((Entity) (Object) this == Minecraft.getInstance().player) {
            SingleEventBus.publish(new PlayerTurnEvent(d, e));
        }
    }
}
