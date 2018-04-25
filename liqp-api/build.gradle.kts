plugins {
  id("org.gradle.kotlin.kotlin-dsl")
}

mverse {
  dependencies {
    compile(guava())
    compile("com.github.alexheretic:dynamics:4.0")
  }
}
