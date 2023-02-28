plugins {
  id  ("java-project-conventions")
}

dependencies {
  implementation(project(":liqp-api"))
  implementation(project(":liqp-core"))
  testImplementation(project(":liqp-junit"))
  testImplementation(libs.bundles.testLibs)

  implementation(libs.jsoup)
}

