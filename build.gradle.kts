import io.mverse.gradle.task.CoverageReportTask
import io.spring.gradle.dependencymanagement.dsl.DependenciesHandler
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {

  kotlin("jvm")
  id("io.mverse.project") version("0.6.5")
  id("io.mverse.multi-module") version("0.6.5")
  findbugs
  java
}

allprojects {
  plugins.apply("findbugs")
  mverse {
    groupId = "io.mverse"
    isDefaultDependencies = false

    coverageRequirement = 0.60
    dependencies {
      compile("klock-jvm")
      compile("kotlin-stdlib-jdk8")
      compileOnly("kotlin-reflect")
      testCompile("assertk-jvm")
      compile("kotlinx-serialization-runtime")
      compile("kotlinx-collections-immutable")
    }
  }

  repositories {
    maven ("https://kotlin.bintray.com/kotlinx" )
  }

  findbugs { isIgnoreFailures = true }

  dependencyManagement {
    dependencies {
      installKotlinDeps()
      installMverseShared()
      dependency("org.jsoup:jsoup:1.11.2")
      dependency("org.antlr:antlr4:4.7.1")
      dependency("org.antlr:antlr4-runtime:4.7.1")
      dependency("com.willowtreeapps.assertk:assertk-jvm:0.11")
      dependency("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.1")

      val klock:String by rootProject
      dependencySet("com.soywiz.korlibs.klock:$klock") {
        entry("klock-jvm")
        entry("klock")
        entry("klock-metadata")
      }

    }
  }

  dependencies {
    compileOnly("com.google.code.findbugs:findbugs") {
      isTransitive = false
    }
  }

  tasks.withType<KotlinCompile> {
    val isTestSource = name.endsWith("compileTestKotlin")
    kotlinOptions {
      jvmTarget = "1.8"
      freeCompilerArgs += listOf("-Xjsr305=strict", "-XXLanguage:+InlineClasses", "-Xuse-experimental=kotlin.Experimental")
      suppressWarnings = isTestSource
    }
  }
}

tasks.withType<CoverageReportTask> {
  this.sourceFilter = { exclude("**/Examples*","**/liquid/parser/v4/*") }
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

fun DependenciesHandler.installMverseShared() {
  val mverseShared: String by project

  dependencySet("io.mverse:$mverseShared") {
    entry("mverse-json")
    entry("mverse-extensions")
    entry("mverse-i18n")
    entry("mverse-lang-jvm")
    entry("mverse-lang-common")
    entry("mverse-log-common")
    entry("mverse-log-jvm")
    entry("mverse-test-common")
    entry("mverse-test-jvm")
    entry("mverse-coroutines-common")
    entry("mverse-coroutines-jvm")
    entry("mverse-events-common")
    entry("mverse-events-jvm")
  }
}


