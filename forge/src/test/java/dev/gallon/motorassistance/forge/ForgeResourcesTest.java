package dev.gallon.motorassistance.forge;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ForgeResourcesTest {
    private static final List<String> CONFIG_OPTIONS = List.of(
            "showHudIndicator",
            "fov",
            "aimBlock",
            "blockRange",
            "miningInteractionDuration",
            "miningAssistanceDuration",
            "miningAimForce",
            "aimEntity",
            "entityRange",
            "attackInteractionSpeed",
            "attackInteractionDuration",
            "attackAssistanceDuration",
            "attackAimForce",
            "stopAttackOnReached"
    );

    @Test
    void packagesForge26MetadataAndOnlyForgeLoaderMetadata() throws IOException {
        String modsToml = readResource("/META-INF/mods.toml");
        String packMetadata = readResource("/pack.mcmeta");
        String compactPackMetadata = packMetadata.replaceAll("\\s+", "");

        assertTrue(modsToml.contains("modId=\"motorassistancemod\""));
        assertTrue(modsToml.contains("version=\"3.2.0\""));
        assertTrue(modsToml.contains("clientSideOnly=true"));
        assertTrue(modsToml.contains("versionRange=\"[66.0.9,)\""));
        assertTrue(modsToml.contains("versionRange=\"[26.3,26.4)\""));
        assertTrue(modsToml.contains("updateJSONURL="));
        assertFalse(modsToml.contains("${"));

        assertTrue(packMetadata.contains("\"description\": \"motorassistancemod resources\""));
        assertTrue(packMetadata.contains("\"max_format\": 121"));
        assertTrue(compactPackMetadata.contains("\"min_format\":[121,0]"));
        assertNull(ForgeResourcesTest.class.getResource("/fabric.mod.json"));
        assertNull(ForgeResourcesTest.class.getResource("/META-INF/neoforge.mods.toml"));
        assertNull(ForgeResourcesTest.class.getResource("/pack.metadata"));
    }

    @Test
    void packagesEveryConfigScreenTranslation() throws IOException {
        String translations = readResource("/assets/motorassistancemod/lang/en_us.json");
        for (String option : CONFIG_OPTIONS) {
            String key = "text.autoconfig.motorassistancemod.option.modConfig." + option;
            assertTrue(translations.contains("\"" + key + "\""), "Missing " + key);
            assertTrue(translations.contains("\"" + key + ".tooltip\""), "Missing tooltip for " + key);
        }
    }

    private static String readResource(String path) throws IOException {
        try (InputStream stream = ForgeResourcesTest.class.getResourceAsStream(path)) {
            assertNotNull(stream, "Missing classpath resource " + path);
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
