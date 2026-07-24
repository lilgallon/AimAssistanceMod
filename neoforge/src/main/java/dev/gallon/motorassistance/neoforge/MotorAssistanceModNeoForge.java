package dev.gallon.motorassistance.neoforge;

import dev.gallon.motorassistance.common.MotorAssistance;
import dev.gallon.motorassistance.common.domain.ModMetadata;
import dev.gallon.motorassistance.neoforge.config.TheModConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = ModMetadata.MOD_ID, dist = Dist.CLIENT)
public final class MotorAssistanceModNeoForge {
    public MotorAssistanceModNeoForge(ModContainer container) {
        MotorAssistance.start(TheModConfig.config);
        container.registerConfig(ModConfig.Type.CLIENT, TheModConfig.CLIENT_SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
