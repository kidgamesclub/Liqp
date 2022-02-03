plugins {
  kotlin("jvm")
  java
}

mverse {
  dependencies {
//    compile(guava())
    implementation("assertk-jvm")
  }
  coverageRequirement = 0.00
}

dependencies {
  implementation(project(":liqp-api"))
  implementation(project(":liqp-core"))
  implementation("javax.json:javax.json-api:1.1.3")
  implementation("org.assertj:assertj-core")
  implementation("net.wuerl.kotlin:assertj-core-kotlin:0.2.1")
}
