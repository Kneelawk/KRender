plugins {
    id("com.kneelawk.versioning")
    id("com.kneelawk.submodule")
    id("com.kneelawk.kpublish")
}

submodule {
    setLibsDirectory()
    applyXplatConnection(":model-guard-xplat")
    setupJavadoc()
}

kpublish {
    createPublication()
}
