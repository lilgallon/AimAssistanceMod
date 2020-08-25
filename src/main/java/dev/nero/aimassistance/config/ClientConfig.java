package dev.nero.aimassistance.config;

import dev.nero.aimassistance.AimAssistanceMod;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public final ForgeConfigSpec.DoubleValue aimForceMobs;
    public final ForgeConfigSpec.DoubleValue aimForceBlocks;
    public final ForgeConfigSpec.BooleanValue aimMobs;
    public final ForgeConfigSpec.BooleanValue aimBlocks;

    public ClientConfig(ForgeConfigSpec.Builder builder) {

        builder.push("Aim assistance"); // Category

        aimForceMobs = builder
                .comment("What should be the force of the aim assistance on mobs?")
                .translation(AimAssistanceMod.MODID + ".config." + "aimForceMobs")
                .defineInRange("aimForceMobs", 5.0, 0.1, 10.0);

        aimForceBlocks = builder
                .comment("What should be the force of the aim assistance on blocks?")
                .translation(AimAssistanceMod.MODID + ".config." + "aimForceBlocks")
                .defineInRange("aimForceBlocks", 5.0, 0.1, 10.0);

        aimMobs = builder
                .comment("Should the aim assistance be activated for mobs?")
                .translation(AimAssistanceMod.MODID + ".config." + "aimMobs")
                .define("aimMobs", true);

        aimBlocks = builder
                .comment("Should the aim assistance be activated for blocks?")
                .translation(AimAssistanceMod.MODID + ".config." + "aimBlocks")
                .define("aimBlocks", true);

        builder.pop();
    }
}
