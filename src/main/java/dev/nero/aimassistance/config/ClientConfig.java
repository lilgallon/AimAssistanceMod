package dev.nero.aimassistance.config;

import dev.nero.aimassistance.AimAssistanceMod;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public final ForgeConfigSpec.BooleanValue stopWhenReached;
    public final ForgeConfigSpec.DoubleValue aimForceMobs;
    public final ForgeConfigSpec.DoubleValue aimForceBlocks;
    public final ForgeConfigSpec.BooleanValue aimMobs;
    public final ForgeConfigSpec.BooleanValue aimBlocks;

    public ClientConfig(ForgeConfigSpec.Builder builder) {

        builder.push("Aim assistance"); // Category

        stopWhenReached = builder
                .comment("If true, it will stop assisting once you're looking at a mob")
                .translation(AimAssistanceMod.MOD_ID + ".config." + "stopWhenReached")
                .define("stopWhenReached", false);

        aimForceMobs = builder
                .comment("What should be the force of the aim assistance on mobs?")
                .translation(AimAssistanceMod.MOD_ID + ".config." + "aimForceMobs")
                .defineInRange("aimForceMobs", 4.0, 0.1, 10.0);

        aimForceBlocks = builder
                .comment("What should be the force of the aim assistance on blocks?")
                .translation(AimAssistanceMod.MOD_ID + ".config." + "aimForceBlocks")
                .defineInRange("aimForceBlocks", 2.5, 0.1, 10.0);

        aimMobs = builder
                .comment("Should the aim assistance be activated for mobs?")
                .translation(AimAssistanceMod.MOD_ID + ".config." + "aimMobs")
                .define("aimMobs", true);

        aimBlocks = builder
                .comment("Should the aim assistance be activated for blocks?")
                .translation(AimAssistanceMod.MOD_ID + ".config." + "aimBlocks")
                .define("aimBlocks", true);

        builder.pop();
    }
}
