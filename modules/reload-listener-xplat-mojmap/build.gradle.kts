plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    applyXplatConnection(":reload-listener-xplat")
    setupJavadoc()
}

kpublish {
    createPublication()
}
