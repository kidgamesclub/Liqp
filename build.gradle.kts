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
  id("io.mverse.project").version("0.5.22")
  id("com.github.johnrengelman.shadow").version("2.0.3")
  id("antlr")
}

mverse {
  groupId = "club.kidgames"
  modules {
    compile("jackson-databind")
    compile("jackson-core")
    compile("kotlin-stdlib")
    compileOnly("lombok")
    compile("guava")

  }
  coverageRequirement = 0.60
  java.sourceSets["main"].withConvention(KotlinSourceSet::class) {
    kotlin.srcDir(file("build/classes/generated-src/antlr/main"))
  }
  dependencies.commonsLang3 = false
  dependencies.guava = false
  dependencies.groovy = false
}

findbugs {
  this.isIgnoreFailures = true
  effort = "min"
}

dependencies {
  compileOnly("org.jsoup:jsoup:1.11.2") {
    isTransitive = false
  }

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
  configurations = listOf(project.configurations.fatJar)
  dependencies {
    include(dependency(":kotlin-stdlib"))
    include(dependency(":kotlin-stdlib-jdk8"))
    include(dependency(":jackson-core"))
    include(dependency(":jackson-databind"))
    include(dependency(":guava"))
  }
}

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

