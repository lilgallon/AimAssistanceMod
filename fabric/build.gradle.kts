plugins {
    id("fabric-loom") version loomVersion
    kotlin("jvm")
    `maven-publish`
    java
}

base {
    archivesName.set(modId)
}


group = "$modGroup.fabric"
version = "$fabricModVersion-fabric"

repositories {
    maven("https://maven.shedaniel.me/") // cloth config
    maven("https://maven.terraformersmc.com/releases/") // mod menu
    maven("https://cursemaven.com") // Controllable
}

dependencies {
    compileOnly(project(":common"))
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")
    modApi("me.shedaniel.cloth:cloth-config-fabric:$clothConfigVersion") {
        // exclude("net.fabricmc.fabric-api")
    }
    modApi("com.terraformersmc:modmenu:$modMenuVersion")

    // The libs below end up being optional to run the mod, so make sure that the code that uses these
    // libs checks if the corresponding mods are loaded first

    // Use these lines to have controllable loaded (used to test the mod using controllable)
    // TODO: I cannot make it work for now ... framework has no documentation whatsoever to know how to use it
//    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
//    modImplementation("curse.maven:controllable-fabric-851657:$controllableFabricVersion")
//    modImplementation("curse.maven:framework-fabric-667391:$frameworkFabricVersion")
    // Use this line to not have controllable loaded during runtime (used to test the mod without controllable loaded)
    compileOnly("curse.maven:controllable-fabric-851657:$controllableFabricVersion")
}

sourceSets {
    main {
        java {
            srcDir(project(":common").sourceSets.main.get().java)
        }

        kotlin {
            srcDir(project(":common").sourceSets.main.get().kotlin)
        }

        resources {
            srcDir(project(":common").sourceSets.main.get().resources)
        }
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version))
        }
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from("LICENSE")
    }

    compileKotlin {
        kotlinOptions.jvmTarget = jvmTarget
    }
}

java {
    withSourcesJar()
}
