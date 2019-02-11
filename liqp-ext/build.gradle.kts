plugins {
  java
  id("findbugs")
}

mverse {
  groupId = "io.mverse"
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

