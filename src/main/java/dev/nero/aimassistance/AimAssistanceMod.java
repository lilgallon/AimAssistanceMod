package dev.nero.aimassistance;

import dev.nero.aimassistance.config.Config;
import dev.nero.aimassistance.core.AimAssistance;
import dev.nero.aimassistance.utils.MouseUtils;
import dev.nero.aimassistance.utils.Wrapper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AimAssistanceMod.MOD_ID)
public class AimAssistanceMod
{
    // Ready if needed
    // private static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "aimassistancemod";
    private AimAssistance aimAssistance;

    public AimAssistanceMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register config
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
    }

    private void setup(final FMLCommonSetupEvent event) {
        aimAssistance = new AimAssistance();
        Config.bakeConfig(); // init config values
    }

    @SubscribeEvent
    public void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == Config.CLIENT_SPEC) {
            Config.bakeConfig(); // update the values
        }
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
            MouseUtils.checkForMouseMove();
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
