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
  id("io.mverse.project").version("0.5.16")
  id("antlr")
}

mverse {
  groupId = "club.kidgames"
  modules {
    compile("jackson-annotations")
    compile("jackson-databind")
    compile("jackson-core")
    compile("findbugs")
    compile("kotlin-stdlib")
  }
  coverageRequirement = 0.60
}


findbugs {
  this.isIgnoreFailures = true
  effort = "min"
}

java.sourceSets["main"].withConvention(KotlinSourceSet::class) {
  kotlin.srcDir(file("build/classes/generated-src/antlr/main"))
}

dependencies {
  compile("org.antlr:antlr4-runtime:4.7.1")
  compile("org.jsoup:jsoup:1.11.2")
  antlr("org.antlr:antlr4:4.7.1")
}

tasks.withType(AntlrTask::class.java) {
  arguments = listOf("-visitor", "-package", "liquid.parser.v4", "-Xexact-output-dir")
  outputDirectory = file("build/generated-src/antlr/main/liquid/parser/v4")
}

tasks["compileKotlin"].dependsOn("generateGrammarSource")
