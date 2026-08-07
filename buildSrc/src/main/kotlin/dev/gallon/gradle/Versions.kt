// Common
const val kotlinVersion = "1.9.10"
const val jvmTarget = "17"

// Minecraft
const val minecraftVersion = "1.20.1"

// Mod
const val modId = "motorassistance"
const val modGroup = "dev.gallon.$modId"
const val commonVersion = "2.1.1"

// Forge - also update mods.toml
const val kotlinForForge = "4.4.0" // https://github.com/thedarkcolour/KotlinForForge
const val kotlinGradlePlugin = kotlinVersion
const val forgeVersion = "47.1.0" // https://files.minecraftforge.net/net/minecraftforge/forge
const val forgeGradlePlugin = "[6.0,6.2)" // https://files.minecraftforge.net/net/minecraftforge/gradle/ForgeGradle/
const val forgeModVersion = "2.1.1-MC$minecraftVersion"

// Fabric - also update fabric.mod.json
// 1.2+ requires gradle 8, forge is not yet compatible
const val loomVersion = "1.3-SNAPSHOT" // https://github.com/FabricMC/fabric-example-mod
const val yarnMappings = "$minecraftVersion+build.9" // https://fabricmc.net/develop/
const val loaderVersion = "0.14.21" // https://fabricmc.net/develop/
const val fabricModVersion = "2.1.1-MC$minecraftVersion"

// External dependencies

// Common
const val clothConfigVersion = "11.1.106" // https://linkie.shedaniel.dev/dependencies

// Fabric - also update fabric.mod.json
const val fabricKotlinVersion = "1.10.10+kotlin.$kotlinVersion" // https://github.com/FabricMC/fabric-language-kotlin
const val modMenuVersion = "7.2.1" // https://github.com/TerraformersMC/ModMenu/releases
const val fabricApiVersion = "0.88.0+1.20.1"
const val frameworkFabricVersion = "4718252" // https://www.curseforge.com/minecraft/mc-mods/framework-fabric
const val controllableFabricVersion = "4592466" // https://www.curseforge.com/minecraft/mc-mods/controllable-fabric

// Forge
const val mixinForgeVersion = "0.7.+"
const val frameworkForgeVersion = "4718251" // https://www.curseforge.com/minecraft/mc-mods/framework
const val controllableForgeVersion = "4598985" // https://www.curseforge.com/minecraft/mc-mods/controllable
