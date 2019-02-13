import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.mverse.gradle.main
import io.mverse.gradle.sourceSets
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
  java
  id("com.github.johnrengelman.shadow")
  id("antlr")
  kotlin("jvm")
}

dependencies {
  compile(project(":liqp-api"))
  testCompile(project(":liqp-junit"))
  compile("com.github.alexheretic:dynamics:4.0")
}

mverse {
  dependencies {
    testCompile(junit())
    testCompile(mockito())
    testCompile(assertj())
    testCompile(assertK())
    fatJar(guava())
    fatJar(streamEx())
    fatJar("jackson-databind")
    fatJar("jackson-annotations")
    fatJar("jackson-core")
    fatJar("kotlin-reflect")
    fatJar("kotlin-stdlib")
    fatJar("antlr4-runtime")
  }
  dependencies["antlr"]("antlr4")
  dependencies["testRuntime"]("jsoup")

  sourceSets.main?.withConvention(KotlinSourceSet::class) {
    kotlin.srcDir(file("build/classes/generated-src/antlr/main"))
  }
}

configurations.compile.extendsFrom(configurations.fatJar)

//##### Configure shadow jar ##### //
val shadowJar: ShadowJar by tasks
shadowJar.apply {
  configurations = listOf(project.configurations.fatJar)
  relocate("com.fasterxml", "kg.com.fasterxml")
  relocate("one", "kg.one")
}

tasks["assemble"].dependsOn(shadowJar)

// #### Configure antlr ##### //
// ########################## //
sourceSets.main?.withConvention(KotlinSourceSet::class) {
  kotlin.srcDir(file("build/generated-src/antlr/main"))
}

tasks.withType(AntlrTask::class.java) {
  arguments = listOf("-visitor", "-package", "liquid.parser.v4", "-Xexact-output-dir")
  outputDirectory = project.file("build/generated-src/antlr/main/liquid/parser/v4")
}

tasks["compileKotlin"].dependsOn("generateGrammarSource")
