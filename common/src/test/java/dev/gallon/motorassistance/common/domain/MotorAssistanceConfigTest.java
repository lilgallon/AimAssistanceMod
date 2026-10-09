package dev.gallon.motorassistance.common.domain;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MotorAssistanceConfigTest {
    @Test
    void declaresNoStaticFieldSoConfigGuisDoNotTryToWriteIt() {
        for (Field field : MotorAssistanceConfig.class.getDeclaredFields()) {
            assertFalse(Modifier.isStatic(field.getModifiers()), field.getName() + " must not be static");
        }
    }

    @Test
    void resetsEveryNullConfigEntryToItsDefaultValue() throws IllegalAccessException {
        MotorAssistanceConfig config = new MotorAssistanceConfig();

        for (Field field : MotorAssistanceConfig.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) && !field.getType().isPrimitive()) {
                field.setAccessible(true);
                field.set(config, null);
            }
        }

        config.resetInvalidValues();

        for (Field field : MotorAssistanceConfig.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) && !field.getType().isPrimitive()) {
                field.setAccessible(true);
                assertNotNull(field.get(config), field.getName() + " was not reset");
            }
        }

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
        assertEquals(1000L, config.getAttackInteractionDuration());
        assertEquals(1100L, config.getAttackAssistanceDuration());
        assertEquals(7.0, config.getAttackAimForce());
        assertFalse(config.getStopAttackOnReached());
    }

    @Test
    void gettersRemainSafeBeforePostLoadValidationRuns() throws ReflectiveOperationException {
        MotorAssistanceConfig config = new MotorAssistanceConfig();
        Field fov = MotorAssistanceConfig.class.getDeclaredField("fov");
        fov.setAccessible(true);
        fov.set(config, null);

        assertEquals(60.0, config.getFov());
    }

    @Test
    void replacesNonFiniteNumbersWithDefaults() {
        MotorAssistanceConfig config = new MotorAssistanceConfig();

        config.setFov(Double.NaN);
        config.setBlockRange(Double.POSITIVE_INFINITY);
        config.setEntityRange(Double.NEGATIVE_INFINITY);
        config.setMiningAimForce(Double.NaN);
        config.setAttackAimForce(Double.POSITIVE_INFINITY);
        config.setAttackInteractionSpeed(Double.NaN);

        assertEquals(60.0, config.getFov());
        assertEquals(7.0, config.getBlockRange());
        assertEquals(5.0, config.getEntityRange());
        assertEquals(7.0, config.getMiningAimForce());
        assertEquals(7.0, config.getAttackAimForce());
        assertEquals(0.5, config.getAttackInteractionSpeed());
    }

    @Test
    void clampsFiniteNumbersToSafeRanges() {
        MotorAssistanceConfig config = new MotorAssistanceConfig();

        config.setFov(Double.MAX_VALUE);
        config.setBlockRange(-Double.MAX_VALUE);
        config.setEntityRange(Double.MAX_VALUE);
        config.setMiningAimForce(-1.0);
        config.setAttackAimForce(Double.MAX_VALUE);
        config.setAttackInteractionSpeed(Double.MAX_VALUE);
        config.setMiningInteractionDuration(Long.MIN_VALUE);
        config.setMiningAssistanceDuration(Long.MAX_VALUE);
        config.setAttackInteractionDuration(0L);
        config.setAttackAssistanceDuration(Long.MAX_VALUE);

        assertEquals(MotorAssistanceConfig.Bounds.MAX_FOV, config.getFov());
        assertEquals(MotorAssistanceConfig.Bounds.MIN_RANGE, config.getBlockRange());
        assertEquals(MotorAssistanceConfig.Bounds.MAX_RANGE, config.getEntityRange());
        assertEquals(MotorAssistanceConfig.Bounds.MIN_AIM_FORCE, config.getMiningAimForce());
        assertEquals(MotorAssistanceConfig.Bounds.MAX_AIM_FORCE, config.getAttackAimForce());
        assertEquals(
                MotorAssistanceConfig.Bounds.MAX_ATTACK_INTERACTION_SPEED,
                config.getAttackInteractionSpeed()
        );
        assertEquals(MotorAssistanceConfig.Bounds.MIN_DURATION, config.getMiningInteractionDuration());
        assertEquals(MotorAssistanceConfig.Bounds.MAX_DURATION, config.getMiningAssistanceDuration());
        assertEquals(
                MotorAssistanceConfig.Bounds.MIN_ATTACK_INTERACTION_DURATION,
                config.getAttackInteractionDuration()
        );
        assertEquals(MotorAssistanceConfig.Bounds.MAX_DURATION, config.getAttackAssistanceDuration());
    }

    @Test
    void preservesValuesAtEveryBoundary() {
        MotorAssistanceConfig config = new MotorAssistanceConfig();

        config.setFov(MotorAssistanceConfig.Bounds.MIN_FOV);
        assertEquals(MotorAssistanceConfig.Bounds.MIN_FOV, config.getFov());
        config.setFov(MotorAssistanceConfig.Bounds.MAX_FOV);
        assertEquals(MotorAssistanceConfig.Bounds.MAX_FOV, config.getFov());

        config.setEntityRange(MotorAssistanceConfig.Bounds.MIN_RANGE);
        assertEquals(MotorAssistanceConfig.Bounds.MIN_RANGE, config.getEntityRange());
        config.setEntityRange(MotorAssistanceConfig.Bounds.MAX_RANGE);
        assertEquals(MotorAssistanceConfig.Bounds.MAX_RANGE, config.getEntityRange());

        config.setAttackInteractionDuration(MotorAssistanceConfig.Bounds.MIN_ATTACK_INTERACTION_DURATION);
        assertEquals(
                MotorAssistanceConfig.Bounds.MIN_ATTACK_INTERACTION_DURATION,
                config.getAttackInteractionDuration()
        );
        config.setAttackInteractionDuration(MotorAssistanceConfig.Bounds.MAX_DURATION);
        assertEquals(MotorAssistanceConfig.Bounds.MAX_DURATION, config.getAttackInteractionDuration());
    }
}
