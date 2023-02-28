import io.mverse.gradle.task.CoverageReportTask
import io.spring.gradle.dependencymanagement.dsl.DependenciesHandler
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {

}


tasks.withType<CoverageReportTask> {
  this.sourceFilter = { exclude("**/Examples*","**/liquid/parser/v4/*") }
}
