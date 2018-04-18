import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.mverse.gradle.sourceSets
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.internal.HasConvention
import org.gradle.api.internal.file.pattern.PatternMatcherFactory.compile
import org.gradle.internal.impldep.bsh.commands.dir
import org.gradle.internal.nativeintegration.filesystem.DefaultFileMetadata.file
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCallArgument.DefaultArgument.arguments

plugins {
  id("org.gradle.kotlin.kotlin-dsl").version("0.16.0")
  id("io.mverse.project").version("0.5.23")
  id("com.github.johnrengelman.shadow").version("2.0.3")
  id("antlr")
}

mverse {
  groupId = "club.kidgames"
  isDefaultDependencies = false
  dependencies {
    testCompile(junit())
    testCompile(junitParams())
    testCompile(assertj())

    compileOnly(lombok())
    fatJar("jackson-databind")
    fatJar("jackson-core")
    fatJar("kotlin-stdlib")
    fatJar(guava())
    fatJar(streamEx())
  }
  coverageRequirement = 0.60
  java.sourceSets["main"].withConvention(KotlinSourceSet::class) {
    kotlin.srcDir(file("build/classes/generated-src/antlr/main"))
  }
}

findbugs {
  this.isIgnoreFailures = true
  effort = "min"
}

dependencies {
  compileOnly("org.jsoup:jsoup:1.11.2") {
    isTransitive = false
  }

  testRuntime("org.jsoup:jsoup:1.11.2") 

  compileOnly("com.google.code.findbugs:findbugs:3.0.1") {
    isTransitive = false
  }

  antlr("org.antlr:antlr4:4.7.1")
  fatJar("org.antlr:antlr4-runtime:4.7.1")
}

configurations.compile.extendsFrom(configurations.fatJar)


//
// Configure shadow
//
val shadowJar: ShadowJar by tasks
shadowJar.apply {
  classifier = null
  configurations = listOf(project.configurations.fatJar)
  dependencies {
    include(dependency(":kotlin-stdlib"))
    include(dependency(":kotlin-stdlib-jdk8"))
    include(dependency(":jackson-core"))
    include(dependency(":jackson-databind"))
    include(dependency(":guava"))
  }
}

tasks["assemble"].dependsOn(shadowJar)

//
// Configure antlr
//
java.sourceSets["main"].withConvention(KotlinSourceSet::class) {
  kotlin.srcDir(file("build/classes/generated-src/antlr/main"))
}

tasks.withType(AntlrTask::class.java) {
  arguments = listOf("-visitor", "-package", "liquid.parser.v4", "-Xexact-output-dir")
  outputDirectory = file("build/generated-src/antlr/main/liquid/parser/v4")
}

tasks["compileKotlin"].dependsOn("generateGrammarSource")

