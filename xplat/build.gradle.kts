plugins {
    `maven-publish`
    id("dev.architectury.loom")
    id("com.kneelawk.versioning")
}

val maven_group: String by project
group = maven_group

val archives_base_name: String by project
base {
    archivesName.set("$archives_base_name-${project.name}-intermediary")
}

java.docsDir.set(rootProject.layout.buildDirectory.map { it.dir("docs").dir(project.name) })

repositories {
    mavenCentral()
    maven("https://maven.quiltmc.org/repository/release") { name = "Quilt" }

    mavenLocal()
}

dependencies {
    val minecraft_version: String by project
    minecraft("com.mojang:minecraft:$minecraft_version")
    val yarn_mappings: String by project
    mappings(loom.officialMojangMappings())

    // Using modCompileOnly & modLocalRuntime so that these dependencies don't get brought into any projects that depend
    // on this one.

    // Fabric Loader
    val fabric_loader_version: String by project
    modCompileOnly("net.fabricmc:fabric-loader:$fabric_loader_version")

    testImplementation("junit:junit:4.13.2")
}

java {
    val java_version: String by project
    val javaVersion = JavaVersion.toVersion(java_version)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion

    withJavadocJar()
    withSourcesJar()
}

tasks {
    processResources.configure {
        inputs.property("version", project.version)

        filesMatching("quilt.mod.json") {
            expand(mapOf("version" to project.version))
        }
        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to project.version))
        }
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        val java_version: String by project
        options.release.set(java_version.toInt())
    }

    jar.configure {
        from(rootProject.file("LICENSE")) {
            rename { "${it}_${archives_base_name}" }
        }
    }

    named("sourcesJar", Jar::class).configure {
        from(rootProject.file("LICENSE")) {
            rename { "${it}_${rootProject.name}" }
        }
    }

    javadoc.configure {
        exclude("com/kneelawk/commonrender/impl")

        val yarn_mappings: String by project
        val jetbrains_annotations_version: String by project
        (options as? StandardJavadocDocletOptions)?.links =
            listOf(
                "https://maven.fabricmc.net/docs/yarn-${yarn_mappings}/",
                "https://javadoc.io/doc/org.jetbrains/annotations/${jetbrains_annotations_version}/"
            )

        options.optionFiles(rootProject.file("javadoc-options.txt"))
    }

    test.configure {
        useJUnit()
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
            artifactId = "${rootProject.name}-${project.name}-intermediary"
            from(components["java"])
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
