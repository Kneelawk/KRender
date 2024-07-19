pluginManagement {
    repositories {
        maven("https://maven.quiltmc.org/repository/release") {
            name = "Quilt"
        }
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        maven("https://maven.architectury.dev/") {
            name = "Architectury"
        }
        maven("https://maven.neoforged.net/releases/") {
            name = "NeoForged"
        }
        gradlePluginPortal()
    }
    plugins {
        val architectury_loom_version: String by settings
        id("dev.architectury.loom") version architectury_loom_version
    }
}

include(":xplat")
include(":fabric")
include(":neoforge")

//include(":example-xplat")
//project(":example-xplat").projectDir = file("example/xplat")
//include(":example-fabric")
//project(":example-fabric").projectDir = file("example/fabric")
//include(":example-neoforge")
//project(":example-neoforge").projectDir = file("example/neoforge")

rootProject.name = "krender"
