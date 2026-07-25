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
}
