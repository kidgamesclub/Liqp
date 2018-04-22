import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
  id("org.gradle.kotlin.kotlin-dsl")
}

mverse {
  dependencies {
    compile(guava())
  }
}

dependencies {
  compile(project(":liqp-core"))
  compile("org.assertj:assertj-core")
  compile("net.wuerl.kotlin:assertj-core-kotlin:0.2.1")
}
