plugins {
    id("java-project-conventions")
//    id("com.keap.build")
//    id("com.keap.library-build")
    id("release-conventions")
}

dependencies {
  implementation(project(":liqp-api"))
  implementation(project(":liqp-core"))
  implementation(libs.bundles.testLibs)
}
