package dev.gallon.motorassistance.forge.config;

import dev.gallon.motorassistance.common.domain.MotorAssistanceConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public final class TheModConfig {
    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final MotorAssistanceConfig config = new MotorAssistanceConfig();

    static {
        Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT = specPair.getLeft();
        CLIENT_SPEC = specPair.getRight();
    }

    private TheModConfig() {
    }

    public static void register(BusGroup modBusGroup) {
        ModConfigEvent.Loading.getBus(modBusGroup).addListener(TheModConfig::onModConfigEvent);
        ModConfigEvent.Reloading.getBus(modBusGroup).addListener(TheModConfig::onModConfigEvent);
    }

    private static void onModConfigEvent(ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == CLIENT_SPEC) {
            bakeConfig();
        }
    }

    public static void bakeConfig() {
        config.setShowHudIndicator(CLIENT.showHudIndicator.get());
        config.setFov(CLIENT.fov.get());
        config.setAimBlock(CLIENT.aimBlock.get());
        config.setBlockRange(CLIENT.blockRange.get());
        config.setMiningInteractionDuration(CLIENT.miningInteractionDuration.get());
        config.setMiningAssistanceDuration(CLIENT.miningAssistanceDuration.get());
        config.setMiningAimForce(CLIENT.miningAimForce.get());
        config.setAimEntity(CLIENT.aimEntity.get());
        config.setEntityRange(CLIENT.entityRange.get());
        config.setAttackInteractionSpeed(CLIENT.attackInteractionSpeed.get());
        config.setAttackInteractionDuration(CLIENT.attackInteractionDuration.get());
        config.setAttackAssistanceDuration(CLIENT.attackAssistanceDuration.get());
        config.setAttackAimForce(CLIENT.attackAimForce.get());
        config.setStopAttackOnReached(CLIENT.stopAttackOnReached.get());
        config.resetInvalidValues();
    }

    public static void applyAndSave(MotorAssistanceConfig source) {
        CLIENT.showHudIndicator.set(source.getShowHudIndicator());
        CLIENT.fov.set(source.getFov());
        CLIENT.aimBlock.set(source.getAimBlock());
        CLIENT.blockRange.set(source.getBlockRange());
        CLIENT.miningInteractionDuration.set(source.getMiningInteractionDuration());
        CLIENT.miningAssistanceDuration.set(source.getMiningAssistanceDuration());
        CLIENT.miningAimForce.set(source.getMiningAimForce());
        CLIENT.aimEntity.set(source.getAimEntity());
        CLIENT.entityRange.set(source.getEntityRange());
        CLIENT.attackInteractionSpeed.set(source.getAttackInteractionSpeed());
        CLIENT.attackInteractionDuration.set(source.getAttackInteractionDuration());
        CLIENT.attackAssistanceDuration.set(source.getAttackAssistanceDuration());
        CLIENT.attackAimForce.set(source.getAttackAimForce());
        CLIENT.stopAttackOnReached.set(source.getStopAttackOnReached());
        CLIENT_SPEC.save();
        bakeConfig();
    }
}
