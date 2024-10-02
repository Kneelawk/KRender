plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
}

submodule {
    applyXplatConnection(":ct-complicated-xplat")
    generateRuns()
}

dependencies {
    implementation(project(":engine-backend-neoforge"))
    jarJar(project(":engine-backend-neoforge"))
}

neoForge {
    mods {
//        create("krender_engine_backend_neoforge") {
//            dependency(project(":engine-backend-neoforge"))
//        }
    }
    runs {
        configureEach {
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }
}
