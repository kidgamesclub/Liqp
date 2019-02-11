plugins {
  kotlin("jvm")
  java
}

mverse {
  dependencies {
    compile(guava())
    compile("com.github.alexheretic:dynamics:4.0")
  }
}
