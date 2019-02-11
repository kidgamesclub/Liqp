import io.mverse.gradle.task.CoverageReportTask
import io.spring.gradle.dependencymanagement.dsl.DependenciesHandler
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  java
  kotlin("jvm")
  id("io.mverse.project")
  id("io.mverse.multi-module")
}



allprojects {
  mverse {
    groupId = "io.mverse"
    isDefaultDependencies = false

    coverageRequirement = 0.60
    dependencies {
      compileOnly(kotlinStdlib())
      compileOnly("kotlin-reflect")
      testCompile("assertk-jvm")
    }
  }

  dependencyManagement {
    dependencies {
      installKotlinDeps()
      dependency("org.jsoup:jsoup:1.11.2")
      dependency("org.antlr:antlr4:4.7.1")
      dependency("org.antlr:antlr4-runtime:4.7.1")
      dependency("com.willowtreeapps.assertk:assertk-jvm:0.11")
    }
  }

  dependencies {
    compileOnly("com.google.code.findbugs:findbugs") {
      isTransitive = false
    }
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"
      suppressWarnings = false
      freeCompilerArgs += listOf("-Xjsr305=strict", "-Xuse-experimental=kotlin.Experimental")
    }
  }
}

tasks.withType<CoverageReportTask> {
  this.sourceFilter = { exclude("**/liquid/parser/v4/*") }
}

fun DependenciesHandler.installKotlinDeps() {
  val kotlinCoroutines: String by project
  val kotlin: String by project
  val kotlinSerialization: String by project
  val kotlinIO: String by project
  // None
  dependencySet("org.jetbrains.kotlin:$kotlin") {
    entry("kotlin-stdlib")
    entry("kotlin-runtime")
    entry("kotlin-stdlib-common")
    entry("kotlin-stdlib-jdk7")
    entry("kotlin-stdlib-jdk8")
    entry("kotlin-reflect")
    entry("kotlin-test-annotations-common")
    entry("kotlin-test")
    entry("kotlin-test-junit")
  }

  dependencySet("org.jetbrains.kotlinx:$kotlinCoroutines") {
    entry("kotlinx-coroutines-core")
    entry("kotlinx-coroutines-core-common")
    entry("kotlinx-coroutines-jdk8")
  }

  dependencySet("org.jetbrains.kotlinx:$kotlinIO") {
    entry("kotlinx-io")
    entry("kotlinx-io-jvm")
    entry("kotlinx-coroutines-io")
    entry("kotlinx-coroutines-io-jvm")
  }

  dependencySet("org.jetbrains.kotlinx:$kotlinSerialization") {
    entry("kotlinx-serialization-runtime")
    entry("kotlinx-serialization-runtime-common")
    entry("kotlinx-serialization-runtime-jsonparser")
  }
}

