package dev.gallon.motorassistance.fabric;

import dev.gallon.motorassistance.common.MotorAssistance;
import dev.gallon.motorassistance.fabric.config.TheModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.world.InteractionResult;

public class MotorAssistanceModFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigHolder<TheModConfig> configHolder = AutoConfig.register(
                TheModConfig.class,
                JanksonConfigSerializer::new
        );
        configHolder.registerSaveListener((holder, config) -> {
            config.validatePostLoad();
            return InteractionResult.PASS;
        });

        TheModConfig config = configHolder.getConfig();
        config.validatePostLoad();
        configHolder.save();
        MotorAssistance.start(config.modConfig);
    }
}
