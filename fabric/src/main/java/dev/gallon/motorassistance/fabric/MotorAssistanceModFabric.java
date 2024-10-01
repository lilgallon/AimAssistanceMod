package dev.gallon.motorassistance.fabric;

import dev.gallon.motorassistance.common.MotorAssistance;
import dev.gallon.motorassistance.fabric.config.TheModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class MotorAssistanceModFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoConfig.register(TheModConfig.class, JanksonConfigSerializer::new);
        TheModConfig config = AutoConfig.getConfigHolder(TheModConfig.class).getConfig();

        MotorAssistance.start(config.modConfig);
    }
}
