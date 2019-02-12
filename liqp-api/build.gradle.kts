plugins {
  kotlin("jvm")
  java
}

mverse {
  dependencies {
    compile(guava())
    compile("mverse-lang-jvm")
    compile("com.github.alexheretic:dynamics:4.0")
    
  }
}
