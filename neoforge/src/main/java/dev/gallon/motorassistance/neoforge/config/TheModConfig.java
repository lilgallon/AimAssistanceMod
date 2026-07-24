package dev.gallon.motorassistance.neoforge.config;

import dev.gallon.motorassistance.common.domain.ModMetadata;
import dev.gallon.motorassistance.common.domain.MotorAssistanceConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(modid = ModMetadata.MOD_ID, value = Dist.CLIENT)
public final class TheModConfig {
    public static final ClientConfig CLIENT;
    public static final ModConfigSpec CLIENT_SPEC;
    public static final MotorAssistanceConfig config = new MotorAssistanceConfig();

    static {
        Pair<ClientConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT = specPair.getLeft();
        CLIENT_SPEC = specPair.getRight();
    }

    private TheModConfig() {
    }

    @SubscribeEvent
    public static void onModConfigEvent(net.neoforged.fml.event.config.ModConfigEvent configEvent) {
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
    }
}
