plugins {
    kotlin("jvm")
}

group = "$modGroup.common"
version = commonVersion

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = jvmTarget
    }

    test {
        useJUnitPlatform()
    }
}
