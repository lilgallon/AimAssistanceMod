plugins {
    kotlin("jvm")
}

group = "$modGroup.common"
version = commonVersion

repositories {
    mavenCentral()
}

dependencies {
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = jvmTarget
    }
}
