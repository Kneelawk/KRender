tasks.create("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

subprojects {
    tasks {
        // make builds reproducible
        withType<AbstractArchiveTask>().configureEach {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
        }

        withType<GenerateModuleMetadata>().configureEach {
            enabled = false
        }
    }
}
