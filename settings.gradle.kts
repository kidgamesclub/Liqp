pluginManagement {
    repositories {
        mavenLocal()
        jcenter()
        gradlePluginPortal()
        google()
        maven {
            setUrl("https://edspuzzle.jfrog.io/artifactory/edspuzzle-gradle-release/")
            credentials {
                username = System.getProperty("ARTIFACTORY_USERNAME")
                password = System.getProperty("ARTIFACTORY_PASSWORD")
            }
        }
        maven("https://kotlin.jfrog.io/kotlinx")
    }

    enableFeaturePreview("VERSION_CATALOGS")

    val kotlin: String by settings
    val mversePlugin: String by settings

    val pluginVersionMap = mapOf(
        "kotlinx-serialization" to kotlin,
        "kotlin-multiplatform" to kotlin,
        "org.jetbrains.kotlin.jvm" to kotlin,
        "org.jetbrains.kotlin.common" to kotlin,
        "io.mverse.project" to mversePlugin,
        "io.mverse.code-generation" to mversePlugin,
        "io.mverse.multi-module" to mversePlugin,
        "io.mverse.multi-platform" to mversePlugin
    )

    resolutionStrategy {
        eachPlugin {

            if (requested.id.id in pluginVersionMap) {
                useVersion(pluginVersionMap[requested.id.id])
            }

            if (requested.id.id == "kotlin-multiplatform") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${target.version}")
            }

            if (requested.id.id == "kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${target.version}")
            }
        }
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {

            bundles {
                library("org.jsoup:jsoup:1.11.2")
                library("com.google.guava:guava:31.1-jre")

                val kotlin: String by settings
                group(name = "org.jetbrains.kotlin", version = kotlin) {
                    library("kotlin-stdlib")
                    library("kotlin-runtime")
                    library("kotlin-stdlib-common")
                    library("kotlin-stdlib-jdk7")
                    library("kotlin-stdlib-jdk8")
                    library("kotlin-reflect")
                    library("kotlin-test-annotations-common")
                    library("kotlin-test")
                    library("kotlin-test-junit")
                }
                val kotlinCoroutines: String by settings

                group("org.jetbrains.kotlinx", version = kotlinCoroutines) {
                    library("kotlinx-coroutines-core")
                    library("kotlinx-coroutines-core-common")
                    library("kotlinx-coroutines-jdk8")
                }
                bundle("jackson") {
                    group("com.fasterxml.jackson.core", version="2.14.2") {
                        library("jackson-core")
                        library("jackson-databind")
                        library("jackson-annotations")

                    }
                }

                bundle("testLibs") {
                    library("junit:junit:4.13.2")
                    library("org.mockito:mockito-core:5.1.1")
                    library("org.assertj:assertj-core:3.24.2")
                    library("com.willowtreeapps.assertk:assertk-jvm:0.25")
                    library("net.wuerl.kotlin:assertj-core-kotlin:0.2.1")
                    library("jackson-core")
                    library("jackson-databind")
                    library("jsoup")
                }

                bundle("antlr") {
                    group("org.antlr", version = "4.9.3") {
                        library("antlr4")
                        library("antlr4-runtime")
                    }
                }


                val klock: String by settings
                bundle("klock") {
                    group("com.soywiz.korlibs.klock", version = klock) {
                        library("klock-jvm")
                        library("klock")
                        library("klock-metadata")
                    }
                }
            }
        }
    }
}

rootProject.name = "liqp"
include("liqp-api")
include("liqp-junit")
include("liqp-core")
include("liqp-ext")

/// BELOW IS FOR MAKING CATALOGS EASIER TO DEFINE
interface HandlesGroup {
    val catalog: VersionCatalogBuilder
    fun appendLibrary(name: String): Any? = false
    fun group(name: String, group: String? = null, version: String? = null, block: GroupSpec.() -> Unit) {

        val group = GroupSpec(
            delegate = catalog, parent = this,
            name = sanitizeGroupName(name),
            group = group ?: name ?: error("Invalid group definition.  Must pass key/value args"),
            version = version
        )
        if (group.version != null) catalog.version(group.name, group.version)
        group.block()
    }

    fun library(library: String, alias: String? = null) {
        val parts = library.split(":")
        if (parts.size == 1) {
            /// We are adding something already aliased
            appendLibrary(sanitizeGroupName(library))
            return
        }

        val alias = sanitizeGroupName(alias ?: parts[1])
        val aliased = when {
            parts.size > 2 -> catalog.library(alias, library)
            parts.size == 1 -> {
                // already aliased
            }

            else -> catalog.library(alias, parts[0], parts[1])
                .withoutVersion()
        }
        appendLibrary(alias)
        return aliased
    }

    companion object {
        fun sanitizeGroupName(value: Any?) = value.toString().replace("-", "_").replace("\\.", "_")
    }
}

class KeapBundleSpec(val name: String, override val catalog: VersionCatalogBuilder, val libs: MutableList<String>) :
    HandlesGroup {
    override fun appendLibrary(name: String): Any? {
        return libs.add(name)
    }

    fun finisher() = catalog.bundle(name, libs)
}

class GroupSpec(
    val delegate: VersionCatalogBuilder,
    val parent: HandlesGroup,
    val name: String,
    val group: String = name,
    val version: String? = null
) {
    fun library(name: String, alias: String = name) {
        val definedAlias = delegate.alias(alias).to(group, name)
        if (version != null) definedAlias.versionRef(group) else definedAlias.withoutVersion()
        parent.appendLibrary(alias)
    }
}

class KeapCatalogSpec(override val catalog: VersionCatalogBuilder) : HandlesGroup {
    fun bundle(name: String, block: KeapBundleSpec.() -> Unit) {
        val bundle = KeapBundleSpec(catalog = catalog, name = name, libs = mutableListOf())
        bundle.block()
        bundle.finisher()
    }
}

fun VersionCatalogBuilder.bundles(block: KeapCatalogSpec.() -> Unit) {
    val bundle = KeapCatalogSpec(catalog = this)
    bundle.block()
}

