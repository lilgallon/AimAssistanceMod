package dev.gallon.motorassistance.fabric.config;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
