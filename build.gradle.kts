import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.internal.file.pattern.PatternMatcherFactory.compile
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCallArgument.DefaultArgument.arguments

plugins {
  id("org.gradle.kotlin.kotlin-dsl").version("0.16.0")
  id("io.mverse.project").version("0.5.16")
  id("antlr")
}

mverse {

  //        checkstyleLocation = "/Users/ericm/etc/checkstyle/checkstyle"
  modules {
    compile("jackson-annotations")
    compile("jackson-databind")
    compile("jackson-core")
    compile("findbugs")
    compile("kotlin-stdlib")
    compile("org.jsoup:jsoup:1.11.2")
  }
}

dependencies {
  compile("org.antlr:antlr4-runtime:4.7.1")
  antlr("org.antlr:antlr4:4.7.1")

}

tasks.withType(AntlrTask::class.java) {
  arguments = listOf("-visitor", "-Xexact-output-dir", "-package", "liquid.parser.v4")
}


//    <dependency>
//    <groupId>org.jsoup</groupId>
//    <artifactId>jsoup</artifactId>
//    <version>${jsoup.version}</version>
//    </dependency>
//

