plugins {
    id("java-project-conventions")
    id("com.keap.build") version("0.20.2")
    id("com.keap.library-build")
}

dependencies {
  implementation(project(":liqp-api"))
  implementation(project(":liqp-core"))
  implementation(libs.bundles.testLibs)
}
