package dev.gallon.motorassistance.fabric

import dev.gallon.motorassistance.common.domain.MotorAssistanceConfig
import dev.gallon.motorassistance.common.services.MotorAssistanceService
import dev.gallon.motorassistance.fabric.adapters.FabricInputAdapter
import dev.gallon.motorassistance.fabric.adapters.FabricMinecraftAdapter
import dev.gallon.motorassistance.fabric.config.ModConfig
import dev.gallon.motorassistance.fabric.events.RenderEvent
import dev.gallon.motorassistance.fabric.events.SingleEventBus
import dev.gallon.motorassistance.fabric.events.TickEvent
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer
import net.fabricmc.api.ModInitializer
import net.minecraft.client.Minecraft

class MotorAssistance : ModInitializer {
    private var motorAssistance: MotorAssistanceService? = null

    override fun onInitialize() {
        AutoConfig.register(ModConfig::class.java, ::JanksonConfigSerializer)
        val config = AutoConfig.getConfigHolder(ModConfig::class.java).config

        SingleEventBus.register<TickEvent> {
            initOrResetMotorAssistance(config.modConfig)
            motorAssistance?.analyseEnvironment()
            motorAssistance?.analyseBehavior()
        }

        SingleEventBus.register<RenderEvent> {
            motorAssistance?.assistIfPossible()
        }
    }

    private fun initOrResetMotorAssistance(config: MotorAssistanceConfig) {
        if (motorAssistance == null && Minecraft.getInstance().player != null) {
            motorAssistance = MotorAssistanceService(
                minecraft = FabricMinecraftAdapter(Minecraft.getInstance()),
                input = FabricInputAdapter(),
                config = config,
            )
        } else if (motorAssistance != null && Minecraft.getInstance().player == null) {
            motorAssistance = null
        }
    }
}
