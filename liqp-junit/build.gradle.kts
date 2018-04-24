import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
  id("org.gradle.kotlin.kotlin-dsl")
  id("findbugs")
}

mverse {
  groupId = "club.kidgames"

  dependencies {
    compile(guava())
  }
  coverageRequirement = 0.00
}

findbugs {
  isIgnoreFailures = true
}

dependencies {
  compile(project(":liqp-core"))
  compile("org.assertj:assertj-core")
  compile("net.wuerl.kotlin:assertj-core-kotlin:0.2.1")
}
