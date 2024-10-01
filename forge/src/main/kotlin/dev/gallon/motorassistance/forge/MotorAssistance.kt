package dev.gallon.motorassistance.forge

import dev.gallon.motorassistance.common.services.MotorAssistanceService
import dev.gallon.motorassistance.forge.adapters.ForgeInputAdapter
import dev.gallon.motorassistance.forge.adapters.ForgeMinecraftAdapter
import dev.gallon.motorassistance.forge.config.ModConfig
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.event.level.LevelEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

@Mod("motorassistance")
object MotorAssistance {
    private var motorAssistance: MotorAssistanceService? = null
    private val forgeMouseAdapter: ForgeInputAdapter = ForgeInputAdapter()

    init {
        AutoConfig.register(ModConfig::class.java, ::JanksonConfigSerializer)
        ModLoadingContext
            .get()
            .registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory::class.java) {
                ConfigScreenHandler.ConfigScreenFactory { _: Minecraft?, parent: Screen? ->
                    AutoConfig.getConfigScreen(
                        ModConfig::class.java,
                        parent,
                    ).get()
                }
            }

        FORGE_BUS.addListener(::onLoggingIn)
        FORGE_BUS.addListener(::onLoggingOut)
        FORGE_BUS.addListener(::onPlayerTick)
        FORGE_BUS.addListener(::onClientTick)
        FORGE_BUS.addListener(::onRender)
        FORGE_BUS.addListener(forgeMouseAdapter::onClientTick)
        FORGE_BUS.addListener(forgeMouseAdapter::onMouseButtonClicked)
    }

    @SubscribeEvent
    fun onLoggingIn(loggingInEvent: LevelEvent.Load) {
        motorAssistance = MotorAssistanceService(
            minecraft = ForgeMinecraftAdapter(Minecraft.getInstance()),
            input = forgeMouseAdapter,
            config = AutoConfig.getConfigHolder(ModConfig::class.java).config.modConfig,
        )
    }

    @SubscribeEvent
    fun onLoggingOut(loggingInEvent: LevelEvent.Unload) {
        motorAssistance = null
    }

    @SubscribeEvent
    fun onPlayerTick(playerTickEvent: net.minecraftforge.event.TickEvent.PlayerTickEvent?) {
        motorAssistance?.analyseBehavior()
    }

    @SubscribeEvent
    fun onClientTick(clientTickEvent: net.minecraftforge.event.TickEvent.ClientTickEvent?) {
        motorAssistance?.analyseEnvironment()
    }

    @SubscribeEvent
    fun onRender(renderTickEvent: net.minecraftforge.event.TickEvent.RenderTickEvent?) {
        motorAssistance?.assistIfPossible()
    }
}
