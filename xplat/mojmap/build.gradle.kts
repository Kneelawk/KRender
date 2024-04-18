import net.fabricmc.loom.LoomGradleExtension
import net.fabricmc.loom.api.mappings.layered.MappingsNamespace
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask

// Copyright (c) 2022 Emi
// MIT License

plugins {
    `maven-publish`
    id("dev.architectury.loom")
    id("com.kneelawk.versioning")
}

evaluationDependsOn(":xplat")

val maven_group: String by project
group = maven_group

val archives_base_name: String by project
base {
    archivesName.set("$archives_base_name-${parent!!.name}-${project.name}")
}

dependencies {
    val minecraft_version: String by rootProject
    minecraft("com.mojang:minecraft:$minecraft_version")

    mappings(loom.officialMojangMappings())
}

val mojmapJar = tasks.create("mojmapJar", RemapJarTask::class) {
    classpath.from((loom as LoomGradleExtension).getMinecraftJarsCollection(MappingsNamespace.INTERMEDIARY))
    dependsOn(project(":xplat").tasks.remapJar)

    inputFile.set(project(":xplat").tasks.remapJar.flatMap { it.archiveFile })
    sourceNamespace.set("intermediary")
    targetNamespace.set("named")

    remapperIsolation.set(true)
}

val mojmapSourcesJar = tasks.create("mojmapSourcesJar", RemapSourcesJarTask::class) {
    classpath.from((loom as LoomGradleExtension).getMinecraftJarsCollection(MappingsNamespace.INTERMEDIARY))
    dependsOn(project(":xplat").tasks.remapSourcesJar)

    archiveClassifier.set("sources")

    inputFile.set(project(":xplat").tasks.remapSourcesJar.flatMap { it.archiveFile })
    sourceNamespace.set("intermediary")
    targetNamespace.set("named")

    remapperIsolation.set(true)
}

tasks {
    build.configure {
        dependsOn(mojmapJar)
        dependsOn(mojmapSourcesJar)
    }

    remapJar.configure {
        archiveClassifier.set("remapJar-disabled")
    }

    remapSourcesJar.configure {
        archiveClassifier.set("remapSourcesJar-disabled")
    }

    afterEvaluate {
        named("genSources").configure {
            setDependsOn(listOf("genSourcesWithVineflower"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "${rootProject.name}-${parent!!.name}-${project.name}"
            artifact(mojmapJar) {
                builtBy(mojmapJar)
                classifier = ""
            }
            artifact(mojmapSourcesJar) {
                builtBy(mojmapSourcesJar)
                classifier = "sources"
            }
        }
    }

    repositories {
        if (System.getenv("PUBLISH_REPO") != null) {
            maven {
                name = "publishRepo"
                url = uri(rootProject.file(System.getenv("PUBLISH_REPO")))
            }
        }
    }
}
