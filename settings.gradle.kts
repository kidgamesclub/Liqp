import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories


pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    maven("https://dl.bintray.com/mverse-io/mverse-public")
  }
}

rootProject.name = "liqp"
include("liqp-core")
include("liqp-ext")
include("liqp-junit")

