package dev.nero.aimassistance;

import dev.nero.aimassistance.module.AimAssistance;
import dev.nero.aimassistance.utils.Wrapper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("aimassistancemod")
public class AimAssistanceMod
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    private AimAssistance aimAssistance;

    public AimAssistanceMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Init");
        aimAssistance = new AimAssistance();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
        if (Wrapper.playerPlaying()) {
            aimAssistance.analyseBehaviour();
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent clientTickEvent) {
        if (Wrapper.playerPlaying()) {
            aimAssistance.analyseEnvironment();
        }
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent renderTickEvent) {
        if (Wrapper.playerPlaying()) {
            aimAssistance.assistIfPossible();
        }
    }
}
