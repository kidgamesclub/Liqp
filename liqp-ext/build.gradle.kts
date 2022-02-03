plugins {
  java
  kotlin("jvm")
}

mverse {
  groupId = "io.mverse"
  isDefaultDependencies = false
  coverageRequirement = 0.00
  dependencies {
    testImplementation(assertj())
    testImplementation(assertK())
    testImplementation(junit())
    testImplementation(mockito())
    implementation("jsoup")
//    compile("mverse-lang-jvm")
  }
}

dependencies {
  implementation(project(":liqp-api"))
  implementation(project(":liqp-core"))
  testImplementation(project(":liqp-junit"))
  testImplementation("junit:junit")
}

