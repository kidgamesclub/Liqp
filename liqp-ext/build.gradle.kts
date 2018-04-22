plugins {
  id("org.gradle.kotlin.kotlin-dsl")
  id("findbugs")
}

mverse {
  groupId = "club.kidgames"
  isDefaultDependencies = false
  coverageRequirement = 0.00
  dependencies {
    testCompile(assertj())
    testCompile(junit())
    testCompile(mockito())
    compile("jsoup")
  }
}

dependencies {
  compile(project(":liqp-core"))
  testCompile(project(":liqp-junit"))
}

findbugs {
  isIgnoreFailures = true
}
