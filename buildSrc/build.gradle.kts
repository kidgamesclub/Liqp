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
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    implementation("com.netflix.nebula:nebula-publishing-plugin:18.4.0")
    implementation("com.netflix.nebula:nebula-release-plugin:17.1.0")
    implementation("org.jfrog.buildinfo:build-info-extractor-gradle:4.31.4")
}
