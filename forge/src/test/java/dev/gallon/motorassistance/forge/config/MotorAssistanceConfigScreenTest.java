package dev.gallon.motorassistance.forge.config;

import dev.gallon.motorassistance.common.domain.MotorAssistanceConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MotorAssistanceConfigScreenTest {
    @Test
    void draftCopyContainsEveryValueAndIsIndependent() {
        MotorAssistanceConfig source = nonDefaultConfig();

        MotorAssistanceConfig draft = MotorAssistanceConfigScreen.copy(source);

        assertNotSame(source, draft);
        assertEquals(source.getShowHudIndicator(), draft.getShowHudIndicator());
        assertEquals(source.getFov(), draft.getFov());
        assertEquals(source.getAimBlock(), draft.getAimBlock());
        assertEquals(source.getBlockRange(), draft.getBlockRange());
        assertEquals(source.getMiningInteractionDuration(), draft.getMiningInteractionDuration());
        assertEquals(source.getMiningAssistanceDuration(), draft.getMiningAssistanceDuration());
        assertEquals(source.getMiningAimForce(), draft.getMiningAimForce());
        assertEquals(source.getAimEntity(), draft.getAimEntity());
        assertEquals(source.getEntityRange(), draft.getEntityRange());
        assertEquals(source.getAttackInteractionSpeed(), draft.getAttackInteractionSpeed());
        assertEquals(source.getAttackInteractionDuration(), draft.getAttackInteractionDuration());
        assertEquals(source.getAttackAssistanceDuration(), draft.getAttackAssistanceDuration());
        assertEquals(source.getAttackAimForce(), draft.getAttackAimForce());
        assertEquals(source.getStopAttackOnReached(), draft.getStopAttackOnReached());

        draft.setShowHudIndicator(true);
        draft.setFov(30.0);
        assertFalse(source.getShowHudIndicator());
        assertEquals(120.5, source.getFov());
        assertTrue(draft.getShowHudIndicator());
        assertEquals(30.0, draft.getFov());
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
}
