plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    setRefmaps("krender_engine_backend_test")
    xplatProjectDependency(":engine-api", include = false)
    setupJavadoc()
}

dependencies {
    val common_events_version: String by project
    modRuntimeOnly("com.kneelawk.common-events:common-events-test-xplat-intermediary:$common_events_version")
}

kpublish {
    createPublication("intermediary")
}
