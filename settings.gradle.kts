rootProject.name = "MotorAssistance"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        maven("https://maven.minecraftforge.net/") {
            name = "Forge"
        }
    }
}

include("common")
include("fabric")
include("forge")
