import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
  id("org.gradle.kotlin.kotlin-dsl")
  id("com.github.johnrengelman.shadow")
  id("antlr")
}


mverse {
  dependencies {
    testCompile(junit())
    testCompile(junitParams())
    testCompile(mockito())
    testCompile(assertj())
    fatJar(guava())
    fatJar(streamEx())
    compileOnly(lombok())

    fatJar("jackson-databind")
    fatJar("jackson-annotations")
    fatJar("jackson-core")
    fatJar("kotlin-reflect")
    fatJar("kotlin-stdlib")
    fatJar("antlr4-runtime")
  }
  dependencies["antlr"]("antlr4")
  dependencies["testRuntime"]("jsoup")

  java.sourceSets["main"].withConvention(KotlinSourceSet::class) {
    kotlin.srcDir(file("build/classes/generated-src/antlr/main"))
  }
}

configurations.compile.extendsFrom(configurations.fatJar)

//##### Configure shadow jar ##### //
val shadowJar: ShadowJar by tasks
shadowJar.apply {
  classifier = null
  configurations = listOf(project.configurations.fatJar)
  relocate("com.fasterxml", "kg.com.fasterxml")
  relocate("one", "kg.one")
}

tasks["assemble"].dependsOn(shadowJar)

// #### Configure antlr ##### //
// ########################## //
java.sourceSets["main"].withConvention(KotlinSourceSet::class) {
  kotlin.srcDir(file("build/generated-src/antlr/main"))
}

tasks.withType(AntlrTask::class.java) {
  arguments = listOf("-visitor", "-package", "liquid.parser.v4", "-Xexact-output-dir")
  outputDirectory = project.file("build/generated-src/antlr/main/liquid/parser/v4")
}

tasks["compileKotlin"].dependsOn("generateGrammarSource")
