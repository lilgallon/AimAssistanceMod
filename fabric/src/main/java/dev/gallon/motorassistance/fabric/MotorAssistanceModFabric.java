package dev.gallon.motorassistance.fabric;

import dev.gallon.motorassistance.common.MotorAssistance;
import dev.gallon.motorassistance.common.domain.MotorAssistanceConfig;
import dev.gallon.motorassistance.fabric.config.TheModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.AutoConfigClient;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.world.InteractionResult;

import java.lang.reflect.Modifier;
import java.util.List;

public class MotorAssistanceModFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigHolder<TheModConfig> configHolder = AutoConfig.register(
                TheModConfig.class,
                JanksonConfigSerializer::new
        );
        // AutoConfig also enumerates static defaults and bounds in nested objects.
        // These constants are not editable configuration options.
        AutoConfigClient.getGuiRegistry(TheModConfig.class).registerPredicateProvider(
                (key, field, config, defaults, registry) -> List.of(),
                field -> field.getDeclaringClass() == MotorAssistanceConfig.class
                        && Modifier.isStatic(field.getModifiers())
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
