package dev.gallon.motorassistance.common.mixins;

import dev.gallon.motorassistance.common.event.PlayerTurnEvent;
import dev.gallon.motorassistance.common.event.SingleEventBus;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "turn(DD)V", at = @At("HEAD"))
    private void onTurn(double d, double e, CallbackInfo ci) {
        if ((Entity) (Object) this instanceof Player) {
            SingleEventBus.publish(new PlayerTurnEvent(d, e));
        }
    }
}
