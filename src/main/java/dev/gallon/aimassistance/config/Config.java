package dev.gallon.aimassistance.config;

import dev.gallon.aimassistance.AimAssistanceMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = AimAssistanceMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    private static boolean stopWhenReached;
    private static double aimForceMobs;
    private static double aimForceBlocks;
    private static boolean aimMobs;
    private static boolean aimBlocks;

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == Config.CLIENT_SPEC) {
            Config.bakeConfig();
        }
    }

    public static void bakeConfig() {
        stopWhenReached = CLIENT.stopWhenReached.get();
        aimForceMobs = CLIENT.aimForceMobs.get();
        aimForceBlocks = CLIENT.aimForceBlocks.get();
        aimMobs = CLIENT.aimMobs.get();
        aimBlocks = CLIENT.aimBlocks.get();
    }

    public static boolean isStopWhenReached() {
        return stopWhenReached;
    }

    public static double getAimForceMobs() {
        return aimForceMobs;
    }

    public static double getAimForceBlocks() {
        return aimForceBlocks;
    }

    public static boolean isAimMobs() {
        return aimMobs;
    }

    public static boolean isAimBlocks() {
        return aimBlocks;
    }
}
