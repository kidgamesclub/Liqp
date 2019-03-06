plugins {
  kotlin("jvm")
  java
}

mverse {
  dependencies {
//    compile(guava())
    compile("assertk-jvm")
  }
  coverageRequirement = 0.00
}

dependencies {
  compile(project(":liqp-api"))
  compile(project(":liqp-core"))
  compile("org.assertj:assertj-core")
  compile("net.wuerl.kotlin:assertj-core-kotlin:0.2.1")
}
