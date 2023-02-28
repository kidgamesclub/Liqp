plugins {
    `groovy-gradle-plugin`
    `java-library`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
    maven {
        setUrl("https://edspuzzle.jfrog.io/artifactory/edspuzzle-gradle-release/")
        credentials {
            username = System.getProperty("ARTIFACTORY_USERNAME")
            password = System.getProperty("ARTIFACTORY_PASSWORD")
        }
    }
}

dependencies {
    implementation("io.mverse.multi-module:io.mverse.multi-module.gradle.plugin:0.6.15")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
}
