plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    applyXplatConnection(":engine-backend-test-xplat")
    setupJavadoc()
}

dependencies {
    val common_events_version: String by project
    modRuntimeOnly("com.kneelawk.common-events:common-events-test-xplat-mojmap:$common_events_version")
}

kpublish {
    createPublication()
}
