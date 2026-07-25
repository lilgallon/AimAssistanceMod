package dev.gallon.motorassistance.fabric.config;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TheModConfigTest {
    @Test
    void resetsEntriesWithIncompatibleTypesToTheirDefaultValues() throws Exception {
        Jankson jankson = Jankson.builder().build();
        TheModConfig config = jankson.fromJson(
                """
                        {
                          "modConfig": {
                            "showHudIndicator": "NOT_A_BOOLEAN",
                            "fov": "NOT_A_NUMBER",
                            "aimBlock": "NOT_A_BOOLEAN",
                            "blockRange": 12.5
                          }
                        }
                        """,
                TheModConfig.class
        );

        config.validatePostLoad();

        assertTrue(config.modConfig.getShowHudIndicator());
        assertEquals(60.0, config.modConfig.getFov());
        assertTrue(config.modConfig.getAimBlock());
        assertEquals(12.5, config.modConfig.getBlockRange());
    }

    @Test
    void recreatesMissingNestedConfiguration() throws Exception {
        Jankson jankson = Jankson.builder().build();
        TheModConfig config = jankson.fromJson("{ \"modConfig\": null }", TheModConfig.class);

        config.validatePostLoad();

        assertNotNull(config.modConfig);
        assertEquals(60.0, config.modConfig.getFov());
    }

    @Test
    void sanitizesDangerousNumericValues() throws Exception {
        Jankson jankson = Jankson.builder().build();
        TheModConfig config = jankson.fromJson(
                """
                        {
                          "modConfig": {
                            "fov": 1.7976931348623157E308,
                            "blockRange": -1.7976931348623157E308,
                            "entityRange": 1.7976931348623157E308,
                            "attackInteractionDuration": 0,
                            "attackAssistanceDuration": 9223372036854775807
                          }
                        }
                        """,
                TheModConfig.class
        );

        config.validatePostLoad();

        assertEquals(180.0, config.modConfig.getFov());
        assertEquals(0.0, config.modConfig.getBlockRange());
        assertEquals(64.0, config.modConfig.getEntityRange());
        assertEquals(1L, config.modConfig.getAttackInteractionDuration());
        assertEquals(60_000L, config.modConfig.getAttackAssistanceDuration());
    }
}
