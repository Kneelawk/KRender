plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    applyMixinExpansions()
    setupJavadoc()
    xplatProjectDependency(":reload-listener")
    val common_events_version: String by project
    xplatExternalDependency(include = false) { "com.kneelawk.common-events:common-events-$it:$common_events_version" }
}

kpublish {
    createPublication("mojmap")
}
