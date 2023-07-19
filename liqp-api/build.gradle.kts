plugins {
    id("java-project-conventions")
    id("com.keap.build") version("0.20.2")
    id("com.keap.library-build")
    //id("release-conventions")
}

dependencies {
  implementation(libs.javax.json)
}

