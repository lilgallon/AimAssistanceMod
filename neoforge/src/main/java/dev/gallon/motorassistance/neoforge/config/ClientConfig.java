package dev.gallon.motorassistance.neoforge.config;

import dev.gallon.motorassistance.common.domain.ModMetadata;
import dev.gallon.motorassistance.common.domain.MotorAssistanceConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ClientConfig {
    public final ModConfigSpec.BooleanValue showHudIndicator;
    public final ModConfigSpec.DoubleValue fov;
    public final ModConfigSpec.BooleanValue aimBlock;
    public final ModConfigSpec.DoubleValue blockRange;
    public final ModConfigSpec.LongValue miningInteractionDuration;
    public final ModConfigSpec.LongValue miningAssistanceDuration;
    public final ModConfigSpec.DoubleValue miningAimForce;
    public final ModConfigSpec.BooleanValue aimEntity;
    public final ModConfigSpec.DoubleValue entityRange;
    public final ModConfigSpec.DoubleValue attackInteractionSpeed;
    public final ModConfigSpec.LongValue attackInteractionDuration;
    public final ModConfigSpec.LongValue attackAssistanceDuration;
    public final ModConfigSpec.DoubleValue attackAimForce;
    public final ModConfigSpec.BooleanValue stopAttackOnReached;

    public ClientConfig(ModConfigSpec.Builder builder) {
        builder.push(ModMetadata.MOD_ID);

        showHudIndicator = builder
                .comment("Show the current aim assistance status in the HUD.")
                .translation(translationKey("showHudIndicator"))
                .define("showHudIndicator", true);
        fov = builder
                .comment("Field of view in which aim assistance can acquire a target.")
                .translation(translationKey("fov"))
                .defineInRange(
                        "fov",
                        60.0,
                        MotorAssistanceConfig.MIN_FOV,
                        MotorAssistanceConfig.MAX_FOV
                );
        aimBlock = builder
                .comment("Enable aim assistance while mining blocks.")
                .translation(translationKey("aimBlock"))
                .define("aimBlock", true);
        blockRange = builder
                .comment("Maximum block targeting distance.")
                .translation(translationKey("blockRange"))
                .defineInRange(
                        "blockRange",
                        7.0,
                        MotorAssistanceConfig.MIN_RANGE,
                        MotorAssistanceConfig.MAX_RANGE
                );
        miningInteractionDuration = builder
                .comment("Interaction duration in milliseconds before mining assistance starts.")
                .translation(translationKey("miningInteractionDuration"))
                .defineInRange(
                        "miningInteractionDuration",
                        500L,
                        MotorAssistanceConfig.MIN_DURATION,
                        MotorAssistanceConfig.MAX_DURATION
                );
        miningAssistanceDuration = builder
                .comment("Maximum mining assistance duration in milliseconds.")
                .translation(translationKey("miningAssistanceDuration"))
                .defineInRange(
                        "miningAssistanceDuration",
                        600L,
                        MotorAssistanceConfig.MIN_DURATION,
                        MotorAssistanceConfig.MAX_DURATION
                );
        miningAimForce = builder
                .comment("Strength of mining aim assistance.")
                .translation(translationKey("miningAimForce"))
                .defineInRange(
                        "miningAimForce",
                        7.0,
                        MotorAssistanceConfig.MIN_AIM_FORCE,
                        MotorAssistanceConfig.MAX_AIM_FORCE
                );
        aimEntity = builder
                .comment("Enable aim assistance while attacking entities.")
                .translation(translationKey("aimEntity"))
                .define("aimEntity", true);
        entityRange = builder
                .comment("Maximum entity targeting distance.")
                .translation(translationKey("entityRange"))
                .defineInRange(
                        "entityRange",
                        5.0,
                        MotorAssistanceConfig.MIN_RANGE,
                        MotorAssistanceConfig.MAX_RANGE
                );
        attackInteractionSpeed = builder
                .comment("Clicks per second required to trigger attack assistance.")
                .translation(translationKey("attackInteractionSpeed"))
                .defineInRange(
                        "attackInteractionSpeed",
                        0.5,
                        MotorAssistanceConfig.MIN_ATTACK_INTERACTION_SPEED,
                        MotorAssistanceConfig.MAX_ATTACK_INTERACTION_SPEED
                );
        attackInteractionDuration = builder
                .comment("Interaction duration in milliseconds before attack assistance starts.")
                .translation(translationKey("attackInteractionDuration"))
                .defineInRange(
                        "attackInteractionDuration",
                        1000L,
                        MotorAssistanceConfig.MIN_ATTACK_INTERACTION_DURATION,
                        MotorAssistanceConfig.MAX_DURATION
                );
        attackAssistanceDuration = builder
                .comment("Maximum attack assistance duration in milliseconds.")
                .translation(translationKey("attackAssistanceDuration"))
                .defineInRange(
                        "attackAssistanceDuration",
                        1100L,
                        MotorAssistanceConfig.MIN_DURATION,
                        MotorAssistanceConfig.MAX_DURATION
                );
        attackAimForce = builder
                .comment("Strength of attack aim assistance.")
                .translation(translationKey("attackAimForce"))
                .defineInRange(
                        "attackAimForce",
                        7.0,
                        MotorAssistanceConfig.MIN_AIM_FORCE,
                        MotorAssistanceConfig.MAX_AIM_FORCE
                );
        stopAttackOnReached = builder
                .comment("Stop attack assistance once the targeted entity is reached.")
                .translation(translationKey("stopAttackOnReached"))
                .define("stopAttackOnReached", false);

        builder.pop();
    }

    private static String translationKey(String option) {
        return "text.autoconfig." + ModMetadata.MOD_ID + ".option.modConfig." + option;
    }
}
