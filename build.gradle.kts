import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.mverse.gradle.sourceSets
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.internal.HasConvention
import org.gradle.api.internal.file.pattern.PatternMatcherFactory.compile
import org.gradle.internal.impldep.bsh.commands.dir
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include
import org.gradle.internal.nativeintegration.filesystem.DefaultFileMetadata.file
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCallArgument.DefaultArgument.arguments

plugins {
  id("org.gradle.kotlin.kotlin-dsl").version("0.17.0")
  id("io.mverse.project").version("0.5.23")
  id("io.mverse.multi-module").version("0.5.23")
}

allprojects {
  mverse {
    groupId = "club.kidgames"
    isDefaultDependencies = false

    coverageRequirement = 0.60
    dependencies {
      testCompile("assertk")
    }
  }

  dependencyManagement {
    dependencies {
      dependency("org.jsoup:jsoup:1.11.2")
      dependency("org.antlr:antlr4:4.7.1")
      dependency("org.antlr:antlr4-runtime:4.7.1")
      dependency("com.willowtreeapps.assertk:assertk:0.10")
    }
  }

  dependencies {
    compileOnly("com.google.code.findbugs:findbugs") {
      isTransitive = false
    }
  }
}
