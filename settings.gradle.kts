pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://dl.bintray.com/nokeedev/distributions-snapshots") }
    }
    val nokeeVersion = extra["nokee.version"].toString()
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("dev.nokee.")) {
                useModule("${requested.id.id}:${requested.id.id}.gradle.plugin:$nokeeVersion")
            }
        }
    }
    plugins {
        fun String.v() = extra["$this.version"].toString()
        fun PluginDependenciesSpec.idv(id: String, key: String = id) = id(id) version key.v()

        idv("com.github.vlsi.crlf", "com.github.vlsi.vlsi-release-plugins")
        idv("com.github.vlsi.gradle-extensions", "com.github.vlsi.vlsi-release-plugins")
        idv("com.github.vlsi.ide", "com.github.vlsi.vlsi-release-plugins")
        idv("com.github.vlsi.license-gather", "com.github.vlsi.vlsi-release-plugins")
        idv("com.github.vlsi.stage-vote-release", "com.github.vlsi.vlsi-release-plugins")
        idv("org.jetbrains.intellij", "org.jetbrains.intellij")
        idv("org.jetbrains.kotlin.jvm", "kotlin")
    }
}

include(
    "dependencies-bom",
    "plugin",
    "base",
    "windows",
    "macos"
)

rootProject.name = "auto-dark-mode"

for (p in rootProject.children) {
    if (p.children.isEmpty()) {
        // Rename leaf projects only
        // E.g. we don't expect to publish examples as a Maven module
        p.name = rootProject.name + "-" + p.name
    }
}
