plugins {
    id("java-project-conventions")
    id("antlr")
    id("release-conventions")
}

dependencies {
    implementation(libs.guava)
    implementation(libs.kotlin.reflect)
    implementation(libs.javaxJsonApi)
    testImplementation(libs.bundles.testLibs)
    testImplementation(libs.bundles.jackson)

    implementation(project(":liqp-api"))
    testImplementation(project(":liqp-junit"))
    implementation(rootProject.libs.guava)
    implementation(libs.bundles.antlr)

    antlr(libs.antlr4)
    testImplementation(libs.jsoup)
}

val generateGrammarSource by tasks.getting(AntlrTask::class)
tasks["compileKotlin"].dependsOn(generateGrammarSource)

kotlin {
    sourceSets["main"].apply {
        kotlin.srcDir(generateGrammarSource.outputDirectory)
    }
}


// #### Configure antlr ##### //
// ########################## //

tasks.withType(AntlrTask::class.java) {
    arguments = listOf("-visitor", "-package", "liquid.parser.v4", "-Xexact-output-dir")
    outputDirectory = project.file("build/generated-src/antlr/main/liquid/parser/v4")
}

