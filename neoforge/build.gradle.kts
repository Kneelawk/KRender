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
    archivesName.set("$archives_base_name-${project.name}")
}

base.libsDirectory.set(rootProject.layout.buildDirectory.map { it.dir("libs") })
java.docsDir.set(rootProject.layout.buildDirectory.map { it.dir("docs").dir(project.name) })

configurations {
    create("dev") {
        isCanBeConsumed = true
        isCanBeResolved = false
    }
}

repositories {
    mavenCentral()
    maven("https://maven.quiltmc.org/repository/release") { name = "Quilt" }
    maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
    maven("https://maven.firstdark.dev/snapshots") { name = "FirstDark" }

    mavenLocal()
}

dependencies {
    val minecraft_version: String by project
    minecraft("com.mojang:minecraft:$minecraft_version")
    val yarn_mappings: String by project
    mappings(loom.officialMojangMappings())

    val neoforge_version: String by project
    neoForge("net.neoforged:neoforge:$neoforge_version")

    compileOnly(project(path = ":xplat", configuration = "namedElements"))
}

java {
    val java_version: String by project
    val javaVersion = JavaVersion.toVersion(java_version)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion

    withSourcesJar()
    withJavadocJar()
}

tasks {
    processResources.configure {
        from(project(":xplat").sourceSets.main.map { it.resources })

        inputs.property("version", project.version)

        filesMatching("META-INF/neoforge.mods.toml") {
            expand(mapOf("version" to project.version))
        }
    }

    withType<JavaCompile>().configureEach {
        source(project(":xplat").sourceSets.main.map { it.allSource })
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
        from(project(":xplat").sourceSets.main.map { it.allSource })
        from(rootProject.file("LICENSE")) {
            rename { "${it}_${rootProject.name}" }
        }
    }

    javadoc.configure {
        source(project(":xplat").sourceSets.main.map { it.allJava })
        exclude("com/kneelawk/knet/impl")
        exclude("com/kneelawk/knet/neoforge/impl")

        val yarn_mappings: String by project
        val jetbrains_annotations_version: String by project
        (options as? StandardJavadocDocletOptions)?.links =
            listOf(
                "https://maven.fabricmc.net/docs/yarn-${yarn_mappings}/",
                "https://javadoc.io/doc/org.jetbrains/annotations/${jetbrains_annotations_version}/"
            )

        options.optionFiles(rootProject.file("javadoc-options.txt"))
    }

    // Brute force prevent gradle from just putting project build dirs on classpath
    create("jarExt", Jar::class) {
        from(compileJava)
        from(processResources)
        from(rootProject.file("LICENSE")) {
            rename { "${it}_${rootProject.name}" }
        }
        archiveClassifier = "jarExt"
        destinationDirectory.set(project.layout.buildDirectory.dir("devlibs"))
    }

    assemble.configure {
        dependsOn("jarExt")
    }

    afterEvaluate {
        named("genSources").configure {
            setDependsOn(listOf("genSourcesWithVineflower"))
        }
    }
}

artifacts {
    add("dev", tasks.getByName("jarExt"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "${rootProject.name}-${project.name}"
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
