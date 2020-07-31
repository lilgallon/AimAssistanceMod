package dev.nero.aimassistance.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {

    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    private static double aimForce;
    private static boolean aimMobs;
    private static boolean aimBlocks;

    public static void bakeConfig() {
        aimForce = CLIENT.aimForce.get();
        aimMobs = CLIENT.aimMobs.get();
        aimBlocks = CLIENT.aimBlocks.get();
    }

    public static double getAimForce() {
        return aimForce;
    }

    public static boolean isAimMobs() {
        return aimMobs;
    }

    public static boolean isAimBlocks() {
        return aimBlocks;
    }
}
