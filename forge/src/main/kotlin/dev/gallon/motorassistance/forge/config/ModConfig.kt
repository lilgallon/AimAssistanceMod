package dev.gallon.motorassistance.forge.config

import dev.gallon.motorassistance.common.domain.MotorAssistanceConfig
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry

@Config(name = "motorassistance")
class ModConfig : ConfigData {
    // documentation: https://shedaniel.gitbook.io/cloth-config/auto-config/creating-a-config-class

    @ConfigEntry.Gui.CollapsibleObject
    val modConfig = MotorAssistanceConfig()
}
