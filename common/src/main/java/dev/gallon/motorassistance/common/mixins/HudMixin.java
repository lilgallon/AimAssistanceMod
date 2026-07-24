package dev.gallon.motorassistance.common.mixins;

import dev.gallon.motorassistance.common.MotorAssistance;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class HudMixin {
    @Shadow
    public abstract boolean isHidden();

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void extractAssistanceIndicator(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker,
            CallbackInfo callbackInfo
    ) {
        if (!isHidden()) {
            MotorAssistance.extractIndicatorRenderState(graphics);
        }
    }
}
