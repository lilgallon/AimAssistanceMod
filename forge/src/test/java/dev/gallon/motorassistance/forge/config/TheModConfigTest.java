package dev.gallon.motorassistance.forge.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import dev.gallon.motorassistance.common.domain.MotorAssistanceConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TheModConfigTest {
    @BeforeEach
    void loadDefaultConfig() {
        TheModConfig.CLIENT_SPEC.acceptConfig(CommentedConfig.inMemory());
        TheModConfig.bakeConfig();
    }

    @Test
    void loadingAndReloadingBakeAllOptions() {
        setSpecToNonDefaultValues();

        TheModConfig.bakeConfig();

        assertNonDefaultValues(TheModConfig.config);
    }

    @Test
    void saveUpdatesTheNativeSpecAndTheCommonConfig() {
        MotorAssistanceConfig draft = nonDefaultConfig();

        TheModConfig.applyAndSave(draft);

        assertFalse(TheModConfig.CLIENT.showHudIndicator.get());
        assertEquals(120.5, TheModConfig.CLIENT.fov.get());
        assertFalse(TheModConfig.CLIENT.aimBlock.get());
        assertEquals(16.5, TheModConfig.CLIENT.blockRange.get());
        assertEquals(1_000L, TheModConfig.CLIENT.miningInteractionDuration.get());
        assertEquals(2_000L, TheModConfig.CLIENT.miningAssistanceDuration.get());
        assertEquals(25.5, TheModConfig.CLIENT.miningAimForce.get());
        assertFalse(TheModConfig.CLIENT.aimEntity.get());
        assertEquals(12.5, TheModConfig.CLIENT.entityRange.get());
        assertEquals(3.5, TheModConfig.CLIENT.attackInteractionSpeed.get());
        assertEquals(2_500L, TheModConfig.CLIENT.attackInteractionDuration.get());
        assertEquals(3_000L, TheModConfig.CLIENT.attackAssistanceDuration.get());
        assertEquals(40.5, TheModConfig.CLIENT.attackAimForce.get());
        assertTrue(TheModConfig.CLIENT.stopAttackOnReached.get());
        assertNonDefaultValues(TheModConfig.config);
    }

    @Test
    void resetRestoresEveryDefault() {
        TheModConfig.applyAndSave(nonDefaultConfig());

        TheModConfig.applyAndSave(new MotorAssistanceConfig());

        MotorAssistanceConfig config = TheModConfig.config;
        assertTrue(config.getShowHudIndicator());
        assertEquals(60.0, config.getFov());
        assertTrue(config.getAimBlock());
        assertEquals(7.0, config.getBlockRange());
        assertEquals(500L, config.getMiningInteractionDuration());
        assertEquals(600L, config.getMiningAssistanceDuration());
        assertEquals(7.0, config.getMiningAimForce());
        assertTrue(config.getAimEntity());
        assertEquals(5.0, config.getEntityRange());
        assertEquals(0.5, config.getAttackInteractionSpeed());
        assertEquals(1_000L, config.getAttackInteractionDuration());
        assertEquals(1_100L, config.getAttackAssistanceDuration());
        assertEquals(7.0, config.getAttackAimForce());
        assertFalse(config.getStopAttackOnReached());
    }

    private static void setSpecToNonDefaultValues() {
        TheModConfig.CLIENT.showHudIndicator.set(false);
        TheModConfig.CLIENT.fov.set(120.5);
        TheModConfig.CLIENT.aimBlock.set(false);
        TheModConfig.CLIENT.blockRange.set(16.5);
        TheModConfig.CLIENT.miningInteractionDuration.set(1_000L);
        TheModConfig.CLIENT.miningAssistanceDuration.set(2_000L);
        TheModConfig.CLIENT.miningAimForce.set(25.5);
        TheModConfig.CLIENT.aimEntity.set(false);
        TheModConfig.CLIENT.entityRange.set(12.5);
        TheModConfig.CLIENT.attackInteractionSpeed.set(3.5);
        TheModConfig.CLIENT.attackInteractionDuration.set(2_500L);
        TheModConfig.CLIENT.attackAssistanceDuration.set(3_000L);
        TheModConfig.CLIENT.attackAimForce.set(40.5);
        TheModConfig.CLIENT.stopAttackOnReached.set(true);
    }

    private static MotorAssistanceConfig nonDefaultConfig() {
        MotorAssistanceConfig config = new MotorAssistanceConfig();
        config.setShowHudIndicator(false);
        config.setFov(120.5);
        config.setAimBlock(false);
        config.setBlockRange(16.5);
        config.setMiningInteractionDuration(1_000L);
        config.setMiningAssistanceDuration(2_000L);
        config.setMiningAimForce(25.5);
        config.setAimEntity(false);
        config.setEntityRange(12.5);
        config.setAttackInteractionSpeed(3.5);
        config.setAttackInteractionDuration(2_500L);
        config.setAttackAssistanceDuration(3_000L);
        config.setAttackAimForce(40.5);
        config.setStopAttackOnReached(true);
        return config;
    }

    private static void assertNonDefaultValues(MotorAssistanceConfig config) {
        assertFalse(config.getShowHudIndicator());
        assertEquals(120.5, config.getFov());
        assertFalse(config.getAimBlock());
        assertEquals(16.5, config.getBlockRange());
        assertEquals(1_000L, config.getMiningInteractionDuration());
        assertEquals(2_000L, config.getMiningAssistanceDuration());
        assertEquals(25.5, config.getMiningAimForce());
        assertFalse(config.getAimEntity());
        assertEquals(12.5, config.getEntityRange());
        assertEquals(3.5, config.getAttackInteractionSpeed());
        assertEquals(2_500L, config.getAttackInteractionDuration());
        assertEquals(3_000L, config.getAttackAssistanceDuration());
        assertEquals(40.5, config.getAttackAimForce());
        assertTrue(config.getStopAttackOnReached());
    }
}
