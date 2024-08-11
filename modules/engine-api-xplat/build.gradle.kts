plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    setRefmaps("krender_engine_api")
    setupJavadoc()
    val common_events_version: String by project
    xplatExternalDependency { "com.kneelawk.common-events:common-events-$it:$common_events_version" }
}

kpublish {
    createPublication("intermediate")
}
