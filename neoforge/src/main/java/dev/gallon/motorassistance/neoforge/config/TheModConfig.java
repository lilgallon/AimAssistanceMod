package dev.gallon.motorassistance.neoforge.config;

import dev.gallon.motorassistance.common.domain.ModMetadata;
import dev.gallon.motorassistance.common.domain.MotorAssistanceConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = ModMetadata.MOD_ID)
public class TheModConfig implements ConfigData {
    // documentation: https://shedaniel.gitbook.io/cloth-config/auto-config/creating-a-config-class

    @ConfigEntry.Gui.CollapsibleObject
    public MotorAssistanceConfig modConfig = new MotorAssistanceConfig();
}
