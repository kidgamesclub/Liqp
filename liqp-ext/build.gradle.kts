plugins {
  id  ("java-project-conventions")
//  id("com.keap.build")
//  id("com.keap.library-build")


  id("release-conventions")
}

dependencies {
  implementation(project(":liqp-api"))
  implementation(project(":liqp-core"))
  testImplementation(project(":liqp-junit"))
  testImplementation(libs.bundles.testLibs)
  implementation(libs.jsoup)
}

