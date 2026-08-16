package dev.gallon.motorassistance.forge;

import dev.gallon.motorassistance.common.MotorAssistance;
import dev.gallon.motorassistance.common.domain.ModMetadata;
import dev.gallon.motorassistance.forge.config.MotorAssistanceConfigScreen;
import dev.gallon.motorassistance.forge.config.TheModConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModMetadata.MOD_ID)
public final class MotorAssistanceModForge {
    public MotorAssistanceModForge(FMLJavaModLoadingContext context) {
        TheModConfig.register(context.getModBusGroup());
        context.registerConfig(ModConfig.Type.CLIENT, TheModConfig.CLIENT_SPEC);
        context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(MotorAssistanceConfigScreen::new)
        );
        MotorAssistance.start(TheModConfig.config);
    }
}
