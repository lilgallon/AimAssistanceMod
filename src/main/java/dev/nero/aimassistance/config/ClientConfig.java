package dev.nero.aimassistance.config;

import dev.nero.aimassistance.AimAssistanceMod;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public final ForgeConfigSpec.DoubleValue aimForce;
    public final ForgeConfigSpec.BooleanValue aimMobs;
    public final ForgeConfigSpec.BooleanValue aimBlocks;

    public ClientConfig(ForgeConfigSpec.Builder builder) {

        builder.push("Aim assistance"); // Category

        aimForce = builder
                .comment("What should be the force of the aim assistance?")
                .translation(AimAssistanceMod.MODID + ".config." + "aimForce")
                .defineInRange("aimForce", 5.0, 0.1, 10.0);

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
