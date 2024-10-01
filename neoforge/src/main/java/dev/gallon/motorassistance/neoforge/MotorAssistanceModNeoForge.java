package dev.gallon.motorassistance.neoforge;

import dev.gallon.motorassistance.common.MotorAssistance;
import dev.gallon.motorassistance.common.domain.ModMetadata;
import dev.gallon.motorassistance.neoforge.config.TheModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jetbrains.annotations.NotNull;

@Mod(ModMetadata.MOD_ID)
public final class MotorAssistanceModNeoForge {
    public MotorAssistanceModNeoForge() {
        AutoConfig.register(TheModConfig.class, JanksonConfigSerializer::new);
        TheModConfig config = AutoConfig.getConfigHolder(TheModConfig.class).getConfig();
        ModLoadingContext
                .get()
                .registerExtensionPoint(IConfigScreenFactory.class, () -> new IConfigScreenFactory() {
                    @Override
                    public @NotNull Screen createScreen(@NotNull ModContainer modContainer, @NotNull Screen parent) {
                        return AutoConfig.getConfigScreen(TheModConfig.class, parent).get();
                    }
                });

        MotorAssistance.start(config.modConfig);
    }
}
