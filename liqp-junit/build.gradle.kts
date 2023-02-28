plugins {
    id("java-project-conventions")
}

dependencies {
  implementation(project(":liqp-api"))
  implementation(project(":liqp-core"))
  implementation(libs.bundles.testLibs)
}
