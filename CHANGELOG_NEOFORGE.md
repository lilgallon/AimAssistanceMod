## Version 3.1.2 NeoForge

_supports Minecraft 26.2.x_

Changes:
- Compatibility is now limited to Minecraft 26.2.x so incompatible future mixin targets are rejected before the mod loads.
- Missing, malformed, non-finite and dangerous numeric configuration values are now corrected through the native NeoForge configuration.
- Unexpected internal runtime failures are logged once and disable the assistance and its HUD for the rest of the session instead of crashing Minecraft.
- Player rotation, target validity and world/player lifecycle handling have been hardened.
