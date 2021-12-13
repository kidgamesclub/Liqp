plugins {
  kotlin("jvm")
  java
}

mverse {
  dependencies {
//    implementation("mverse-lang-jvm")
    implementation("guava")
    implementation("javax.json:javax.json-api:1.1.3")
  }
}
