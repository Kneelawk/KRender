plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    applyMixinExpansions()
    setupJavadoc()
    val common_events_version: String by project
    xplatExternalDependency(include = false) { "com.kneelawk.common-events:common-events-$it:$common_events_version" }
}

kpublish {
    createPublication("mojmap")
}

minivan {
    version("1.21.8")
    accessWideners(project(":model-loading-fabric").file("src/main/resources/krender_model_loading.accesswidener"))
}
