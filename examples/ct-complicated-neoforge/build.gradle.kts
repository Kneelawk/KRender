plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
}

submodule {
    applyXplatConnection(":ct-complicated-xplat")
    generateRuns()
}

neoForge {
    mods {
        create("krender_model_loading") {
            dependency(project(":model-loading-neoforge"))
        }
        create("krender_model_creation") {
            dependency(project(":model-creation-neoforge"))
        }
    }
}
