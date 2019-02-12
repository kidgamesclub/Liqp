plugins {
  java
  kotlin("jvm")
}

mverse {
  groupId = "io.mverse"
  isDefaultDependencies = false
  coverageRequirement = 0.00
  dependencies {
    testCompile(assertj())
    testCompile(assertK())
    testCompile(junit())
    testCompile(mockito())
    compile("jsoup")
    compile("mverse-lang-jvm")
  }
}

dependencies {
  compile(project(":liqp-api"))
  compile(project(":liqp-core"))
  testCompile(project(":liqp-junit"))
}

